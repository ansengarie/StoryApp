package com.ansengarie.storyapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ansengarie.storyapp.R
import com.ansengarie.storyapp.adapter.StoryAdapter
import com.ansengarie.storyapp.data.paging.StoryPagingViewModel
import com.ansengarie.storyapp.data.paging.StoryPagingViewModelFactory
import com.ansengarie.storyapp.data.response.StoryResponse
import com.ansengarie.storyapp.databinding.ActivityHomeBinding
import com.ansengarie.storyapp.ui.login.dataStore
import com.ansengarie.storyapp.ui.maps.MapsActivity
import com.ansengarie.storyapp.ui.splash.SplashScreenActivity
import com.ansengarie.storyapp.ui.upload.UploadActivity
import com.ansengarie.storyapp.utils.UserPreferences
import com.ansengarie.storyapp.utils.PreferencesViewModel
import com.ansengarie.storyapp.utils.ViewModelFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var prefViewModel: PreferencesViewModel
    private var _homeBinding: ActivityHomeBinding? = null
    private val binding get() = _homeBinding
    private lateinit var listStoryAdapter: StoryAdapter
    private val pagingViewModel: StoryPagingViewModel by viewModels {
        StoryPagingViewModelFactory(this, UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        listStoryAdapter = StoryAdapter()

        setupListData()
        showUserList()
        setupViewModel()
        setupActionButton()
    }

    override fun onResume() {
        super.onResume()
        listStoryAdapter.refresh()
    }

    private fun setupListData() {
        pagingViewModel.getAllStories.observe(this) { listStory ->
            setupStoryData(listStory)
        }
    }

    private fun setupStoryData(item: PagingData<StoryResponse>) {
        listStoryAdapter.submitData(lifecycle, item)
        binding?.rvStory?.adapter = listStoryAdapter
    }

    private fun showUserList() {
        binding?.apply {
            rvStory.layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    private fun setupActionButton() {
        binding?.apply {
            fabAddStory.setOnClickListener {
                startActivity(Intent(this@HomeActivity, UploadActivity::class.java))
            }
        }
    }

    private fun setupViewModel() {
        prefViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[PreferencesViewModel::class.java]

        prefViewModel.getInfo().observe(this) { story ->

            story.token

            prefViewModel.getInfo().observe(this) { story ->
                if (story.isLogin) {
                    Toast.makeText(this, "Mari bercerita, ${story.name} !", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    startActivity(Intent(this, SplashScreenActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _homeBinding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.btn_logout -> {
                prefViewModel.logout()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}