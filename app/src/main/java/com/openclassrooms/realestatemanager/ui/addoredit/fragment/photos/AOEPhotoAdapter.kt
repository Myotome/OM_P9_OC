package com.openclassrooms.realestatemanager.ui.addoredit.fragment.photos

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.openclassrooms.realestatemanager.databinding.ImageListContentBinding
import com.openclassrooms.realestatemanager.model.Photo

class AOEPhotoAdapter(private val listener: (Photo) -> Unit) :
    ListAdapter<Photo, AOEPhotoAdapter.AddPhotoViewHolder>(AddPhotoCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddPhotoViewHolder(
        ImageListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: AddPhotoViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class AddPhotoViewHolder(private val binding: ImageListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(photo: Photo, listener: (Photo) -> Unit) {

            binding.ivContentList.load(Uri.parse(photo.storageUriString?:photo.image))

            binding.tvContentList.text = photo.name

            itemView.setOnClickListener { listener(photo) }
        }

    }

    private class AddPhotoCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.name == newItem.name
    }
}