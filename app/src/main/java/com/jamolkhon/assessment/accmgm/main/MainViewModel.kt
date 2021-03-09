package com.jamolkhon.assessment.accmgm.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamolkhon.assessment.accmgm.AssistedSavedStateViewModelFactory
import com.jamolkhon.assessment.accmgm.TransactionStatus
import com.jamolkhon.assessment.accmgm.api.AccountApi
import com.jamolkhon.assessment.accmgm.api.AmountDto
import com.jamolkhon.assessment.accmgm.database.Balance
import com.jamolkhon.assessment.accmgm.database.BalanceDao
import com.jamolkhon.assessment.accmgm.database.Transaction
import com.jamolkhon.assessment.accmgm.database.TransactionDao
import com.jamolkhon.assessment.accmgm.rethrowCancellation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.util.*

class MainViewModel @AssistedInject constructor(
  private val accountApi: AccountApi,
  private val transactionDao: TransactionDao,
  private val balanceDao: BalanceDao,
  @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  @AssistedFactory
  interface Factory : AssistedSavedStateViewModelFactory<MainViewModel>

  init {
    viewModelScope.launch {
      transactionDao.transactions().collect { transactions ->
        val items = transactions.map { transaction ->
          Items.TransactionItem(
            transaction.id,
            transaction.accountId,
            transaction.amount,
            transaction.balance,
            transaction.status == TransactionStatus.FAILED
          )
        }
        state {
          copy(
            items = listOf(Items.NewTransaction, Items.TransactionsHeader).plus(items)
          )
        }
      }
    }
  }

  private val _states = MutableStateFlow(MainViewState())
  val states: StateFlow<MainViewState> = _states

  private val _events = MutableSharedFlow<MainEvent>(0, 100)
  val events: Flow<MainEvent> = _events

  fun submitTransaction(accountId: String, amount: String) {
    if (_states.value.submitting) {
      return
    }
    viewModelScope.launch {
      val result = runCatching { UUID.fromString(accountId) }
      if (result.isFailure) {
        _events.emit(MainEvent.InvalidAccountId)
        return@launch
      }

      val amountLong = amount.toLongOrNull()
      if (amountLong == null || amountLong == 0L) {
        _events.emit(MainEvent.InvalidAmount)
        return@launch
      }

      state { copy(submitting = true) }

      val transactionId = UUID.randomUUID().toString()
      val transactionStatus = updateAccount(transactionId, accountId, amountLong)

      try {
        val transaction = Transaction(
          transactionId,
          accountId,
          amountLong,
          OffsetDateTime.now(),
          transactionStatus
        )
        transactionDao.insertTransaction(transaction)
      } catch (e: Exception) {
        e.rethrowCancellation()
        _events.emit(MainEvent.TransactionNotSaved)
      }

      state { copy(submitting = false) }
    }
  }

  fun retryTransaction(transactionId: String) {
    viewModelScope.launch {
      state { copy(retrying = true) }
      val transaction = transactionDao.transactionById(transactionId)
      if (transaction.status == TransactionStatus.FAILED) {
        val transactionStatus =
          updateAccount(transactionId, transaction.accountId, transaction.amount)
        if (transactionStatus != transaction.status) {
          try {
            transactionDao.updateTransaction(transaction.copy(status = transactionStatus))
          } catch (e: Exception) {
            e.rethrowCancellation()
            _events.emit(MainEvent.TransactionNotSaved)
          }
        }
      }
      state { copy(retrying = false) }
    }
  }

  private suspend fun updateAccount(
    transactionId: String,
    accountId: String,
    amount: Long
  ): TransactionStatus {
    state { copy(updating = true) }
    val status = try {
      val response = accountApi.updateAmount(transactionId, AmountDto(accountId, amount))
      when (response.code()) {
        200 -> {
          viewModelScope.launch {
            try {
              updateAccountBalance(accountId)
            } catch (e: Exception) {
              _events.emit(MainEvent.BalanceUpdateFailed)
            }
          }
          TransactionStatus.COMPLETED
        }
        else -> {
          _events.emit(MainEvent.SubmitFailed)
          TransactionStatus.FAILED
        }
      }
    } catch (e: Exception) {
      e.rethrowCancellation()
      _events.emit(MainEvent.SubmitFailed)
      TransactionStatus.FAILED
    }
    state { copy(updating = false) }
    return status
  }

  private suspend fun updateAccountBalance(accountId: String) {
    val balanceDto = accountApi.getBalance(accountId)
    balanceDao.insertOrUpdate(Balance(accountId, balanceDto.body()!!.balance))
  }

  private suspend fun state(block: MainViewState.() -> MainViewState) {
    _states.emit(block(_states.value))
  }
}
