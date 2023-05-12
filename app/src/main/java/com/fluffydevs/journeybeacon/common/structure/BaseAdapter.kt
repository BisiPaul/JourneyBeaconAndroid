package com.fluffydevs.journeybeacon.common.structure

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

/**
 *
 *  A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 *  @param <T> Type of the items in the list
 *  @param <V> The type of the ViewDataBinding
</V></T> */
abstract class BaseAdapter<Item, VH : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<Item>) :
    ListAdapter<Item, BaseViewHolder<VH>>(
        AsyncDifferConfig.Builder<Item>(diffCallback).setBackgroundThreadExecutor(
            Executors.newSingleThreadExecutor()
        ).build()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VH> {
        val binding = createBinding(parent, viewType)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holderBase: BaseViewHolder<VH>, position: Int) {
        bind(holderBase.binding, getItem(position), position)
        holderBase.binding.executePendingBindings()
    }

    private fun createBinding(parent: ViewGroup, viewType: Int): VH {

        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getItemLayout(viewType),
            parent,
            false,
            createDataBindingComponent()
        )
    }

    protected abstract fun getItemLayout(viewType: Int): Int

    protected abstract fun bind(binding: VH, item: Item, position: Int)

    open fun createDataBindingComponent() : DataBindingComponent?{
        return null
    }
}


/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <T> The type of the ViewDataBinding.
</T> */
class BaseViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
    RecyclerView.ViewHolder(binding.root)