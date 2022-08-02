package com.ansengarie.storyapp.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ansengarie.storyapp.databinding.ActivityUploadBinding
import com.ansengarie.storyapp.ui.camera.CameraActivity
import com.ansengarie.storyapp.ui.home.MainViewModel
import com.ansengarie.storyapp.ui.login.dataStore
import com.ansengarie.storyapp.utils.UserPreferences
import com.ansengarie.storyapp.utils.reduceFileImage
import com.ansengarie.storyapp.utils.rotateBitmap
import com.ansengarie.storyapp.utils.uriToFile
import com.ansengarie.storyapp.utils.PreferencesViewModel
import com.ansengarie.storyapp.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var prefViewModel: PreferencesViewModel
    private var _storyBinding: ActivityUploadBinding? = null
    private val binding get() = _storyBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var token: String

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupViewModel()
        setupBinding()
    }

    private fun setupViewModel() {
        prefViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[PreferencesViewModel::class.java]

        prefViewModel.getInfo().observe(this) { story ->
            token = story.token
        }
    }

    private fun uploadImageStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description =
                binding?.tfDesc?.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val currentImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                currentImageFile
            )

            if (binding?.tfDesc?.text?.isEmpty() == true) {
                binding!!.tfDesc.error = "Deskripsi Tidak Boleh Kosong!"
            } else {
                mainViewModel.uploadStory(token, imageMultipart, description)
                mainViewModel.storyResponse.observe(this) { storyResponse ->
                    if (!storyResponse.error) {
                        Toast.makeText(this, "Upload Success!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private var getFile: File? = null

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding?.ivUploaded?.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Silahkan pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile
            binding?.ivUploaded?.setImageURI(selectedImg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _storyBinding = null
    }

    private fun setupView() {
        _storyBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun setupBinding() {
        binding?.apply {
            btnCameraUpload.setOnClickListener { startCameraX() }
            btnGalleryUpload.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener { uploadImageStory() }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}