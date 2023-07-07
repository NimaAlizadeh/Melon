package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melon.R
import com.example.melon.databinding.HomePostsPictureItemBinding
import com.example.melon.databinding.ProfilePostsRecyclerItemBinding
import com.example.melon.models.Post
import javax.inject.Inject


class MultiplePicAdapter @Inject constructor(): RecyclerView.Adapter<MultiplePicAdapter.CustomViewHolder>()
{
    private lateinit var binding: HomePostsPictureItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = HomePostsPictureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun bindItems(model: String)
        {
            binding.apply {

                //load picture in image view with coil
                recyclerItemImageView.load(model){
                    crossfade(true)
                    crossfade(1000)
                    placeholder(ContextCompat.getDrawable(context, R.color.light_grey))
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
}