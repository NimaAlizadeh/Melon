package com.example.melon.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.melon.R
import com.example.melon.databinding.HomePostsRecyclerItemBinding
import com.example.melon.databinding.ProfilePostsRecyclerItemBinding
import com.example.melon.models.HomePostsResponse
import com.example.melon.models.Post
import com.example.melon.models.PostModel
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import okhttp3.internal.notifyAll
import javax.inject.Inject


class SearchPostsAdapter @Inject constructor(): PagingDataAdapter<HomePostsResponse.Post, SearchPostsAdapter.CustomViewHolder>(
    differCallback)
{
    private lateinit var binding: ProfilePostsRecyclerItemBinding
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = ProfilePostsRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

                //load with glide
                val headers = Headers {
                    val headersMap = HashMap<String, String>()
                    headersMap["auth-token"] = Constants.USER_TOKEN
                    headersMap
                }
                val glideUrl = GlideUrl(Constants.BASE_URL + "post/images/" + model.images[0], headers)

                Glide.with(context)
                    .load(glideUrl)
                    .listener(object : RequestListener<Drawable> {
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


                cardView.setOnClickListener {
                    onItemClickListener?.let {
                        it(bindingAdapterPosition, "onClick")
                    }
                }
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


    //on item select handling
    private var onItemClickListener: ((Int, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (Int, String) -> Unit){
        onItemClickListener = listener
    }
}