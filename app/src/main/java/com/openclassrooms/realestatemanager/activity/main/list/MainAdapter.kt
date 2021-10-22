package com.openclassrooms.realestatemanager.activity.main.list

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


class MainAdapter(private val listener: (MainViewState) -> Unit) :
    ListAdapter<MainViewState, MainAdapter.EstateViewHolder>(
        EstateDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EstateViewHolder(
        EstateListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) =
        holder.bind(getItem(position), listener)


    class EstateViewHolder(private val binding: EstateListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(estateViewState: MainViewState, listener: (MainViewState) -> Unit) {

//            Glide.with(binding.root)
//                .load(estateViewState.photo.path)
//                .fitCenter()
//                .into(binding.ivEstateContent)

            binding.tvContentDistrict.text = estateViewState.district
            binding.tvContentPrice.text =
                DecimalFormat("#,###")
                    .format(estateViewState.price)
                    .toString()
            binding.tvContentType.text = estateViewState.type

            binding.ivEstateContent.load(Uri.parse(estateViewState.photo?.image))

            if(!estateViewState.onSale) binding.tvContentSold.visibility = View.VISIBLE

            itemView.setOnClickListener { listener(estateViewState) }
        }
    }

    private class EstateDiffCallback : DiffUtil.ItemCallback<MainViewState>() {
        override fun areItemsTheSame(oldItem: MainViewState, newItem: MainViewState) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MainViewState, newItem: MainViewState) =
            oldItem == newItem
    }


}