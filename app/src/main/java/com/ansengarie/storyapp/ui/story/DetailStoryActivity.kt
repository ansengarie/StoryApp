package com.ansengarie.storyapp.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ansengarie.storyapp.R
import com.ansengarie.storyapp.data.response.StoryResponse
import com.ansengarie.storyapp.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private var _detailStoryBinding: ActivityDetailStoryBinding? = null
    private val binding get() = _detailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()

        val detailStory = intent.getParcelableExtra<StoryResponse>(EXTRA_DETAIL) as StoryResponse

        binding?.apply {

            Glide.with(this@DetailStoryActivity)
                .load(detailStory.photoUrl)
                .into(ivImgDetail)
            tvNameDetail.text = detailStory.name
            tvDeskripsiDetail.text = detailStory.description
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _detailStoryBinding = null
    }

    private fun setupView() {
        _detailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.apply {
            title = getString(R.string.title_detail_activity)
        }
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

}