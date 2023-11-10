package com.techno.baihai.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techno.baihai.databinding.FragmentImagePickerBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagePickerDialog : BottomSheetDialogFragment() {

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
    ): View? {
//        return inflater.inflate(R.layout.fragment_image_picker, container, false)
        binding = FragmentImagePickerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isCamera == true) {
            binding?.buttonCapture?.visibility = View.VISIBLE
        } else {
            binding?.buttonCapture?.visibility = View.GONE

        }

        binding?.buttonCapture?.setOnClickListener {

            if (checkCameraPermissionAndCaptureImage()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }


        binding?.buttonPickGallery?.setOnClickListener {
            if (checkCameraPermissionAndCaptureImage()) {
                pickImageFromGallery()
            } else {
                requestCameraPermission()
            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (targetFragment is ImagePickerListener) {
            listener = targetFragment as ImagePickerListener
        }
    }


    private fun checkCameraPermissionAndCaptureImage(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            cameraRequestCode
        )
    }

    private fun openCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val photoFile: File? = try {
//            createImageFiles()
//        } catch (ex: IOException) {
//            null
//        }
//        photoFile?.also {
//            val photoURI: Uri = FileProvider.getUriForFile(
//                requireContext(), "com.it.twiker.fileprovider", it
//            )
//            imageUri = photoURI
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//            startActivityForResult(intent, cameraRequestCode)
//        }
        takePhotoFromCamera()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) startActivityForResult(
            cameraIntent,
            cameraRequestCode
        )
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun createImageFiles(): File {
        // Create a unique file name based on the timestamp
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"

        // Get the directory for storing images
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Create the image file
        val imageFile = File(storageDir, imageFileName)

        // Save the current path
        var currentPhotoPath = imageFile.absolutePath

        return imageFile
    }


    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage,
            "Title", null
        )
        return Uri.parse(path)
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
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, galleryRequestCode)
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
                type = if (isVideo == true) {
                    "video/*"
                } else if (isImage == true) {
                    "image/*"
                } else if (isMedia == true) {
                    "image/* video/*"
                } else {
                    "image/*"
                }
//                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                action = Intent.ACTION_GET_CONTENT
            }
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
                    val selectedImageUri = data?.data
                    selectedImageUri?.let {
                        val img_file = Utilities.uriToFile(requireContext(), it)
                        listener?.onImageSelected(img_file!!)
                        dismiss()
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
