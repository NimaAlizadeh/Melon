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
import com.example.melon.models.*
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import javax.inject.Inject


class SearchUserAdapter @Inject constructor(): RecyclerView.Adapter<SearchUserAdapter.CustomViewHolder>() {
    private lateinit var binding: SearchUserRecyclerItemBinding
    lateinit var context: Context

    private var postsList = emptyList<User>()

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
        fun bindItems(userItem: User) {
            binding.apply {

                loadUserProfileAvatar(userItem.id)

                searchUserRecyclerUserName.text = userItem.username

                if(MainActivity.followingsIdList.contains(userItem.id)){
                    searchUserRecyclerIsFollowing.setTextColor(ContextCompat.getColor(context, R.color.light_grey))
                    searchUserRecyclerIsFollowing.text = "Following"
                }
                else if(MainActivity.followingsRequestedIdList.contains(userItem.id)){
                    searchUserRecyclerIsFollowing.setTextColor(ContextCompat.getColor(context, R.color.light_grey))
                    searchUserRecyclerIsFollowing.text = "Requested"
                }
                else
                {
                    searchUserRecyclerIsFollowing.setTextColor(ContextCompat.getColor(context, R.color.blue_purple))
                    searchUserRecyclerIsFollowing.text = "Not Followed"
                }

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

    fun setData(newListData: List<User>)
    {
        val notesDiffUtils = NotesDiffUtils(postsList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        postsList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<User>, private val newItem: List<User>) : DiffUtil.Callback(){
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
    private var onItemClickListener: ((User, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (User, String) -> Unit) {
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