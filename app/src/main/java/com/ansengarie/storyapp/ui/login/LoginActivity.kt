package com.ansengarie.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ansengarie.storyapp.data.model.UserModel
import com.ansengarie.storyapp.databinding.ActivityLoginBinding
import com.ansengarie.storyapp.ui.home.HomeActivity
import com.ansengarie.storyapp.ui.home.MainViewModel
import com.ansengarie.storyapp.utils.UserPreferences
import com.ansengarie.storyapp.utils.PreferencesViewModel
import com.ansengarie.storyapp.utils.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account_settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var prefViewModel: PreferencesViewModel
    private var islog: ActivityLoginBinding? = null
    private val binding get() = islog
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        islog = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupViewModel()

        binding?.apply {
            mainViewModel.isLoading.observe(this@LoginActivity) {
                showLoading(it)
            }

            btnLogin.setOnClickListener {
                val email = tfEmailLogin.text.toString()
                val password = tfPasswordLogin.text.toString()
                mainViewModel.loginUser(email, password)

                when {
                    email.isEmpty() -> {
                        tfPasswordLogin.error = "Masukkan Email"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        tfPasswordLogin.error = "Email Tidak Valid"
                    }
                    password.isEmpty() && password.length < 6 -> {
                        tfPasswordLogin.error = "Masukkan Password"
                    }
                    else -> {
                        setupLoginResult()
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        prefViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[PreferencesViewModel::class.java]
    }

    private fun setupLoginResult() {
        mainViewModel.loginResult.observe(this) { loginResult ->
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            val token = "Bearer ${loginResult.token}"
            prefViewModel.saveInfo(
                UserModel(
                    loginResult.name,
                    token,
                    true
                )
            )
            startActivity(intent)
        }
    }


    private fun showLoading(state: Boolean) {
        if (state) binding?.pbLogin?.visibility = View.VISIBLE
        else binding?.pbLogin?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        islog = null
    }

}