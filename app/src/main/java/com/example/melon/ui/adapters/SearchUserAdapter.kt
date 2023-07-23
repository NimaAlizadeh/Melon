package com.example.melon.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.melon.R
import com.example.melon.databinding.CommentsRecyclerItemBinding
import com.example.melon.databinding.HomePostsRecyclerItemBinding
import com.example.melon.databinding.SearchUserRecyclerItemBinding
import com.example.melon.models.CommentModel
import com.example.melon.models.Post
import com.example.melon.models.PostModel
import com.example.melon.models.UserX
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import javax.inject.Inject


class SearchUserAdapter @Inject constructor(): RecyclerView.Adapter<SearchUserAdapter.CustomViewHolder>() {
    private lateinit var binding: SearchUserRecyclerItemBinding
    lateinit var context: Context

    private var postsList = emptyList<UserX>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        binding = SearchUserRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(postsList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = postsList.size

    inner class CustomViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(userItem: UserX) {
            binding.apply {
//                if(adapterPosition == differ.currentList.size - 1)
//                    searchUserRecyclerSeparator.visibility = View.INVISIBLE

                loadUserProfileAvatar(userItem.id)

                searchUserRecyclerUserName.text = userItem.username

                if(MainActivity.followingIdList.contains(userItem.id))
                    searchUserRecyclerIsFollowing.text = "Following"
                else
                    searchUserRecyclerIsFollowing.text = "Not Following"

                searchUserRecyclerItemWholeLayout.setOnClickListener {
                    onItemClickListener?.let {
                        it(userItem, Constants.SEARCH_FRAGMENT_GO_TO_PROFILE_FRAGMENT)
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<UserX>() {
        override fun areItemsTheSame(oldItem: UserX, newItem: UserX): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserX, newItem: UserX): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun setData(newListData: List<UserX>)
    {
        val notesDiffUtils = NotesDiffUtils(postsList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        postsList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<UserX>, private val newItem: List<UserX>) : DiffUtil.Callback(){
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








//    //on item select handling
    private var onItemClickListener: ((UserX, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (UserX, String) -> Unit) {
        onItemClickListener = listener
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
            .into(binding.searchUserRecyclerImageView)
    }
}