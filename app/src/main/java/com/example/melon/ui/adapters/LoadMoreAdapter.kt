package com.example.melon.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melon.databinding.LoadMoreBinding

class LoadMoreAdapter (private val retry: () -> Unit): LoadStateAdapter<LoadMoreAdapter.CustomViewHolder>()
{
    lateinit var binding: LoadMoreBinding

    override fun onBindViewHolder(holder: CustomViewHolder, loadState: LoadState) {
        holder.bindData(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CustomViewHolder {
        binding = LoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(retry)
    }


    inner class CustomViewHolder (retry: () -> Unit) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.loadMoreRetryButton.setOnClickListener {
                retry()
            }
        }

        fun bindData(state: LoadState){
            binding.apply {
                loadMoreProgressbar.isVisible = state is LoadState.Loading
                loadMoreErrorText.isVisible = state is LoadState.Error
                loadMoreRetryButton.isVisible = state is LoadState.Error
            }
        }

    }
}