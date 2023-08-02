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
import com.example.melon.models.MultiSelectRecycler
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
        holder.bindItems(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(image: String)
        {
            binding.apply {

                //username  initiate
                showPostRecyclerItemUsername.text = userName

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
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            Log.d("TAG", "onLoadFailed: ")
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                            showPostRecyclerItemProgressbar.visibility = View.GONE
                            return false
                        }

                    })
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
}