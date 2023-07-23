package com.example.melon.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
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
import com.example.melon.databinding.ProfilePostsRecyclerItemBinding
import com.example.melon.models.Post
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import okhttp3.MultipartBody
import retrofit2.http.Url
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection


class ProfilePostsAdapter @Inject constructor(): RecyclerView.Adapter<ProfilePostsAdapter.CustomViewHolder>()
{
    private lateinit var binding: ProfilePostsRecyclerItemBinding
    private lateinit var context: Context
    private var size = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = ProfilePostsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    override fun getItemCount(): Int {
        size = differ.currentList.size
        return size
    }

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        fun bindItems(model: Post)
        {
            binding.apply {

                //load with glide
                val headers = Headers {
                    val headersMap = HashMap<String, String>()
                    headersMap["auth-token"] = Constants.USER_TOKEN
                    headersMap
                }
                val glideUrl = GlideUrl(Constants.BASE_URL + "post/images/" + model.images[0], headers)

                Glide.with(context)
                    .load(glideUrl)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            Log.d("TAG", "onLoadFailed: ")
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            recyclerItemProgressbar.visibility = View.GONE
                            return false
                        }

                    })
                    .into(recyclerItemImageView)


                recyclerItemIsMultiple.visibility = View.GONE


                //show multiple icon or not
                if(model.images.size > 1)
                    recyclerItemIsMultiple.visibility = View.VISIBLE
                else
                    recyclerItemIsMultiple.visibility = View.GONE
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
}