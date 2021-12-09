package com.openclassrooms.realestatemanager.ui.main.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.databinding.EstateListContentBinding
import java.text.DecimalFormat


class ListAdapter(private val listener: (ListViewState) -> Unit) :
    ListAdapter<ListViewState, com.openclassrooms.realestatemanager.ui.main.list.ListAdapter.EstateViewHolder>(
        EstateDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EstateViewHolder(
        EstateListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) =
        holder.bind(getItem(position), listener)


    class EstateViewHolder(private val binding: EstateListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(estateViewState: ListViewState, listener: (ListViewState) -> Unit) {

            binding.tvContentDistrict.text = estateViewState.district
            binding.tvContentPrice.text =
                DecimalFormat("#,###")
                    .format(estateViewState.price)
                    .toString()
            binding.tvContentType.text = estateViewState.type

//            binding.ivEstateContent.load(Uri.parse(estateViewState.photo.image))
            binding.ivEstateContent.load(Uri.parse(estateViewState.photo.storageUriString))

//            Log.d(TAG, "bind: ${estateViewState.onSale}")
            if (!estateViewState.onSale) {
                binding.tvContentSold.visibility = View.VISIBLE
                binding.tvContentPrice.visibility = View.INVISIBLE
            } else {
                binding.tvContentSold.visibility = View.GONE
                binding.tvContentPrice.visibility = View.VISIBLE
            }

            itemView.setOnClickListener { listener(estateViewState) }
        }
    }

    private class EstateDiffCallback : DiffUtil.ItemCallback<ListViewState>() {
        override fun areItemsTheSame(oldItem: ListViewState, newItem: ListViewState) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ListViewState, newItem: ListViewState) =
            oldItem.id == newItem.id
    }


}