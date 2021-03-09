package com.jamolkhon.assessment.accmgm.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jamolkhon.assessment.accmgm.R
import com.jamolkhon.assessment.accmgm.databinding.NewTransactionBinding

class NewTransactionView : LinearLayout {

  //private var onSubmitClickListener: ((accountId: String, amount: String) -> Unit)? = null

  private lateinit var binding: NewTransactionBinding

  constructor(context: Context) : super(context) {
    init(null, 0)
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(attrs, 0)
  }

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
    context,
    attrs,
    defStyle
  ) {
    init(attrs, defStyle)
  }

  private fun init(attrs: AttributeSet?, defStyle: Int) {
    orientation = VERTICAL
    binding = NewTransactionBinding.inflate(LayoutInflater.from(context), this)
  }

  fun setOnSubmitClickListener(listener: (accountId: String, amount: String) -> Unit) {
    //onSubmitClickListener = listener
    binding.submitBtn.setOnClickListener {
      listener(binding.accountIdInput.text.toString(), binding.amountInput.text.toString())
    }
  }

  fun setLoading(loading: Boolean) {
    binding.submitBtn.isEnabled = !loading
    binding.submitBtn.setText(if (loading) R.string.submitting else R.string.submit)
  }

  fun clearInputs() {
    binding.accountIdInput.text = null
    binding.amountInput.text = null
  }
}
