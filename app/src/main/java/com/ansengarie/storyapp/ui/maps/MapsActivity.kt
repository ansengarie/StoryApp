package com.ansengarie.storyapp.ui.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.ansengarie.storyapp.R
import com.ansengarie.storyapp.databinding.ActivityMapsBinding
import com.ansengarie.storyapp.ui.home.MainViewModel
import com.ansengarie.storyapp.ui.login.dataStore
import com.ansengarie.storyapp.utils.PreferencesViewModel
import com.ansengarie.storyapp.utils.UserPreferences
import com.ansengarie.storyapp.utils.ViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var prefViewModel: PreferencesViewModel
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mainViewModel.listStoryLoc.observe(this) {
            it?.let {
                for (i in it) {
                    val latLng = LatLng(i.lat!!, i.lon!!)
                    mMap.addMarker(MarkerOptions().position(latLng).title(i.name))
                }
            }
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private fun setupViewModel() {
        prefViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[PreferencesViewModel::class.java]

        prefViewModel.getInfo().observe(this) { story ->

            val token = story.token
            mainViewModel.getAllStoriesLoc(token)
        }
    }

}