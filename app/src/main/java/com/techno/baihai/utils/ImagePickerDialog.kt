package com.techno.baihai.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.techno.baihai.databinding.FragmentImagePickerBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagePickerDialog : BottomSheetDialogFragment() {
    private var cameraPicker: ActivityResultLauncher<Intent>? = null
    private var imageVideoPicker: ActivityResultLauncher<Intent>? = null
    private var position: Int? = 1

    private var binding: FragmentImagePickerBinding? = null
    private val _binding get() = binding!!


    private val cameraRequestCode = 101
    private val galleryRequestCode = 102

    private var imageUri: Uri? = null
    private var isCamera: Boolean? = true
    private var isVideo: Boolean? = false
    private var isImage: Boolean? = false
    private var isMedia: Boolean? = false
    private var listener: ImagePickerListener? = null
    private var onMediaPickedListener: ((List<File>) -> Unit)? = null

    fun setOnMediaPickedListener(listener: (List<File>) -> Unit) {
        onMediaPickedListener = listener
    }


    interface ImagePickerListener {
        fun onImageSelected(imageFile: File)
    }

    fun setImageListener(listeners: ImagePickerListener) {
        listener = listeners
    }

    fun setShowCamera(isCameras: Boolean) {
        isCamera = isCameras
    }

    fun setShowVideoOnly(isVideos: Boolean) {
        isVideo = isVideos
    }

    fun setShowImageOnly(isImages: Boolean) {
        isImage = isImages
    }

    fun setShowMedia(isVideos: Boolean) {
        isVideo = isVideos
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePickerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        imageVideoPicker =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedMediaFiles = mutableListOf<File>()
                    val selectedMediaClipData = result.data?.clipData

                    if (selectedMediaClipData != null) {
                        // Multiple items were selected
                        for (i in 0 until selectedMediaClipData.itemCount) {
                            val uri = selectedMediaClipData.getItemAt(i).uri
                            val file = uriToFile(uri)
                            if (file != null) {
                                selectedMediaFiles.add(file)
                            }
                        }
                    } else {
                        // Single item was selected
                        val uri = result.data?.data
                        if (uri != null) {
                            val file = uriToFile(uri)
                            if (file != null) {
                                selectedMediaFiles.add(file)
                            }
                        }
                    }

                    //  onMediaPickedListener?.invoke(selectedMediaFiles)
                    listener?.onImageSelected(selectedMediaFiles[0])

                }
            }

        cameraPicker =

            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data?.data
                    Log.e("cameraPath", uri.toString())


                    if (uri != null) {
                        val file = uriToFile(uri)
                        if (file != null) {
                            Log.e("cameraPath", file.path)
                            // onMediaPickedListener?.invoke(listOf(file))
                            listener?.onImageSelected(file)


                        }
                    } else {
                        Log.e("cameraPath", "null")

                    }
                }
            }

        if (isCamera == true) {
            binding?.buttonCapture?.visibility = View.VISIBLE
        } else {
            binding?.buttonCapture?.visibility = View.GONE

        }


        binding?.buttonCapture?.setOnClickListener {

            position = 0
            checkAndRequestPermissions(position!!)

        }


        binding?.buttonPickGallery?.setOnClickListener {
            position = 1
            checkAndRequestPermissions(position!!)


        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (targetFragment is ImagePickerListener) {
            listener = targetFragment as ImagePickerListener
        }
    }

    private fun checkAndRequestPermissions(position: Int) {
        dismiss()
        val camera = Manifest.permission.CAMERA
        var permissionAdditional = Manifest.permission.READ_EXTERNAL_STORAGE
        var permissionAdditional1 = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionAdditional = Manifest.permission.READ_MEDIA_VIDEO
            permissionAdditional1 = Manifest.permission.READ_MEDIA_IMAGES
        }
        Dexter.withContext(this.activity)
            .withPermissions(
                camera,
                permissionAdditional,
                permissionAdditional1,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        choosePicker(position)
                    } else {
                        // showSettingDialogue()
                        checkAndRequestPermissions(position)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { error ->
                Log.e(
                    "Dexter",
                    "There was an error: $error"
                )
            }.check()


    }

    private fun choosePicker(position: Int) {
        when (position) {
            0 -> {
                openCamera()
            }

            1 -> {
                pickImageFromGallery()
            }

            else -> {
                pickImageFromGallery()

            }
        }
    }

    private fun openCamera() {
        /*        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photoFile: File? = try {
                    createImageFiles()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(), "com.it.twiker.fileprovider", it
                    )
                    imageUri = photoURI
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, cameraRequestCode)
                }*/
        takePhotoFromCamera()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null)
            startActivityForResult(cameraIntent, cameraRequestCode)
        //  cameraPicker?.launch(cameraIntent)
    }


    private fun uriToFile(uri: Uri): File? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            val name = it.getString(nameIndex)
            val file = File(requireContext().cacheDir, name)
            val outputStream = file.outputStream()

            try {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun getImageFile(inContext: Context, inImage: Bitmap, fileName: String): File? {
        val imageFile = File(inContext.getExternalFilesDir(null), fileName)
        try {
            val fileOutputStream = FileOutputStream(imageFile)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            return imageFile
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun pickImageFromGallery() {

        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = if (isVideo == true) {
                    "video/*"
                } else if (isImage == true) {
                    "image/*"
                } else if (isMedia == true) {
                    "image/* video/*"
                } else {
                    "image/*"
                }
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                action = Intent.ACTION_GET_CONTENT
            }
        // imageVideoPicker?.launch(intent)

        startActivityForResult(intent, galleryRequestCode)

    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                cameraRequestCode -> {

                    val imageBitmap = data?.extras?.get("data") as Bitmap?

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    if (imageBitmap != null) {
                        // val tempUri = getImageUri(requireContext(), imageBitmap)
                        val timeStamp =
                            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                        val imageFileName = "JPEG_$timeStamp.jpg"
                        val imageFile =
                            getImageFile(requireContext(), imageBitmap, imageFileName)

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        //  val pathOfImg = tempUri?.let { RealPathUtil.getRealPath(requireContext(), it) }
                        if (imageFile != null) {
                            listener?.onImageSelected(imageFile)


                            // File(pathOfImg).let { listener?.onImageSelected(it) }
                        } else {
                            // Handle the case where pathOfImg is null
                            Log.e("ImageCapture", "PathOfImg is null")
                        }
                    } else {
                        // Handle the case where imageBitmap is null
                        Log.e("ImageCapture", "ImageBitmap is null")
                    }
                    dismiss()

                }


                galleryRequestCode -> {
                    val selectedMediaFiles = mutableListOf<File>()
                    val selectedMediaClipData = data?.clipData

                    if (selectedMediaClipData != null) {
                        // Multiple items were selected
                        for (i in 0 until selectedMediaClipData.itemCount) {
                            val uri = selectedMediaClipData.getItemAt(i).uri
                            val file = uriToFile(uri)
                            if (file != null) {
                                selectedMediaFiles.add(file)
                            }
                        }
                        listener?.onImageSelected(selectedMediaFiles[0])

                    } else {
                        // Single item was selected
                        val selectedImageUri = data?.data

                        selectedImageUri?.let {
                            val img_file = Utilities.uriToFile(requireContext(), it)
                            listener?.onImageSelected(img_file!!)
                            dismiss()
                        }

                        //  onMediaPickedListener?.invoke(selectedMediaFiles)


                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): ImagePickerDialog {
            return ImagePickerDialog()
        }
    }
}
