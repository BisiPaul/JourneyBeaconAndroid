package com.fluffydevs.journeybeacon.presentation.dashboard

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.fluffydevs.journeybeacon.R
import com.fluffydevs.journeybeacon.common.structure.BaseAdapter
import com.fluffydevs.journeybeacon.data.payment.getpayments.PaymentModel
import com.fluffydevs.journeybeacon.databinding.ItemPaymentBinding

const val VIEW_TYPE_LIST_ITEM = 1

class PaymentsAdapter(

) : BaseAdapter<PaymentModel, ViewDataBinding>(
    diffCallback = PaymentsAdapterDiffUtil()
) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is PaymentModel -> VIEW_TYPE_LIST_ITEM
            else -> throw Exception("Unhandled item view type")
        }

    override fun getItemLayout(viewType: Int): Int =
        when (viewType) {
            VIEW_TYPE_LIST_ITEM -> R.layout.item_payment
            else -> throw Exception("Unhandled item layout")
        }

    override fun bind(
        binding: ViewDataBinding,
        item: PaymentModel,
        position: Int
    ) {
        when (getItemViewType(position)) {
            VIEW_TYPE_LIST_ITEM -> {
                (binding as ItemPaymentBinding).apply {
                    this.item = item
                }
            }

            else -> {
                throw Exception("Unhandled item binding")
            }
        }
    }

}

class PaymentsAdapterDiffUtil : DiffUtil.ItemCallback<PaymentModel>() {

    override fun areItemsTheSame(
        oldItem: PaymentModel,
        newItem: PaymentModel,
    ): Boolean {
        return oldItem.javaClass == newItem.javaClass
    }

    override fun areContentsTheSame(
        oldItem: PaymentModel,
        newItem: PaymentModel,
    ): Boolean {
        return oldItem == newItem
    }
}