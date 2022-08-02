package com.ansengarie.storyapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.ansengarie.storyapp.R
import com.ansengarie.storyapp.databinding.ActivityRegisterBinding
import com.ansengarie.storyapp.ui.home.MainViewModel

class RegisterActivity : AppCompatActivity() {

    private var _registerBinding: ActivityRegisterBinding? = null
    private val binding get() = _registerBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()

        binding?.apply {

            btnRegis.setOnClickListener {
                val name = tfNameRegis.text.toString()
                val email = tfEmailRegis.text.toString()
                val password = tfPasswordRegis.text.toString()
                val confirmPassword = tfConfirmPasswordRegis.text.toString()

                when {
                    name.isEmpty() -> {
                        tfNameRegis.error = "Masukkan Nama"
                    }
                    email.isEmpty() -> {
                        tfEmailRegis.error = "Masukkan Email"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        tfEmailRegis.error = "Email Tidak Valid"
                    }
                    password.isEmpty() && password.length < 6 -> {
                        tfPasswordRegis.error = "Masukkan Password"
                    }
                    confirmPassword.isEmpty() -> {
                        tfConfirmPasswordRegis.error = "Masukkan Kembali Password"
                    }
                    password != confirmPassword -> {
                        tfConfirmPasswordRegis.error = "Password Tidak Sama"
                    }
                    else -> {
                        mainViewModel.registerUser(name, email, password)
                        AlertDialog.Builder(this@RegisterActivity).apply {
                            setTitle("Selamat!")
                            setMessage("Akun anda telah berhasil terdaftar.")
                            setPositiveButton("Next") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _registerBinding = null
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_regis)
        }

        _registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}