package com.ansengarie.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ansengarie.storyapp.data.response.StoryResponse
import com.ansengarie.storyapp.databinding.ItemRowStoryBinding
import com.ansengarie.storyapp.ui.story.DetailStoryActivity
import com.bumptech.glide.Glide

class StoryAdapter : PagingDataAdapter<StoryResponse, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StoryAdapter.ListViewHolder {
        return ListViewHolder(
            ItemRowStoryBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(hold: StoryAdapter.ListViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            hold.bind(user)
        }
    }

    inner class ListViewHolder(private var binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryResponse) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.photoUrl)
                    .into(ivImage)
                tvName.text = item.name

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_DETAIL, item)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivImage, "image"),
                            Pair(tvName, "name"),
                        )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())

                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse>() {
            override fun areItemsTheSame(oldItem: StoryResponse, newItem: StoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryResponse,
                newItem: StoryResponse
            ): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.photoUrl == newItem.photoUrl &&
                        oldItem.id == newItem.id &&
                        oldItem.createdAt == newItem.createdAt &&
                        oldItem.description == newItem.description
            }
        }
    }
}