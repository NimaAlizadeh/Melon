package com.example.melon.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.melon.R
import com.example.melon.databinding.ConfirmAddPostItemBinding
import com.example.melon.databinding.HomePostsPictureItemBinding
import com.example.melon.databinding.ProfilePostsRecyclerItemBinding
import com.example.melon.databinding.ShowPostRecyclerItemBinding
import com.example.melon.models.HomePostsResponse
import com.example.melon.models.MultiSelectRecycler
import com.example.melon.models.Post
import com.example.melon.ui.activities.MainActivity
import com.example.melon.ui.addPost.AddPostFragment
import com.example.melon.utils.Constants
import javax.inject.Inject


class ShowPostsItemAdapter @Inject constructor(): RecyclerView.Adapter<ShowPostsItemAdapter.CustomViewHolder>()
{
    private lateinit var binding: ShowPostRecyclerItemBinding
    private lateinit var context: Context
    private lateinit var userName: String
    private lateinit var avatar: String

    private var imagesList = emptyList<String>()

    fun setValues(userName: String, avatar: String){
        this.userName = userName
        this.avatar = avatar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = ShowPostRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.bindItems(imagesList[position])
    }

    override fun getItemCount(): Int = imagesList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(image: String)
        {
            binding.apply {

                //username  initiate
                showPostRecyclerItemUsername.text = userName

                // onclick listener for clicking on username or picture to go to theirProfileFragment
                showPostRecyclerItemUserLayout.setOnClickListener {
                    onItemClickListener?.let {
                        it(avatar, "userNameOnClick")
                    }
                }

                //load picture in image view with glide
                val headers = Headers {
                    val headersMap = HashMap<String, String>()
                    headersMap["auth-token"] = Constants.USER_TOKEN
                    headersMap
                }
                val glideUrl = GlideUrl(Constants.BASE_URL + "post/images/" + image, headers)

                Glide.with(context)
                    .load(glideUrl)
                    .timeout(60000)
                    .into(showPostRecyclerItemImageView)


                //load user profile avatar with glide
                val glideUrl1 = GlideUrl(Constants.BASE_URL + "auth/avatars/" + avatar, headers)
                Glide.with(context)
                    .load(glideUrl1)
                    .placeholder(R.drawable.solid_black)
                    .error(R.drawable.baseline_person_24)
                    .into(binding.showPostRecyclerItemUserImage)
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

    fun setData(newListData: List<String>)
    {
        val notesDiffUtils = NotesDiffUtils(imagesList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        imagesList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<String>, private val newItem: List<String>) : DiffUtil.Callback(){
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
    private var onItemClickListener: ((String, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (String, String) -> Unit){
        onItemClickListener = listener
    }
}