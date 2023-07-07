package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melon.R
import com.example.melon.databinding.HomePostsRecyclerItemBinding
import com.example.melon.models.PostModel
import com.example.melon.utils.Constants
import javax.inject.Inject


class HomePostsAdapter @Inject constructor(): RecyclerView.Adapter<HomePostsAdapter.CustomViewHolder>()
{
    private lateinit var binding: HomePostsRecyclerItemBinding
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = HomePostsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    override fun getItemCount() = differ.currentList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(model: Int)
        {
            binding.apply {
                var isLiked = false

                //load picture in image view with coil
                recyclerItemImageView.load(model){
                    crossfade(true)
                    crossfade(1000)
                    placeholder(ContextCompat.getDrawable(context, R.color.light_grey))
                }

                homeFragmentDescriptionText.setOnClickListener {
                    if(homeFragmentDescriptionText.maxLines == 1)
                        homeFragmentDescriptionText.maxLines = 100
                    else
                        homeFragmentDescriptionText.maxLines = 1
                }

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

                //set on click for user layout to go to user profile
                homeFragmentUserLayout.setOnClickListener {
                    onItemClickListener?.let {
                        it(PostModel(), Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT)
                    }
                }

            }
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    //on item select handling
    private var onItemClickListener: ((PostModel, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (PostModel, String) -> Unit){
        onItemClickListener = listener
    }

//    fun setData(newListData: List<Photo>)
//    {
//        val notesDiffUtils = NotesDiffUtils(dataList, newListData)
//        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
//        dataList = newListData
//        diffUtils.dispatchUpdatesTo(this)
//    }
//
//    class NotesDiffUtils(private val oldItem: List<Photo>, private val newItem: List<Photo>) : DiffUtil.Callback(){
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
}