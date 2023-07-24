package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melon.databinding.ProfilePostsRecyclerItemBinding
import com.example.melon.ui.addPost.AddPostFragment
import javax.inject.Inject


class ChangeAvatarAdapter @Inject constructor(): RecyclerView.Adapter<ChangeAvatarAdapter.CustomViewHolder>()
{
    private lateinit var binding: ProfilePostsRecyclerItemBinding
    private lateinit var context: Context
    private lateinit var photoListener: PhotoListener

    fun setPhotoListener(photoListener: PhotoListener) {
        this.photoListener = photoListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = ProfilePostsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.bindItems(differ.currentList[position])
        holder.setIsRecyclable(false)

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 700
        holder.itemView.startAnimation(anim)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(image: String)
        {
            binding.apply {

                recyclerItemProgressbar.visibility = View.GONE


                Glide.with(context).load(image).into(recyclerItemImageView)

                recyclerItemImageView.setOnClickListener {
                    photoListener.onPhotoClicked(image)
                }
            }
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface PhotoListener{
        fun onPhotoClicked(path: String)
    }
}