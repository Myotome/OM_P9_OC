package com.openclassrooms.realestatemanager.activity.main.detail

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ImageListContentBinding
import com.openclassrooms.realestatemanager.model.Photo

class DetailAdapter(private val listener: (Photo) -> Unit) :
    ListAdapter<Photo, DetailAdapter.DetailViewHolder>(DetailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DetailViewHolder(
        ImageListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DetailAdapter.DetailViewHolder, position: Int) =
        holder.bind(getItem(position), listener)


    class DetailViewHolder(private val binding: ImageListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo, listener: (Photo) -> Unit) {

//            Glide.with(binding.ivContentList)
//                .load(photo.location.path)
//                .fitCenter()
//                .into(binding.ivContentList)

            binding.ivContentList.load(Uri.parse(photo.image))
            binding.tvContentList.text = photo.name

        }
    }

    private class DetailDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.name == newItem.name
    }

}