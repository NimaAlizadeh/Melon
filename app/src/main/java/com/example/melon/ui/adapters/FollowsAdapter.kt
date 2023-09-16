package com.example.melon.ui.adapters

import android.content.Context
import android.util.Log
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
import com.example.melon.ui.activities.MainActivity
import com.example.melon.utils.Constants
import javax.inject.Inject


class FollowsAdapter @Inject constructor(): RecyclerView.Adapter<FollowsAdapter.CustomViewHolder>() {
    private lateinit var binding: NotificationRecyclerItemBinding
    lateinit var context: Context

    private var followsList: ArrayList<FollowModel> = ArrayList()
    private var page = ""

    fun setPage(page: String){
        this.page = page
    }

    fun removeAt(position: Int, element: FollowModel) {
        followsList.remove(element)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, followsList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        binding = NotificationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(followsList[position])
//        holder.setIsRecyclable(false)

    }

    override fun getItemCount() = followsList.size

    inner class CustomViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(model: FollowModel) {
            binding.apply {

                notificationRecyclerUserName.text = model.username
                loadUserProfileAvatar(model.id)

                initViews(model)


                notificationRecyclerWholeLayout.setOnClickListener {
                    onItemClickListener?.let {
                        it(bindingAdapterPosition, model, Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT)
                    }
                }

                notificationRecyclerButtonConfirm.setOnClickListener {
                    onItemClickListener?.let {

                        when(notificationRecyclerButtonConfirm.text){
                            "Follow" -> {
                                it(bindingAdapterPosition, model, "Follow")
                            }

                            "Requested" -> {
                                it(bindingAdapterPosition, model, "Requested")
                            }
                        }
                    }
                }

                notificationRecyclerButtonDelete.setOnClickListener {
                    onItemClickListener?.let {

                        when(notificationRecyclerButtonDelete.text){
                            "Remove" -> {
                                it(bindingAdapterPosition, model, "Remove")
                            }

                            "Following" -> {
                                it(bindingAdapterPosition, model, "Following")
                            }
                        }

                    }
                }

            }
        }
    }

    private fun initViews(model: FollowModel){

        binding.apply {

            if(model.name == null){
                notificationRecyclerTextRequest.visibility = View.GONE
            }else{
                if(model.name.isEmpty()){
                    notificationRecyclerTextRequest.visibility = View.GONE
                }else{
                    notificationRecyclerTextRequest.visibility = View.VISIBLE
                    notificationRecyclerTextRequest.text = model.name
                }
            }

            if(MainActivity.profilePosition == Constants.GO_TO_MY_USER_PROFILE_FRAGMENT){

                if(page == Constants.FRAGMENT_FOLLOWERS){

                    //showing follow and remove button for my profile user

                    notificationRecyclerButtonDelete.visibility = View.VISIBLE
                    notificationRecyclerButtonDelete.text = "Remove"


                    if(MainActivity.followingsIdList.contains(model.id)){
                        notificationRecyclerButtonConfirm.visibility = View.GONE
                    }else{
                        if(MainActivity.followingsRequestedIdList.contains(model.id)){
                            notificationRecyclerButtonConfirm.text = "Requested"
                        }else{
                            notificationRecyclerButtonConfirm.text = "Follow"
                        }

                        notificationRecyclerButtonConfirm.visibility = View.VISIBLE
                    }

                } else if (page == Constants.FRAGMENT_FOLLOWINGS){

                    //showing following button that user can click on it and unfollow the user

                    if(MainActivity.followingsIdList.contains(model.id)){
                        notificationRecyclerButtonDelete.visibility = View.VISIBLE
                        notificationRecyclerButtonDelete.text = "Following"
                        notificationRecyclerButtonConfirm.visibility = View.GONE
                    }else{
                        notificationRecyclerButtonConfirm.text = "Follow"
                        notificationRecyclerButtonConfirm.visibility = View.VISIBLE
                        notificationRecyclerButtonDelete.visibility = View.GONE
                    }
                }

            }else if(MainActivity.profilePosition == Constants.GO_TO_THEIR_USER_PROFILE_FRAGMENT){

                // if it's following then we are going to let the user unfollow them
                if(MainActivity.followingsIdList.contains(model.id)){
                    notificationRecyclerButtonConfirm.visibility = View.GONE
                    notificationRecyclerButtonDelete.visibility = View.VISIBLE
                    notificationRecyclerButtonDelete.text = "Following"
                }
                else{
                    notificationRecyclerButtonDelete.visibility = View.GONE

                    if(MainActivity.followingsRequestedIdList.contains(model.id)){
                        notificationRecyclerButtonConfirm.text = "Requested"
                        notificationRecyclerButtonConfirm.visibility = View.VISIBLE
                    }else{
                        notificationRecyclerButtonConfirm.text = "Follow"
                        notificationRecyclerButtonConfirm.visibility = View.VISIBLE
                    }
                }

            }

            if(model.id == MainActivity.myUserID){
                notificationRecyclerButtonConfirm.visibility = View.GONE
                notificationRecyclerButtonDelete.visibility = View.GONE
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

    fun setData(newListData: ArrayList<FollowModel>)
    {
        val notesDiffUtils = NotesDiffUtils(followsList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        followsList = newListData
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
    private var onItemClickListener: ((Int ,FollowModel, String) -> Unit)? = null

    fun setOnItemCLickListener(listener: (Int, FollowModel, String) -> Unit) {
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