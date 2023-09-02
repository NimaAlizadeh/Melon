package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.example.melon.R
import com.example.melon.databinding.CommentsRecyclerItemBinding
import com.example.melon.models.CommentModel
import com.example.melon.models.PostModel
import com.example.melon.utils.Constants
import javax.inject.Inject


class CommentsAdapter @Inject constructor(): RecyclerView.Adapter<CommentsAdapter.CustomViewHolder>() {
    private lateinit var binding: CommentsRecyclerItemBinding
    lateinit var context: Context

    private var commentList = emptyList<CommentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        binding = CommentsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(commentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = commentList.size

    inner class CustomViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(comment: CommentModel) {
            binding.apply {
                commentsRecyclerUsername.text = comment.username
                commentsRecyclerCommentText.text = comment.comment
                commentsRecyclerBuiltTime.text = comment.time
                loadUserProfileAvatar(comment.user_id)
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


    fun setData(newListData: List<CommentModel>)
    {
        val notesDiffUtils = NotesDiffUtils(commentList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        commentList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<CommentModel>, private val newItem: List<CommentModel>) : DiffUtil.Callback(){
        override fun getOldListSize() = oldItem.size

        override fun getNewListSize() = newItem.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }

    private fun loadUserProfileAvatar(userId: String){
        //load user profile avatar with glide
        val headers = Headers {
            val headersMap = HashMap<String, String>()
            headersMap["auth-token"] = Constants.USER_TOKEN
            headersMap
        }
        val glideUrl = GlideUrl(Constants.BASE_URL + "auth/avatars/" + userId, headers)

        Glide.with(context)
            .load(glideUrl)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                    binding.searchUserRecyclerImageProgressbar.visibility = View.GONE
//                    return false
//                }
//
//                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                    binding.searchUserRecyclerImageProgressbar.visibility = View.GONE
//                    return false
//                }
//
//            })
            .placeholder(R.drawable.solid_black)
            .error(R.drawable.baseline_person_24)
//            .skipMemoryCache(true)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.commentsRecyclerUserImage)
    }

    //on item select handling
    private var onItemClickListener: ((PostModel, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (PostModel, String) -> Unit) {
        onItemClickListener = listener
    }
}