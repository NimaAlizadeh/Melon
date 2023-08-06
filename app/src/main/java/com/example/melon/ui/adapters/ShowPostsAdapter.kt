package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.example.melon.R
import com.example.melon.databinding.HomePostsRecyclerItemBinding
import com.example.melon.models.Post
import com.example.melon.models.PostModel
import com.example.melon.utils.Constants
import okhttp3.internal.notifyAll
import javax.inject.Inject


class ShowPostsAdapter @Inject constructor(): RecyclerView.Adapter<ShowPostsAdapter.CustomViewHolder>()
{
    private lateinit var binding: HomePostsRecyclerItemBinding
    lateinit var context: Context
    lateinit var userName: String
    lateinit var userProfileAvatar: String

    private var postsList = emptyList<Post>()

    fun setValues(userName: String, userProfileAvatar: String){
        this.userName = userName
        this.userProfileAvatar = userProfileAvatar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = HomePostsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.bindItems(postsList[position])

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        holder.itemView.startAnimation(anim)
    }

    override fun getItemCount() = postsList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(model: Post)
        {
            binding.apply {
                var isLiked = false

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


                // do what you want in this when user clicks on like button
                // use interface to do that
                homeFragmentPostLikeButton.setOnClickListener {
                    if(isLiked){
                        isLiked = false
                        homeFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_border_24))
                    }else{
                        isLiked = true
                        homeFragmentPostLikeImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24))
                    }
                }

                homeFragmentPostCommentButton.setOnClickListener {
                    onItemClickListener?.let {
                        it(PostModel(), Constants.GO_TO_COMMENTS_FRAGMENT)
                    }
                }

                homeFragmentPostLikeButton.setOnClickListener {
                    onItemClickListener?.let {
                        it(PostModel(), Constants.DO_LIKE_BUTTON)
                    }
                }

                //indicator for recycler
                val pagerSnapHelper = PagerSnapHelper()
                pagerSnapHelper.attachToRecyclerView(homeFragmentPicsRecycler)
                homeFragmentPicsIndicator.attachToRecyclerView(homeFragmentPicsRecycler, pagerSnapHelper)

                val adapter = ShowPostsItemAdapter()
                adapter.setValues(userName, userProfileAvatar)
                adapter.setData(model.images)
                homeFragmentPicsRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                homeFragmentPicsRecycler.adapter = adapter

            }
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun setData(newListData: List<Post>)
    {
        val notesDiffUtils = NotesDiffUtils(postsList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        postsList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<Post>, private val newItem: List<Post>) : DiffUtil.Callback(){
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

    //on item select handling
    private var onItemClickListener: ((PostModel, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (PostModel, String) -> Unit){
        onItemClickListener = listener
    }
}