package com.openclassrooms.realestatemanager.ui.main.detail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ImageListContentBinding
import com.openclassrooms.realestatemanager.model.Photo

class DetailAdapter :
    ListAdapter<Photo, DetailAdapter.DetailViewHolder>(DetailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DetailViewHolder(
        ImageListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) =
        holder.bind(getItem(position))


    class DetailViewHolder(private val binding: ImageListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {

            photo.storageUriString?.let {binding.ivContentList.load( Uri.parse(it))}
                ?:photo.image?.let{ binding.ivContentList.load(Uri.parse(it))}
                ?:binding.ivContentList.load(R.drawable.ic_baseline_image_not_supported_24)
            binding.tvContentList.text = photo.name

        }
    }

    private class DetailDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.name == newItem.name
    }

}