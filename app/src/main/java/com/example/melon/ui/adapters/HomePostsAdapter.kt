package com.example.melon.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.example.melon.R
import com.example.melon.databinding.HomePostsRecyclerItemBinding
import com.example.melon.models.HomePostsResponse
import com.example.melon.models.Post
import com.example.melon.models.PostModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import okhttp3.internal.notifyAll
import javax.inject.Inject


class HomePostsAdapter @Inject constructor(): PagingDataAdapter<HomePostsResponse.Post, HomePostsAdapter.CustomViewHolder>(
    differCallback)
{
    private lateinit var binding: HomePostsRecyclerItemBinding
    lateinit var context: Context
//    lateinit var userName: String
//    lateinit var userProfileAvatar: String

//    private var postsList = emptyList<HomePostsResponse.Post>()

//    fun setValues(userName: String, userProfileAvatar: String){
//        this.userName = userName
//        this.userProfileAvatar = userProfileAvatar
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = HomePostsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.bindItems(getItem(position)!!)
        holder.setIsRecyclable(false)

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        holder.itemView.startAnimation(anim)
    }

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SetTextI18n")
        fun bindItems(model: HomePostsResponse.Post)
        {
            binding.apply {

                homeFragmentTimeText.text = model.time

                var isLiked = if(model.likes.contains(MainActivity.myUserID)){
                    homeFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24))
                    true
                } else {
                    homeFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_border_24))
                    false
                }

                homeFragmentLikesText.text = model.likes.size.toString()

                if(model.images.size <= 1)
                    homeFragmentPicsIndicator.visibility = View.GONE
                else
                    homeFragmentPicsIndicator.visibility = View.VISIBLE

                //description
                if(model.description.isNotEmpty()){
                    homeFragmentDescriptionText.visibility = View.VISIBLE
                    homeFragmentDescriptionText.text = model.description
                }else{
                    homeFragmentDescriptionText.visibility = View.GONE
                }

                // description settings to open it and maybe do some animation for it
                homeFragmentDescriptionText.setOnClickListener {
                    if(homeFragmentDescriptionText.maxLines == 2)
                        homeFragmentDescriptionText.maxLines = 1000
                }


                homeFragmentPostCommentButton.setOnClickListener {
                    onItemClickListener?.let {
                        getItem(bindingAdapterPosition)?.let { it1 -> it(it1, Constants.GO_TO_COMMENTS_FRAGMENT) }
                    }
                }

                homeFragmentPostLikeButton.setOnClickListener {
                    onItemClickListener?.let {
                        getItem(bindingAdapterPosition)?.let { it1 -> it(it1, Constants.DO_LIKE_BUTTON) }
                    }

                    if(isLiked){
                        isLiked = false
                        homeFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_border_24))
                        homeFragmentLikesText.text = ((homeFragmentLikesText.text).toString().toInt() - 1).toString()
                    }
                    else{
                        isLiked = true
                        homeFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24))
                        homeFragmentLikesText.text = ((homeFragmentLikesText.text).toString().toInt() + 1).toString()
                    }
                }

                //indicator for recycler
                val pagerSnapHelper = PagerSnapHelper()
                pagerSnapHelper.attachToRecyclerView(homeFragmentPicsRecycler)
                homeFragmentPicsIndicator.attachToRecyclerView(homeFragmentPicsRecycler, pagerSnapHelper)

                val adapter = ShowPostsItemAdapter()
                adapter.setValues(model.username, model.user)
                adapter.setData(model.images)
                homeFragmentPicsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                homeFragmentPicsRecycler.adapter = adapter

            }
        }
    }

    companion object{
        private val differCallback = object: DiffUtil.ItemCallback<HomePostsResponse.Post>(){
            override fun areItemsTheSame(oldItem: HomePostsResponse.Post, newItem: HomePostsResponse.Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HomePostsResponse.Post, newItem: HomePostsResponse.Post): Boolean {
                return oldItem == newItem
            }
        }
    }

//    val differ = AsyncListDiffer(this, differCallback)

//    fun setData(newListData: List<HomePostsResponse.Post>)
//    {
//        val notesDiffUtils = NotesDiffUtils(postsList, newListData)
//        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
//        postsList = newListData
//        diffUtils.dispatchUpdatesTo(this)
//    }
//
//    class NotesDiffUtils(private val oldItem: List<Post>, private val newItem: List<Post>) : DiffUtil.Callback(){
//        override fun getOldListSize() = oldItem.size
//
//        override fun getNewListSize() = newItem.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
//        {
//            return oldItem[oldItemPosition] === newItem[newItemPosition]
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
//        {
//            return oldItem[oldItemPosition] === newItem[newItemPosition]
//        }
//    }


    //on item select handling
    private var onItemClickListener: ((HomePostsResponse.Post, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (HomePostsResponse.Post, String) -> Unit){
        onItemClickListener = listener
    }
}