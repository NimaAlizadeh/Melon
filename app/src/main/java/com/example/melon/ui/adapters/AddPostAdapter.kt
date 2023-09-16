package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.melon.databinding.ProfilePostsRecyclerItemBinding
import com.example.melon.models.HomePostsResponse
import com.example.melon.ui.addPost.AddPostFragment
import javax.inject.Inject


class AddPostAdapter @Inject constructor(): PagingDataAdapter<String, AddPostAdapter.CustomViewHolder>(
    differCallback
)
{
    private lateinit var binding: ProfilePostsRecyclerItemBinding
    private lateinit var context: Context
    private lateinit var photoListener: PhotoListener
    val selectedItems = arrayListOf<String>()
    var isMaximum = false

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
        val isSelected = selectedItems.contains(getItem(position)!!)
        holder.bindItems(getItem(position)!!, isSelected)
        holder.setIsRecyclable(false)

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 700
        holder.itemView.startAnimation(anim)
    }

//    override fun getItemCount(): Int = differ.currentList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(image: String, isSelected: Boolean)
        {
            binding.apply {
                if(isSelected)
                {
                    recyclerItemIsSelected.visibility = View.VISIBLE
                    recyclerItemImageView.alpha = 0.5f
                }
                else
                {
                    recyclerItemIsSelected.visibility = View.GONE
                    recyclerItemImageView.alpha = 1f
                }

                recyclerItemProgressbar.visibility = View.GONE


                Glide.with(context).load(image).into(recyclerItemImageView)
//                recyclerItemImageView.load(image)

                recyclerItemImageView.setOnClickListener {
                    if (selectedItems.size < 5)
                        isMaximum = false

                    val isSelectedItem = selectedItems.contains(image)
                    if(isSelectedItem){
                        selectedItems.remove(image)
                        recyclerItemIsSelected.visibility = View.GONE
                        recyclerItemImageView.alpha = 1f
                    }
                    else
                    {
                        if (selectedItems.size < 5) {
                            recyclerItemIsSelected.visibility = View.VISIBLE
                            recyclerItemImageView.alpha = 0.5f
                            selectedItems.add(image)
                        } else {
                            // Show a message to the user indicating the maximum selection limit has been reached
                            Toast.makeText(itemView.context, "Maximum selection limit reached", Toast.LENGTH_SHORT).show()
                        }
                    }

                    photoListener.onPhotoClicked(image, isSelectedItem)
                }

                recyclerItemImageView.setOnLongClickListener {
                    AddPostFragment().isOnMultipleMode = true
                    photoListener.onPhotoLongClicked(image, isSelected)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    companion object{
        private val differCallback = object: DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    interface PhotoListener{
        fun onPhotoClicked(path: String, isSelected: Boolean)
        fun onPhotoLongClicked(path: String, isSelected: Boolean)
    }
}