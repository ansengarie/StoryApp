package com.ansengarie.storyapp.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ansengarie.storyapp.databinding.ActivitySplashScreenBinding
import com.ansengarie.storyapp.ui.home.HomeActivity
import com.ansengarie.storyapp.ui.login.LoginActivity
import com.ansengarie.storyapp.ui.register.RegisterActivity
import com.ansengarie.storyapp.utils.PreferencesViewModel
import com.ansengarie.storyapp.utils.UserPreferences
import com.ansengarie.storyapp.utils.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account_settings")

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var prefViewModel: PreferencesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupViewModel()

        ObjectAnimator.ofFloat(binding.ivSplash, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLoginSplash, View.ALPHA, 1f).setDuration(500)
        val signup =
            ObjectAnimator.ofFloat(binding.btnRegisterSplash, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }

        binding.apply {
            btnLoginSplash.setOnClickListener {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }
            btnRegisterSplash.setOnClickListener {
                startActivity(Intent(this@SplashScreenActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun setupView() {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        prefViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[PreferencesViewModel::class.java]
        prefViewModel.getInfo().observe(this) { user ->
            if (user.isLogin) {
                val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

}