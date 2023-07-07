package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melon.R
import com.example.melon.databinding.CommentsRecyclerItemBinding
import com.example.melon.databinding.HomePostsRecyclerItemBinding
import com.example.melon.models.CommentModel
import com.example.melon.models.PostModel
import com.example.melon.utils.Constants
import javax.inject.Inject


class CommentsAdapter @Inject constructor(): RecyclerView.Adapter<CommentsAdapter.CustomViewHolder>() {
    private lateinit var binding: CommentsRecyclerItemBinding
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        binding = CommentsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    inner class CustomViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(comment: CommentModel) {
            binding.apply {
                commentsRecyclerUsername.text = comment.username
                commentsRecyclerCommentText.text = comment.commentText
                commentsRecyclerBuiltTime.text = comment.builtTime
                commentsRecyclerUserImage.load(comment.userImage)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CommentModel>() {
        override fun areItemsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    //on item select handling
    private var onItemClickListener: ((PostModel, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (PostModel, String) -> Unit) {
        onItemClickListener = listener
    }
}