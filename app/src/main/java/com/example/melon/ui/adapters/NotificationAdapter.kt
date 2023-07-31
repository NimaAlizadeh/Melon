package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.example.melon.R
import com.example.melon.databinding.NotificationRecyclerItemBinding
import com.example.melon.models.FollowModel
import com.example.melon.models.UserX
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import javax.inject.Inject


class NotificationAdapter @Inject constructor(): RecyclerView.Adapter<NotificationAdapter.CustomViewHolder>() {
    private lateinit var binding: NotificationRecyclerItemBinding
    lateinit var context: Context

    private var notificationList = emptyList<FollowModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        binding = NotificationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size

    inner class CustomViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(model: FollowModel) {
            binding.apply {

                if(MainActivity.appPagePosition == Constants.FRAGMENT_FOLLOWS){
                    notificationRecyclerButtonConfirm.text = "Confirm"
                    notificationRecyclerButtonDelete.visibility = View.GONE

                    if(model.name.isEmpty()){
                        notificationRecyclerTextRequest.visibility = View.GONE
                    }else{
                        notificationRecyclerTextRequest.visibility = View.VISIBLE
                        notificationRecyclerTextRequest.text = model.name
                    }

                }else{
                    notificationRecyclerButtonConfirm.text = "Follow"
                    notificationRecyclerButtonDelete.visibility = View.VISIBLE

                    notificationRecyclerTextRequest.visibility = View.VISIBLE
                    notificationRecyclerTextRequest.text = "requested to follow you"
                }

                notificationRecyclerUserName.text = model.username
                loadUserProfileAvatar(model.id)


                notificationRecyclerButtonConfirm.setOnClickListener {
                    onItemClickListener?.let {
                        it(model, "confirm")
                    }
                }

                notificationRecyclerButtonDelete.setOnClickListener {
                    onItemClickListener?.let {
                        it(model, "delete")
                    }
                }

            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<FollowModel>() {
        override fun areItemsTheSame(oldItem: FollowModel, newItem: FollowModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FollowModel, newItem: FollowModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun setData(newListData: List<FollowModel>)
    {
        val notesDiffUtils = NotesDiffUtils(notificationList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        notificationList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<FollowModel>, private val newItem: List<FollowModel>) : DiffUtil.Callback(){
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
    private var onItemClickListener: ((FollowModel, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (FollowModel, String) -> Unit) {
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
            .into(binding.notificationRecyclerImageView)
    }
}