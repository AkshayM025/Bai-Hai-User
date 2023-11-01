package com.techno.baihai.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONException
import java.io.*
import java.text.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class Utilities @Inject constructor(
    private val context: Context
) {

    companion object {


        private fun createEmptyFile(fileName: String): File {
            return File.createTempFile(fileName, null)
        }

        // Then, when making the API request:
//        val vatCertificateFile: MultipartBody.Part = createPartFromFile(emptyFile)

        // Function to create a MultipartBody.Part from a file
        private fun createPartFromFile(file: File): MultipartBody.Part {
            val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("vatCertificateFile", file.name, requestFile)
        }

        fun showMessage(context: Context, str: String) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()

        }


        fun alertBox(context: Context) {
            val builder = AlertDialog.Builder(context)
            // Set the title
            builder.setTitle("AlertDialog Title")

            // Set a message (optional)
            builder.setMessage("This is the message of the AlertDialog.")

            // Add a cancel button
            builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                // Handle the cancel button click
                dialog.dismiss()
            }

            // Add an OK button
            builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                // Handle the OK button click
                dialog.dismiss()
            }
            // Customize other AlertDialog properties as needed
            val dialog = builder.create()

            // Set a custom color with transparency (e.g., transparent grey)
            val color = Color.argb(128, 0, 0, 0) // Adjust the alpha value (128) for transparency

            // Create a Drawable with the custom color
            val drawable = ColorDrawable(color)

// Set the background drawable for the AlertDialog overlay
            dialog.window?.setBackgroundDrawable(drawable)

            dialog.show()

        }

        fun alertBox2(context: Context) {
            val builder = AlertDialog.Builder(context)
            // Set the title
            builder.setTitle("AlertDialog Title")

            // Set a message (optional)
            builder.setMessage("This is the message of the AlertDialog.")

            // Add a cancel button
            builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                // Handle the cancel button click
                dialog.dismiss()
            }

            // Add an OK button
            builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                // Handle the OK button click
                dialog.dismiss()
            }            // Customize other AlertDialog properties as needed
            val dialog = builder.create()

// Set the background color of the AlertDialog overlay
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // Use a color or drawable resource for the background

            dialog.show()
        }

        private var MOBILE_NUMBER_PATTERN = Pattern.compile("^[0-9]{9,12}$")
        private var NAME_PATTERN = Pattern.compile("^(([^ ]?)(^[a-zA-Z].*[a-zA-Z]$)([^ ]?))$")
        var EMAIL_ADDRESS_PATTERN: Pattern =
            Pattern.compile(
                "^([a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~:<>;\"()_-]" +
                        "+)@{1}(([a-zA-Z0-9_-]{1,67})|([a-zA-Z0-9-]+\\.[a-zA-Z0-9-]{1,67}" +
                        "))\\.(([a-zA-Z]{2,10})(\\.[a-zA-Z]{2,10})?)$"
            )

        fun upperCaseFirst(value: String?): String? {
            if (TextUtils.isEmpty(value))
                return value
            else if (!TextUtils.isEmpty(value) && value.equals("null"))
                return value
            // Convert String to char array.
            val array = value?.toCharArray()
            try {
                // Modify first element in array.
                array?.set(0, Character.toUpperCase(array[0]))
                // Return string.
            } catch (e: Exception) {
            }
            return array?.let { String(it) }
        }

        fun getLikesCount(value: Int): String {
            if (value == null)
                return "0"
            else if (value < 1000)
                return value.toString()
            else if (value >= 1000) {
                var count = value / 1000
                return "${count}K"
            }
            return "0"
        }

        fun formateNumber(value: String): String {
            if (TextUtils.isEmpty(value))
                return value
            else if (!TextUtils.isEmpty(value) && value.equals("null"))
                return value
            var number = ""
            try {
                if (value.length > 6) number =
                    value.substring(0, 3) + "-${value.substring(3, 6)}-${
                        value.substring(
                            6,
                            value.length
                        )
                    }"
                else if (value.length > 3) number =
                    value.substring(0, 3) + "-${value.substring(3, value.length)}"
                else number = value
            } catch (e: Exception) {
            }
            return number
        }

        fun checkEmail(email: String): Boolean {
            try {
                return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        fun checkName(name: String): Boolean {
            try {
                if (name.isNullOrEmpty()) return false
                if (name.length < 2) return false
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }

        fun checkMobileNumber(name: String): Boolean {
            try {
                if (name.isNullOrEmpty()) return false
                if (name.length < 9) return false
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }

        fun upperCaseTitle(value: String?): String {
            var response = value
            try {
                if (TextUtils.isEmpty(value))
                    return value!!
                else if (!TextUtils.isEmpty(value) && value.equals("null"))
                    return value!!
                val strArray =
                    value!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val builder = StringBuilder()
                for (s in strArray) {
                    if (s != null && s.length > 1) {
                        val cap = s.substring(0, 1).toUpperCase() + s.substring(1)
                        builder.append("$cap ")
                    }
                }
                response = builder.toString()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return response!!
        }

        fun getStatusText(value: String?): String {
            var response = value
            try {
                if (TextUtils.isEmpty(value))
                    return value!!
                else if (!TextUtils.isEmpty(value) && value.equals("null"))
                    return value!!
                response = value!!.replace("_", " ")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return response!!
        }


        fun getFormatedValue(value: String?): String? {
            if (value == null)
                return value
            if (TextUtils.isEmpty(value))
                return value
            val priceFormat = DecimalFormat("0.00")
            try {
                if (value != "" && value != "null") {
                    priceFormat.minimumFractionDigits = 2
                    return priceFormat.format(java.lang.Double.parseDouble(value))
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return value
            }
            return value
        }

        fun getFormatedValueDigit(value: String?): String? {
            if (value == null)
                return value
            if (TextUtils.isEmpty(value))
                return value
            val priceFormat = DecimalFormat("0")
            try {
                if (value != null) {
                    if (value != "" && value != "null") {
                        priceFormat.minimumFractionDigits = 2
                        return priceFormat.format(java.lang.Double.parseDouble(value))
                    }
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return value
            }
            return value
        }

        fun getFormatedValueRating(value: String?): String? {
            if (value == null)
                return value
            if (TextUtils.isEmpty(value))
                return value
            val priceFormat = DecimalFormat("0.0")
            try {
                if (value != null) {
                    if (value != "" && value != "null") {
                        priceFormat.minimumFractionDigits = 1
                        return priceFormat.format(java.lang.Double.parseDouble(value))
                    }
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return value
            }
            return value
        }

        @Suppress("DEPRECATION")
        fun textFromHtml(html: String): Spanned {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            else Html.fromHtml(html)
        }

        fun isNumeric(str: String): Boolean {
            return str.matches("-?\\d+(\\.\\d+)?".toRegex())
            // match a number with optional '-' and decimal.
        }

        val SECOND_MILLIS = 1000
        val MINUTE_MILLIS = 60 * SECOND_MILLIS
        val HOUR_MILLIS = 60 * MINUTE_MILLIS
        val DAY_MILLIS = 24 * HOUR_MILLIS

        fun getTimeAgo(time: Long, currentTime: Long): String? {
            var time = time
            if (time < 1000000000000L) {
                time *= 1000
            }
            if (time > currentTime || time <= 0) {
                return null
            }
            val diff = currentTime - time
            return if (diff < MINUTE_MILLIS) {
                "just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                " a minute ago"
            } else if (diff < 50 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " min ago "
            } else if (diff < 90 * MINUTE_MILLIS) " an hour ago"
            else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " hours ago"
            } else if (diff < 48 * HOUR_MILLIS) {
                "yesterday"
            } else if (30 < diff / DAY_MILLIS) {
                "${(diff / DAY_MILLIS) / 30} month ago"
            } else {
                (diff / DAY_MILLIS).toString() + " days ago"
            }
        }

        fun uriToFile(context: Context, uri: Uri): File? {
            val contentResolver: ContentResolver = context.contentResolver
            val filePath = getRealPathFromURI(contentResolver, uri)

            if (filePath != null) {
                return File(filePath)
            }

            // If the above method fails, try copying the file to the app's cache directory
            val cacheDir = context.cacheDir
            val file = File(cacheDir, "temp_image_file")

            try {
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                inputStream?.close()
                outputStream.close()
                return file
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        private fun getRealPathFromURI(contentResolver: ContentResolver, uri: Uri): String? {
            var realPath: String? = null
            try {
                val cursor = contentResolver.query(uri, null, null, null, null)
                if (cursor != null) {
                    cursor.moveToFirst()
                    val index = cursor.getColumnIndex("_data")
                    if (index != -1) {
                        realPath = cursor.getString(index)
                    }
                    cursor.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return realPath
        }

        fun convertUtcToLocalTime(
            input: SimpleDateFormat,
            output: SimpleDateFormat,
            updateTime: String
        ): String {
            if (TextUtils.isEmpty(updateTime))
                return "N/A"
            if (updateTime == "0000-00-00 00:00:00") {
                return "N/A"
            }
            try {
                if (updateTime.equals("N/A", ignoreCase = true)) return "N/A"
                input.timeZone = TimeZone.getTimeZone("UTC")
                val updateDate = input.parse(updateTime)
                return output.format(updateDate)
            } catch (e1: Exception) {
                e1.printStackTrace()
                return "N/A"
            }
        }

        val decimalFormatSymbols = DecimalFormatSymbols().apply {
            decimalSeparator = ','
            groupingSeparator = ' '
            setCurrency(Currency.getInstance("AED"))
        }
    }

    fun getTime(time: Long): String {
        if (time < 60 && time != 0L) {
            return "$time sec"
        } else if (time < 60 * 60 && time != 0L) {
            val min = time / 60
            var remain = (time % 60)
            if (remain != 0L && remain > 2) {
                var sec = getTime(remain)
                return "$min min $sec"
            }
            return "$time min"
        } else if (time < 60 * 60 * 24 && time != 0L) {
            val hour = time / 60
            var remain = (time % 60)
            var time = ""
            if (remain != 0L && remain > 2) {
                time = getTime(remain)
                return "$hour hr $time"
            }
            return "$hour hr"
        } else {
            return "$time sec"
        }
    }

    fun getReceiveFileDirectory(): String? {
        val cacheDir: File
        try {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
                cacheDir = File(
                    Environment.getExternalStorageDirectory(),
                    "LMS/pdf"
                )
            else
                cacheDir = context.cacheDir
            if (!cacheDir.exists())
                cacheDir.mkdirs()
            return cacheDir.absolutePath
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return null
    }

    fun checkMobile(mobile: String): Boolean {
        return MOBILE_NUMBER_PATTERN.matcher(
            mobile.replace(
                "[^0-9]".toRegex(),
                ""
            )
        ).matches()
    }

    private fun getFlagResource(context: Context?, countryIso: String): Int {
        try {
            return context!!.resources.getIdentifier(
                "country_" + countryIso.toLowerCase(),
                "drawable",
                context.packageName
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private fun readRawFileAsString(context: Context?, rawFile: Int): String {
        val inputStream = context!!.resources.openRawResource(rawFile)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val result = StringBuffer()
        while (true) {
            val line = reader.readLine() ?: break
            result.append(line)
        }
        reader.close()
        return result.toString()
    }

    val appVersion: String
        get() {
            var appVersion = ""
            try {
                appVersion =
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return appVersion
        }

    /**
     * Method for getting device unique number
     * i.e DeviceId
     */
    @Suppress("DEPRECATION")
    val deviceId: String
        @SuppressLint("HardwareIds")
        get() {
            var deviceId = ""
            try {
                deviceId = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (TextUtils.isEmpty(deviceId))
                deviceId = "NA"
            return deviceId

            return deviceId
        }
    /**
     * method for email validation
     */
    /**
     * Validation regular expression
     */
    fun checkEmail(email: String): Boolean {
        try {
            if (email.length < 7) return false
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
//        (?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        Log.i("isValidPassword", "==isValidPassword==" + matcher.matches())
        return matcher.matches()
    }

    // * get network type *//

    private val isSDCARDMounted: Boolean
        get() {
            val status = Environment.getExternalStorageState()
            return status == Environment.MEDIA_MOUNTED
        }

    fun getAbsolutePath(uri: Uri): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = context.contentResolver.query(
            uri, projection,
            null, null, null
        )
        var filePath = ""
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            filePath = cursor.getString(columnIndex)
        }
        cursor?.close()
        return filePath
    }

    @SuppressLint("Range")
    fun getImageFilePath(uri: Uri): String? {
        val file = File(uri.path)
        var res: String? = null
        val filePath =
            file.path.split(":".toRegex()).toTypedArray()
        val image_id = filePath[filePath.size - 1]
        val cursor = context.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
            MediaStore.Images.Media._ID + " = ? ",
            arrayOf(image_id), null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            res = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor.close()
        }
        return res
    }

    fun getAbsolutePathMultiple(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        // Cursor cursor = ((Activity) context).managedQuery(uri, projection,
        // null, null, null);

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Explain to the user why we need to read the contacts
                val cursor = context!!.getContentResolver().query(uri, projection, null, null, null)
                if (cursor != null) {
                    val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    cursor.moveToFirst()
                    return cursor.getString(column_index)
                } else
                    return ""
            } else {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        }
        return ""
    }

    fun getNewFile(directoryName: String, imageName: String): File {
//        val root = Environment.getExternalStorageDirectory().toString() + directoryName
        val file: File
        var folder: File
        if (isSDCARDMounted) {
            folder = File(
                context.getExternalFilesDir(Environment.getExternalStorageDirectory().toString()),
                directoryName
            )
            folder.mkdir()
            file = File(folder, imageName)
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
//            File(root).mkdirs()
//            file = File(root, imageName)
        } else {
            file = File(context.filesDir, imageName)
        }
        return file
    }

    fun deletePicture(directoryName: String) {
        try {
            val root = Environment.getExternalStorageDirectory().toString()
            val f = File(root + directoryName)
            val files = f.listFiles() ?: return
            for (i in files.indices) {
                if (files[i].isFile)
                    files[i].delete()
            }
            f.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun upperCaseFirst(value: String): String {
        if (value == null)
            return ""
        if (value.equals(""))
            return ""
        if (TextUtils.isEmpty(value))
            return value
        else if (!TextUtils.isEmpty(value) && value.equals("null"))
            return value
        // Convert String to char array.
        val array = value.toCharArray()
        try {
            // Modify first element in array.
            array[0] = Character.toUpperCase(array[0])
            // Return string.
        } catch (e: Exception) {
        }
        return String(array)
    }

    fun getFormatedValueRating(value: String?): String? {
        val priceFormat = DecimalFormat("0.0")
        try {
            if (value != null) {
                if (value != "" && value != "null") {
                    priceFormat.minimumFractionDigits = 1
                    return priceFormat.format(java.lang.Double.parseDouble(value))
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return value
        }
        return value
    }

    protected fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                try {
                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(span.url))
                    context.startActivity(myIntent)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    protected fun setTextViewHTML(text: TextView, html: String) {
        val sequence = Html.fromHtml(html)
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(
            0, sequence.length,
            URLSpan::class.java
        )
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        text.text = strBuilder
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    fun htmlToString(text: TextView, string: String) {
        setTextViewHTML(text, string)
    }

    fun getFormatedValue(value: String?): String? {
        if (value == null)
            return value
        if (TextUtils.isEmpty(value))
            return value
        val priceFormat = DecimalFormat("0.00")
        try {
            if (value != null) {
                if (value != "" && value != "null") {
                    priceFormat.minimumFractionDigits = 2
                    return priceFormat.format(java.lang.Double.parseDouble(value))
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return value
        }
        return value
    }

    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = 24 * HOUR_MILLIS

    fun convertToLocalTime(
        input: SimpleDateFormat,
        output: SimpleDateFormat,
        updateTime: String
    ): String {
        if (TextUtils.isEmpty(updateTime))
            return "N/A"
        if (updateTime == "0000-00-00 00:00:00") {
            return "N/A"
        }
        try {
            if (updateTime.equals("N/A", ignoreCase = true)) return "N/A"
//            input.timeZone = TimeZone.getTimeZone("UTC")
            val updateDate = input.parse(updateTime)
            return output.format(updateDate)
        } catch (e1: Exception) {
            e1.printStackTrace()
            return "N/A"
        }
    }


    fun getFirstCharAndLastWordFirst(name: String): String {
        var s = ""
        try {
            if (TextUtils.isEmpty(name)) return ""
            val myName = name
                .split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val a = myName.size
            for (i in myName.indices) {
                if (i == 0) {
                    s = myName[i]
                }
                if (i == 1) {
                    s = s + " " + myName[i].get(0)
                    return s
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return s
    }

    fun convertLocalToUTCTime(
        input: SimpleDateFormat,
        output: SimpleDateFormat,
        updateTime: String
    ): String {
        if (TextUtils.isEmpty(updateTime))
            return "N/A"
        if (updateTime == "0000-00-00 00:00:00") {
            return "N/A"
        }
        try {
            if (updateTime.equals("N/A", ignoreCase = true)) return "N/A"
            val updateDate = input.parse(updateTime)
//            output.timeZone = TimeZone.getTimeZone("UTC")
            return output.format(updateDate)
        } catch (e1: Exception) {
            e1.printStackTrace()
            return "N/A"
        }
    }

    fun getLong(sValue: String): Long {
        val numberFormat = NumberFormat.getInstance(Locale.ENGLISH)
        var parse: Number? = null
        try {
            if (TextUtils.isEmpty(sValue) || sValue.equals("null", ignoreCase = true))
                return 0
            parse = numberFormat.parse(sValue)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return parse?.toLong() ?: 0
    }




    val isNetworkAvailable: Boolean
        get() {
            try {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected)
                    return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

    fun getNetworkType(): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info == null || !info.isConnected)
            return "-" //not connected
        if (info.type == ConnectivityManager.TYPE_WIFI)
            return "WIFI"
        if (info.type == ConnectivityManager.TYPE_MOBILE) {
            val networkType = info.subtype
            when (networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN //api<8 : replace by 11
                -> return "2G"

                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B //api<9 : replace by 14
                    , TelephonyManager.NETWORK_TYPE_EHRPD  //api<11 : replace by 12
                    , TelephonyManager.NETWORK_TYPE_HSPAP  //api<13 : replace by 15
                    , TelephonyManager.NETWORK_TYPE_TD_SCDMA  //api<25 : replace by 17
                -> return "3G"

                TelephonyManager.NETWORK_TYPE_LTE    //api<11 : replace by 13
                    , TelephonyManager.NETWORK_TYPE_IWLAN  //api<25 : replace by 18
                    , 19  //LTE_CA
                -> return "4G"

                else -> return ""
            }
        }
        return ""
    }
}
