package com.garibyan.armen.tbc_classwork_5.screens.main

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.garibyan.armen.tbc_classwork_5.databinding.RvItemChooserBinding
import com.garibyan.armen.tbc_classwork_5.databinding.RvItemInputBinding
import com.garibyan.armen.tbc_classwork_5.nodel.network.ViewItem

class ViewItemAdapter : ListAdapter<ViewItem, RecyclerView.ViewHolder>(ItemViewCallBack()) {

    inner class InputViewHolder(private val binding: RvItemInputBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ViewItem) = with(binding) {
            icon.load(item.icon?.let { "https://icon-library.com/images/image-error-icon/image-error-icon-21.jpg" })
            edtText.hint = item.hint?.let { it }
            edtText.inputType = item.keyboard?.let { getInputType(it) }!!
            tvRequired.visibility = item.required?.let { isVisible(it) }!!
            root.visibility = item.isActive?.let { isVisible(it) }!!
//            isActive, rogorc mivxvdi, saertod gvinda es value tu ara, amiton rootis visibility davsete
//            am shemtxvevashi let arafershi ar gvchirdeba, magram samomavlod dagvchirdeba da davtove
        }
    }

    inner class ChooserViewHolder(private val binding: RvItemChooserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ViewItem) = with(binding) {
            icon.load(item.icon?.let { "https://icon-library.com/images/image-error-icon/image-error-icon-21.jpg" })
            tvRequired.visibility = item.required?.let { isVisible(it) }!!
        }
    }

    private fun isVisible(value: Any): Int{
        var requiredVisibility = View.GONE

        when (value) {
            is String -> if (value == "true") requiredVisibility = View.VISIBLE
            is Boolean -> if(value) requiredVisibility = View.VISIBLE
        }
        return requiredVisibility
    }

    private fun getInputType(value: String): Int{
        return if (value == "number") InputType.TYPE_CLASS_NUMBER
        else InputType.TYPE_CLASS_TEXT
    }

    class ItemViewCallBack : DiffUtil.ItemCallback<ViewItem>() {
        override fun areItemsTheSame(oldItem: ViewItem, newItem: ViewItem) =
            oldItem.fieldId == newItem.fieldId

        override fun areContentsTheSame(oldItem: ViewItem, newItem: ViewItem) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ViewType.INPUT -> InputViewHolder(
                RvItemInputBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> ChooserViewHolder(
                RvItemChooserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InputViewHolder -> holder.bind(getItem(position))
            is ChooserViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).fieldType) {
            "input" -> ViewType.INPUT
            else -> ViewType.CHOOSER
        }
    }
}