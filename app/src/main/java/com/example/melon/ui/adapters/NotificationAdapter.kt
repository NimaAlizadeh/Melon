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
import com.example.melon.models.Follow
import com.example.melon.models.FollowModel
import com.example.melon.models.NotificationResponse
import com.example.melon.utils.Constants
import javax.inject.Inject


class NotificationAdapter @Inject constructor(): RecyclerView.Adapter<NotificationAdapter.CustomViewHolder>() {
    private lateinit var binding: NotificationRecyclerItemBinding
    lateinit var context: Context

    private var notificationList = emptyList<NotificationResponse.Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        binding = NotificationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(notificationList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = notificationList.size

    inner class CustomViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(model: NotificationResponse.Notification) {
            binding.apply {

                notificationRecyclerActionIcon.visibility = View.VISIBLE
                notificationRecyclerButtonConfirm.visibility = View.GONE
                notificationRecyclerButtonDelete.visibility = View.GONE


                notificationRecyclerUserName.text = model.performer.username
                notificationRecyclerTextRequest.text = model.message
                loadUserProfileAvatar(model.performer._id)

                when(model.action){
                    "LIKED_POST" -> {
                        notificationRecyclerActionIcon.setImageResource(R.drawable.baseline_favorite_border_24)
                    }
                    "COMMENTED_ON_A_POST" -> {
                        notificationRecyclerActionIcon.setImageResource(R.drawable.bubble_chat)
                    }
                    "ADD_FOLLOWER" -> {
                        notificationRecyclerActionIcon.setImageResource(R.drawable.baseline_person_add_alt_1_24)
                    }
                    "REQUEST_ACCEPTANCE" -> {
                        notificationRecyclerActionIcon.setImageResource(R.drawable.baseline_check_24)
                    }
                }


                notificationRecyclerWholeLayout.setOnClickListener {
                    onItemClickListener?.let {
                        it(model, Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT)
                    }
                }

            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Follow>() {
        override fun areItemsTheSame(oldItem: Follow, newItem: Follow): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Follow, newItem: Follow): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun setData(newListData: List<NotificationResponse.Notification>)
    {
        val notesDiffUtils = NotesDiffUtils(notificationList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        notificationList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<NotificationResponse.Notification>, private val newItem: List<NotificationResponse.Notification>) : DiffUtil.Callback(){
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
    private var onItemClickListener: ((NotificationResponse.Notification, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (NotificationResponse.Notification, String) -> Unit) {
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

            .placeholder(R.drawable.solid_black)
            .error(R.drawable.baseline_person_24)
            .into(binding.notificationRecyclerImageView)
    }
}