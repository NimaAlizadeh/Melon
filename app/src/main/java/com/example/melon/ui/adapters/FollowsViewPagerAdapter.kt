package com.example.melon.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.melon.databinding.FollowsViewPagerItemBinding
import com.example.melon.databinding.FragmentFollowsBinding
import com.example.melon.models.FollowModel
import javax.inject.Inject

class FollowsViewPagerAdapter: RecyclerView.Adapter<FollowsViewPagerAdapter.ViewPagerViewHolder>()
{
    private lateinit var binding: FollowsViewPagerItemBinding

    private lateinit var context: Context

    @Inject
    lateinit var adapter: NotificationAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        binding = FollowsViewPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewPagerViewHolder()
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bindItems(differ.currentList)
    }


    inner class ViewPagerViewHolder: RecyclerView.ViewHolder(binding.root){
        fun bindItems(list: List<FollowModel>){
            binding.apply {

                loadAdapter(list)

                followsViewPagerItemSearchEdt.addTextChangedListener {
                    if(it!!.isNotEmpty()){

                        val tempList = ArrayList<FollowModel>()
                        list.forEach{ model ->
                            if(model.username.contains(it.toString()))
                                tempList.add(model)
                        }
                        loadAdapter(tempList)
                        followsViewPagerItemNotFoundText.visibility = View.GONE
                    }else{
                        loadAdapter(emptyList())
                        followsViewPagerItemNotFoundText.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    fun loadAdapter(list: List<FollowModel>){
        binding.apply {
            adapter.setData(list)
            followsViewPagerItemRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            followsViewPagerItemRecycler.adapter = adapter
        }
    }


    private val differCallback = object: DiffUtil.ItemCallback<FollowModel>(){
        override fun areItemsTheSame(oldItem: FollowModel, newItem: FollowModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FollowModel, newItem: FollowModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}