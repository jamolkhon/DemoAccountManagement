package com.jamolkhon.assessment.accmgm.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.jamolkhon.assessment.accmgm.App
import com.jamolkhon.assessment.accmgm.R
import com.jamolkhon.assessment.accmgm.databinding.*
import com.jamolkhon.assessment.accmgm.savedStateViewModels
import com.jamolkhon.assessment.accmgm.visible
import com.squareup.cycler.Recycler
import com.squareup.cycler.toDataSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private lateinit var recycler: Recycler<Items>

  private val vm: MainViewModel by savedStateViewModels { savedStateHandle ->
    (application as App).component.mainViewModelFactory().create(savedStateHandle)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    recycler = recycler(binding.listView)
  }

  private fun recycler(recyclerView: RecyclerView): Recycler<Items> {
    return Recycler.adopt(recyclerView) {
      row<Items.NewTransaction, View> {
        create(R.layout.new_transaction_card) {
          val binding = NewTransactionCardBinding.bind(view)
          binding.newTransactionView.setOnSubmitClickListener { accountId, amount ->
            vm.submitTransaction(accountId, amount)
          }
        }
      }
      row<Items.TransactionsHeader, View> {
        create(R.layout.transactions_header) {
        }
      }
      row<Items.TransactionItem, View> {
        create(R.layout.transaction_item) {
          val binding = TransactionItemBinding.bind(view)
          bind { item ->
            binding.actionView.text = if (item.amount > 0) {
              getString(R.string.transferred_x_to_x, item.amount, item.id)
            }
            else {
              getString(R.string.withdrew_n_from_x, item.amount, item.id)
            }
            binding.balanceView.text = getString(R.string.current_account_balance, item.balance)
            binding.retryButton.visible = item.failed
            binding.retryButton.setOnClickListener {
              vm.retryTransaction(item.id)
            }
          }
        }
      }
      /*stableId {}
      compareItemsContent { items, items2 -> }*/
    }
  }

  private var stateJob: Job? = null
  private var eventJob: Job? = null
  override fun onStart() {
    super.onStart()
    stateJob = lifecycleScope.launch {
      vm.states.collect { state ->
        binding.loader.visible = state.requestInProgress()
        recycler.update {
          data = state.items.toDataSource()
        }
      }
    }
    eventJob = lifecycleScope.launch {
      vm.events.collect { event ->
        Toast.makeText(this@MainActivity, event.javaClass.simpleName, Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onStop() {
    super.onStop()
    stateJob?.cancel()
    stateJob = null
    eventJob?.cancel()
    eventJob = null
  }
}
