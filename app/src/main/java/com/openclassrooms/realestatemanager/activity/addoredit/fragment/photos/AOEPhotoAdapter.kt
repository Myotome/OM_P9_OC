package com.openclassrooms.realestatemanager.activity.addoredit.fragment.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.ImageListContentBinding
import com.openclassrooms.realestatemanager.model.Photo

class AOEPhotoAdapter(private val listener: (Photo) -> Unit) :
    ListAdapter<Photo, AOEPhotoAdapter.AddPhotoViewHolder>(AddPhotoCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddPhotoViewHolder(
        ImageListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: AddPhotoViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class AddPhotoViewHolder(private val binding: ImageListContentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo : Photo, listener: (Photo) -> Unit) {
            Glide.with(binding.root)
                .load(photo.location)
                .fitCenter()
                .into(binding.ivContentList)

            binding.tvContentList.text = photo.name
        }

    }

    private class AddPhotoCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.name == newItem.name
    }
}