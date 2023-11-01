package com.techno.baihai.activity.kotlin


import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.techno.baihai.R
import com.techno.baihai.activity.PinLocationActivity
import com.techno.baihai.activity.ThankyouPointActivity
import com.techno.baihai.api.Constant
import com.techno.baihai.databinding.ActivityProductDonateBinding
import com.techno.baihai.service.MyPlacesAdapter
import com.techno.baihai.utils.CustomSnakbar
import com.techno.baihai.utils.GPSTracker
import com.techno.baihai.utils.PrefManager
import com.techno.baihai.utils.Tools
import org.json.JSONException
import org.json.JSONObject
import www.develpoeramit.mapicall.ApiCallBuilder
import www.develpoeramit.mapicall.ApiCallBuilder.onResponse
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class ProductDonateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private var binding: ActivityProductDonateBinding? = null
    var mContext: Context? = this@ProductDonateActivity
//    var iv_donate: CardView? = null
//    var tv_donate: CardView? = null
    var uid: String? = null
    var image: String? = null
    var lat = 0.0
    var lng = 0.0
    var latitude: String? = null
    var longitude: String? = null
    var view: View? = null
//    var et_productDesc: EditText? = null
//    var et_productName: EditText? = null
//    var pr_donateYesid: TextView? = null
//    var pr_donateNoid: TextView? = null
//    var et_productLocation: TextView? = null
    var category_name: String? = null
    var category_id: String? = null
    private var catid: String? = null
    var category: ArrayList<String?>? = null
//    var spinner: Spinner? = null
    private var usedTxt: String? = null
    private var returnValue = ArrayList<Uri?>()
    var test: String? = null

    var adapter: MyPlacesAdapter? = null
//    private var map_location: ImageView? = null
    private var isInternetPresent = false
    private var file1: File? = null
    private var file2: File? = null
    private var file3: File? = null
    private var file4: File? = null
    private var file5: File? = null
//    private var rl_Pager: RelativeLayout? = null
    private var photos_viewpager: ViewPager? = null
//    private var sliderDotspanel: LinearLayout? = null
//    private var btnRemoveImage: ImageView? = null
    private var proSliderAdapter: ProductAdapter? = null
    private var fileHashMap: HashMap<String, File?>? = null
    private var progressDialog: ProgressDialog? = null
    var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null


    companion object {
        private const val TAG = "ProductDonate"
        private const val AUTOCOMPLETE_REQUEST_CODE = 111
        var imageSlider: SliderView? = null
        @JvmStatic
        fun task() {
            imageSlider!!.visibility = View.GONE
//            binding.visibility = View.VISIBLE
        }

        @Throws(IOException::class)
        fun getFile(context: Context?, uri: Uri?): File {
            val destinationFilename =
                File(context!!.filesDir.path + File.separatorChar + queryName(context, uri))
            try {
                context.contentResolver.openInputStream(uri!!).use { ins ->
                    createFileFromStream(
                        ins!!, destinationFilename
                    )
                }
            } catch (ex: Exception) {
                Log.e("Save File", ex.message!!)
                ex.printStackTrace()
            }
            return destinationFilename
        }

        private fun createFileFromStream(ins: InputStream, destination: File?) {
            try {
                FileOutputStream(destination).use { os ->
                    val buffer = ByteArray(4096)
                    var length: Int
                    while (ins.read(buffer).also { length = it } > 0) {
                        os.write(buffer, 0, length)
                    }
                    os.flush()
                }
            } catch (ex: Exception) {
                Log.e("Save File", ex.message!!)
                ex.printStackTrace()
            }
        }

        private fun queryName(context: Context?, uri: Uri?): String {
            val returnCursor = context!!.contentResolver.query(uri!!, null, null, null, null)!!
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult<PickVisualMediaRequest, Uri>(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->

            if (uri != null) {

                returnValue.add(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_product_donate)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_donate)


        PrefManager.isConnectingToInternet(mContext)
        isInternetPresent = PrefManager.isNetworkConnected(mContext)
        category = ArrayList()
        //set  imageSlider
        imageSlider = findViewById(R.id.imageSlider)
        //Initializing Spinner

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        binding?.spinner?.onItemSelectedListener = this
        binding?.tvDonate?.setOnClickListener { // listener.click(new T(listener));
            startActivity(Intent(mContext, ThankyouPointActivity::class.java))
        }
        // Registers a photo picker activity launcher in single-select mode.
        binding?.prDonateYesid?.setOnClickListener {
            // listener.click(new T(listener));
            setLog("el producto que donara el usuario es usado")
            binding?.prDonateYesid?.setBackgroundColor(Color.parseColor("#257712"))
            binding?.prDonateNoid?.setBackgroundColor(Color.parseColor("#727272"))
            binding?.prDonateYesid?.setTextColor(Color.parseColor("#FFFFFF"))
            binding?.prDonateNoid?.setTextColor(Color.parseColor("#000000"))
            usedTxt = ""
            usedTxt = binding?.prDonateYesid?.text.toString().trim { it <= ' ' }
        }
        binding?.prDonateNoid?.setOnClickListener {
            setLog("el producto que donara el usuario no es usado")
            binding?.prDonateYesid?.setBackgroundColor(Color.parseColor("#727272"))
            binding?.prDonateNoid?.setBackgroundColor(Color.parseColor("#257712"))
            binding?.prDonateYesid?.setTextColor(Color.parseColor("#000000"))
            binding?.prDonateNoid?.setTextColor(Color.parseColor("#FFFFFF"))
            usedTxt = ""
            usedTxt = binding?.prDonateNoid?.text.toString().trim { it <= ' ' }
        }
        val user = PrefManager.getInstance(mContext).user
        uid = user.id.toString()
        Log.i(TAG, "user_id: $uid")
        currentLocation
        binding?.mapLocation?.setOnClickListener {
            startActivityForResult(
                Intent(
                    this@ProductDonateActivity,
                    PinLocationActivity::class.java
                ), AUTOCOMPLETE_REQUEST_CODE
            )
        }
        binding?.tvDonate?.setOnClickListener { view ->
            if (isInternetPresent) {
                setLog("el usuario dono un producto")
                validate(view)
            } else {
                val prefManager = PrefManager(mContext)
                PrefManager.showSettingsAlert(mContext)
            }
        }
        if (isInternetPresent) {
            getCategory()
        } else {
            val prefManager = PrefManager(mContext)
            PrefManager.showSettingsAlert(mContext)
        }
        photos_viewpager = findViewById(R.id.photos_viewpager)
        binding?.image1?.setOnClickListener {
            val camera = Manifest.permission.CAMERA
            //WRITE_EXTERNAL_STORAGE
            var permission_additional =
                Manifest.permission.READ_EXTERNAL_STORAGE
            var permission_additional2 =
                Manifest.permission.READ_EXTERNAL_STORAGE
            var permission_addtional3 =
                Manifest.permission.READ_EXTERNAL_STORAGE
            var permission_addtional4 =
                Manifest.permission.READ_EXTERNAL_STORAGE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission_additional2 = Manifest.permission.READ_MEDIA_VIDEO
                permission_additional = Manifest.permission.READ_MEDIA_IMAGES
                permission_addtional3 = Manifest.permission.READ_MEDIA_AUDIO
                permission_addtional4 = Manifest.permission.READ_MEDIA_IMAGES
            }
            Dexter.withContext(this@ProductDonateActivity)
                .withPermissions(
                    camera,
                    permission_additional,
                    permission_additional2,
                    permission_addtional3,
                    permission_addtional4
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            try {


                                // options.setPreSelectedUrls(ArrayList<Uri> data);
                                //  Pix.start(ProductDonateActivity.this, options);
                                val mediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType =
                                    ImageOnly as ActivityResultContracts.PickVisualMedia.VisualMediaType
                                val request: PickVisualMediaRequest =
                                    PickVisualMediaRequest.Builder()
                                        .setMediaType(mediaType)
                                        .build()
                                pickMedia!!.launch(request)
                            } catch (e: Exception) {
                                Log.i(
                                    TAG,
                                    "cdfcsef: " + e.message
                                )
                            }
                        } else {
                            showSettingDialogue()
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
        binding?.btnRemoveimage?.setVisibility(View.GONE)
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, poistion: Int, l: Long) {
        catid = adapterView.selectedItemId.toString()
        Log.i(TAG, "catId=>$catid")
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {

    }

    private val currentLocation: Unit
        get() {
            val track = GPSTracker(mContext)
            if (track.canGetLocation()) {
                latitude = track.latitude.toString()
                Log.e("lat=>", "-------->$latitude")
                longitude = track.longitude.toString()
                Log.e("lon=>", "-------->$longitude")
            } else {
                track.showSettingsAlert()
            }
        }

    public override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.cancel()
        }
    }

    private fun validate(view: View) {
        if (binding?.etProductName?.equals("")!!) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please enter product name!")
            binding?.etProductName?.requestFocus()
        } else if (binding?.spinner?.selectedItem.toString().trim { it <= ' ' } == "Select Category") {
            Toast.makeText(mContext, "Please select a Category", Toast.LENGTH_SHORT).show()
        } else if (binding?.etProductDesc!!.equals("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please enter description!")
            binding?.etProductDesc?.requestFocus()
        } else if (binding?.etProductLocation!!.equals("")) {
            CustomSnakbar.showDarkSnakabar(mContext, view, "Please select a location!")
            binding?.etProductLocation?.requestFocus()
        } else {
            uploadProductToStoreApi(uid, binding?.etProductName?.text.toString(), binding?.etProductDesc?.text.toString(), binding?.etProductLocation?.text.toString(), view)
        }
    }

    private fun uploadProductToStoreApi(
        uid: String?, p_name: String, p_description: String,
        p_location: String, view: View
    ) {
        if (usedTxt == null) {
            usedTxt = "Y"
        }
        if (fileHashMap == null) {
            CustomSnakbar.showSnakabar(mContext, view, "Please Select a Product Image")
        } else {
            progressDialog = ProgressDialog(mContext)
            progressDialog!!.setMessage("Please wait...")
            progressDialog!!.show()
            val parms = HashMap<String, String?>()
            parms["user_id"] = uid
            parms["name"] = p_name
            parms["description"] = p_description
            parms["address"] = p_location
            parms["used"] = usedTxt
            parms["category_id"] = catid
            parms["lat"] = lat.toString()
            parms["lon"] = lng.toString()
            Log.e("camPath", fileHashMap.toString())


//  http://bai-hai.com/webservice/add_product_by_user?name=testproduct&description=thisis%20test&address=vijay&
            // lat=789456&lon=5464&category_id=1&user_id=12
            AndroidNetworking.upload(Constant.BASE_URL + "add_product_by_user?")
                .addMultipartParameter(parms)
                .addMultipartFile(fileHashMap)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        progressDialog!!.dismiss()
                        Log.e("ResponseUpdate", "" + response)
                        try {
                            val `object` = JSONObject(response)
                            val status = `object`.optString("status")
                            val message = `object`.optString("message")
                            if (status == "1") {
                                binding?.etProductName!!.setText("")
                                binding?.etProductDesc!!.setText("")
                                binding?.etProductLocation!!.text = ""
                                finish()
                                setLog("se cargo el producto donado por el usuario $p_name")
                                startActivity(Intent(mContext, ThankyouPointActivity::class.java))
                                Animatoo.animateInAndOut(mContext)
                            } else {
                                progressDialog!!.dismiss()
                                CustomSnakbar.showDarkSnakabar(mContext, view, "" + message)
                            }
                        } catch (e: JSONException) {
                            progressDialog!!.dismiss()
                            e.printStackTrace()
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog!!.dismiss()
                        // Toast.makeText(mContext, "apifall" + anError, Toast.LENGTH_LONG).show();
                    }
                })
        }
    }

    private fun getCategory() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.show()
        var langua = "EN"
        val lang = PrefManager.get(mContext, "lang")
        if (lang == "es") {
            langua = "ES"
        }
        val param = HashMap<String, String?>()
        param["lat"] = latitude
        param["lon"] = longitude
        param["language"] = langua
        ApiCallBuilder.build(mContext)
            .isShowProgressBar(false)
            .setUrl(Constant.BASE_URL + "get_category") //http://bai-hai.com/webservice/get_category
            .setParam(param)
            .execute(object : onResponse {
                override fun Success(response: String) {
                    Log.e("Response=>", "" + response)
                    progressDialog!!.dismiss()
                    try {
                        val `object` = JSONObject(response)
                        val status = `object`.optString("status")
                        val message = `object`.optString("message")
                        if (status == "1") {
                            try {
                                val jArray = `object`.optJSONArray("result")
                                // Log.e(TAG, "result=>" + jArray);
                                //Initializing the ArrayList
                                if (lang == "es" && lang != null) {
                                    category!!.add("Seleccionar Categoria")
                                } else {
                                    category!!.add("Select Category")
                                }
                                if (jArray != null) {
                                    for (i in 0 until jArray.length()) {
                                        val object1 = jArray.getJSONObject(i)


                                        //Log.e(TAG, "resulti=>" + i);
                                        category_id = object1.getString("id")
                                        category_name = object1.getString("category_name")
                                        val imageUrl = object1.getString("image")
                                        category!!.add(category_name)
                                    }
                                }
                                if (category != null && category != null) {
                                    binding?.spinner!!.adapter = ArrayAdapter(
                                        mContext!!, android.R.layout.simple_spinner_dropdown_item,
                                        category!!
                                    )
                                } else {
                                    Toast.makeText(
                                        mContext,
                                        "Category Not Found..!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                progressDialog!!.dismiss()
                                e.printStackTrace()
                            }
                        } else {
                            progressDialog!!.dismiss()

                            //Toast.makeText(mContext, "Status" + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (e: JSONException) {
                        progressDialog!!.dismiss()
                        e.printStackTrace()
                        //Toast.makeText(mContext, "Exception" + e, Toast.LENGTH_SHORT).show();
                    }
                }

                override fun Failed(error: String) {
                    progressDialog!!.dismiss()
                    //CustomSnakbar.showDarkSnakabar(mContext, mview, "" + error);
                    //Toast.makeText(mContext, "Error" + error, Toast.LENGTH_SHORT).show();
                }
            })
    }

    private fun showPictureDialog() {
        if (mContext != null) {
            val pictureDialog = AlertDialog.Builder(mContext)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems = arrayOf(
                "Select photo from gallery",
                "Capture photo from camera"
            )
            pictureDialog.setItems(
                pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            }
            pictureDialog.show()
        } else {
            Toast.makeText(mContext, "Some fields in image  are null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun choosePhotoFromGallary() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, 1)
    }

    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(mContext!!.packageManager) != null) {
            startActivityForResult(cameraIntent, 0)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == RESULT_OK || requestCode == 100) {
                /* aqui carga las imagenes*/
                //  returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                // Log.e("TAG",data.getData().getPath());
                if (returnValue.size == 0) {
                    returnValue.add(data!!.data)
                }
                binding?.image1?.visibility = View.GONE
                //  myPagerAdapter = new MyPagerAdapter(mContext, returnValue);
                // photos_viewpager.setAdapter(myPagerAdapter);
                binding?.rlPager!!.visibility = View.GONE
                binding?.btnRemoveimage!!.visibility = View.GONE
                imageSlider!!.visibility = View.VISIBLE
                if (returnValue.size > 0) {
                    rotateImages()
                    val dataImg = ArrayList<String>()
                    for (i in returnValue.indices) {
                        try {
                            val file = getFile(
                                mContext,
                                returnValue[i]
                            ) //create path from uri
                            val Info = file.path.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                            dataImg.add(Info[0])
                        } catch (e: Exception) {
                        }
                    }
                    proSliderAdapter = ProductAdapter(applicationContext, dataImg)
                    imageSlider!!.setSliderAdapter(proSliderAdapter!!)
                    proSliderAdapter!!.notifyDataSetChanged()
                    imageSlider!!.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    imageSlider!!.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                    imageSlider!!.autoCycleDirection =
                        SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                    imageSlider!!.indicatorSelectedColor = Color.WHITE
                    imageSlider!!.indicatorUnselectedColor = Color.GRAY
                    imageSlider!!.scrollTimeInSec = 4 //set scroll delay in seconds :
                    imageSlider!!.startAutoCycle()
                    SharePost()


                    proSliderAdapter?.setOnItemClickListener(object :
                        ProductAdapter.OnClickListener {
                        override fun onItemClick(currentItem: ArrayList<String>?) {

                          proSliderAdapter?.notifyDataSetChanged()

                        }
                    })
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "cdsf" + e.message)
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            lat = data!!.extras!!.getDouble("lat")
            lng = data.extras!!.getDouble("lng")
            binding?.etProductLocation!!.text = Tools.getCompleteAddressString(mContext, lat, lng)
            Log.e("TAG", "latisda$lat")
            Log.e("TAG", "longidbh$lng")
        }
    }

    @Throws(IOException::class)
    private fun SharePost() {
        if (returnValue.size > 0) {
            fileHashMap = HashMap()

            //     images = new ArrayList<>();
            for (i in returnValue.indices) {
                Log.e("vcfgxb", returnValue.size.toString())
                when (i) {
                    0 -> {
                        file1 = getFile(
                            applicationContext,
                            returnValue[i]
                        )
                        fileHashMap!!["image1"] = file1
                    }

                    1 -> {
                        file2 = getFile(
                            applicationContext,
                            returnValue[i]
                        )
                        fileHashMap!!["image2"] = file2
                    }

                    2 -> {
                        file3 = getFile(
                            applicationContext,
                            returnValue[i]
                        )
                        fileHashMap!!["image3"] = file3
                    }

                    3 -> {
                        file4 = getFile(
                            applicationContext,
                            returnValue[i]
                        )
                        fileHashMap!!["image4"] = file4
                    }

                    4 -> {
                        file5 = getFile(
                            applicationContext,
                            returnValue[i]
                        )
                        fileHashMap!!["image5"] = file5
                    }
                }
                Log.e("bfgbf", fileHashMap.toString())
            }
        } else {
            CustomSnakbar.showDarkSnakabar(mContext, binding!!.root, "Please Select Image!")
        }
    }

    private fun rotateImages() {
        if (returnValue.size > 0) {
            //     images = new ArrayList<>();
            for (i in returnValue.indices) {
                Log.e("vcfgxb", returnValue.size.toString())
                var `in`: InputStream? = null
                try {
                    `in` = FileInputStream(
                        mContext!!.contentResolver.openFileDescriptor(
                            returnValue[i]!!, "r"
                        )!!.fileDescriptor
                    )
                } catch (e: FileNotFoundException) {
                    Log.e("TAG", "originalFilePath is not valid", e)
                }
                val options = BitmapFactory.Options()
                var bitmap = BitmapFactory.decodeStream(`in`, null, options)
                val matrix = Matrix()
                matrix.postRotate(90f)
                bitmap = Bitmap.createScaledBitmap(bitmap!!, bitmap.width, bitmap.height, true)
                bitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)

            }
        } else {
            CustomSnakbar.showDarkSnakabar(mContext, binding!!.root, "Please Select Image!")
        }
    }


    private fun showSettingDialogue() {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialogInterface, i ->
            dialogInterface.cancel()
            openSetting()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialogInterface, i -> dialogInterface.cancel() }
        builder.show()
    }

    private fun openSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", mContext!!.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun setLog(message: String) {
        val user = PrefManager.getInstance(mContext).user
        var id: String? = null
        id = if (user.id === "") {
            "1"
        } else {
            user.id
        }
        val parms1 = HashMap<String, String?>()
        parms1["user_id"] = id
        parms1["activity"] = message
        ApiCallBuilder.build(mContext)
            .setUrl(Constant.BASE_URL + Constant.LOG_APP)
            .setParam(parms1)
            .execute(object : onResponse {
                override fun Success(response: String) {
                    try {
                        Log.e("selectedresponse=>", "-------->$response")
                        val `object` = JSONObject(response)
                        val status = `object`.getString("status")
                        if (status == "true") {
                            Log.e("selectedresponse=>", "-------->exitoso")
                        }
                    } catch (e: JSONException) {


                        //Toast.makeText(mContext, "Error:" + e, Toast.LENGTH_SHORT).show();
                        e.printStackTrace()
                    }
                }

                override fun Failed(error: String) {}
            })
    }

    fun onBackFromUploadProduct(view: View?) {
        onBackPressed()
    }


}
