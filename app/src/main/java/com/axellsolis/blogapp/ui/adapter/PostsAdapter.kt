package com.axellsolis.blogapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.axellsolis.blogapp.R
import com.axellsolis.blogapp.data.Post
import com.axellsolis.blogapp.databinding.PostItemBinding
import com.axellsolis.blogapp.utils.TimeUtils
import com.axellsolis.blogapp.utils.hide
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PostsAdapter(private val onClickItem: (Post) -> Unit) :
    ListAdapter<Post, PostsAdapter.PostViewHolder>(DiffCallback) {

    class PostViewHolder(
        private val binding: PostItemBinding,
        private val context: Context,
        private val onClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }

        fun bind(post: Post) {
            binding.apply {
                tvUserName.text = post.profileName
                tvTimeStamp.text = getTimeAgo(post)
                if (post.postDescription.isEmpty()) {
                    tvDescription.hide()
                } else {
                    tvDescription.text = post.postDescription
                }

                Glide.with(context)
                    .load(post.profilePicture)
                    .circleCrop()
                    .placeholder(R.drawable.loading_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfileThumbnail)

                Glide.with(context)
                    .load(post.postImage)
                    .centerCrop()
                    .placeholder(R.drawable.loading_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPostPicture)
            }
        }

        private fun getTimeAgo(post: Post): String? {
            return post.createdAt?.time?.div(1000L)?.let {
                TimeUtils.getTimeAgo(it.toInt())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(
            PostItemBinding.inflate(inflater, parent, false),
            parent.context
        ) { position ->
            onClickItem(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = getItem(position)
        holder.bind(currentPost)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.profileName == newItem.profileName
        }
    }

}