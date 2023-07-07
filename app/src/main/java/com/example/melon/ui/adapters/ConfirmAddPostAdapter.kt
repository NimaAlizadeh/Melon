package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melon.databinding.ConfirmAddPostItemBinding
import com.example.melon.databinding.HomePostsPictureItemBinding
import com.example.melon.databinding.ProfilePostsRecyclerItemBinding
import com.example.melon.models.MultiSelectRecycler
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.addPost.AddPostFragment
import javax.inject.Inject


class ConfirmAddPostAdapter @Inject constructor(): RecyclerView.Adapter<ConfirmAddPostAdapter.CustomViewHolder>()
{
    private lateinit var binding: ConfirmAddPostItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = ConfirmAddPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.bindItems(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(image: String)
        {
            binding.apply {
                Glide.with(context).load(image).into(confirmAddPostFragmentImageViewItem)
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
}