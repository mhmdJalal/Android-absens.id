package com.example.absensid.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enabled() {
    isEnabled = true
}

fun View.disabled() {
    isEnabled = false
}

fun View.disable() {
    alpha = 0.5f
}

fun View.enable() {
    alpha = 1f
}

fun TextView.underlineText() {
    paintFlags = this.paintFlags and Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.strikethruText() {
    paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG
}

fun SwipeRefreshLayout.stopRefreshing() {
    isRefreshing = false
}

fun SwipeRefreshLayout.startRefreshing() {
    isRefreshing = true
}

fun <T : Any> T?.isNull(): Boolean {
    return this == null
}
fun <T : Any> T?.isNotNull(): Boolean {
    return this != null
}

object Utils {

    fun convertPhoneNumber(phone: String): String {
        return if (phone.startsWith("62")) {
            "0${phone.substring(2, phone.length)}"
        } else {
            phone
        }
    }

    fun convertSecretPhoneNumber(phone: String): String {
        if (phone.isNotEmpty()) {
            val phoneNumber = if (phone.startsWith("62")) {
                "0${phone.substring(2, phone.length)}"
            } else {
                phone
            }

            val awal = phoneNumber.substring(0, 3)
            val akhir = phoneNumber.substring(phoneNumber.length - 3, phoneNumber.length)
            val secret = phoneNumber.substring(3, phoneNumber.length - 3)
            var secretNumber = ""
            for (i in 1..secret.length) {
                secretNumber += "*"
            }
            Log.e("PHONENUMBER", "phone $awal$secret$akhir")
            Log.e("PHONENUMBER", "phone $phoneNumber")
            return "$awal$secretNumber$akhir"
        }

        return ""
    }

    fun convertSecretEmail(email: String): String {

        if (email.isNotEmpty()) {
            val emailName = email.substringBefore("@")
            val awal = if (emailName.length > 5) {
                email.substring(0, 2)
            } else {
                email.substring(0, 1)
            }
            val secret = if (emailName.length > 5) {
                emailName.substring(2, emailName.length - 1)
            } else {
                emailName.substring(1, emailName.length - 1)
            }
            val akhir = emailName.substring(emailName.length - 1, emailName.length) + "@" + email.substringAfter("@")

            var secretEmail = ""
            for (i in 1..secret.length) {
                secretEmail += "*"
            }
            Log.e("PHONENUMBER", "$email $awal$secretEmail$akhir")
            return "$awal$secretEmail$akhir"
        }

        return ""
    }

    fun addThousandSeparator(value: Double): String {
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols

        symbols.groupingSeparator = '.'
        formatter.decimalFormatSymbols = symbols
        return formatter.format(value)
    }

    fun addDecimalSeparator(value: Double): String {
        val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols

        symbols.groupingSeparator = ','
        formatter.decimalFormatSymbols = symbols
        return formatter.format(value)
    }

    /*fun addRupiahSeparator(value: Double): String {
        *//*val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols

        symbols.groupingSeparator = ','
        formatter.decimalFormatSymbols = symbols
        return formatter.format(value)*//*
        //val harga = 250000000.0
        //val localID=Locale("in","ID")
        val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()

        formatRp.currencySymbol = ""
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp
        return kursIndonesia.format(value)
    }*/

    /*fun setViewByRatio(view: View, widthRatio: Float, heightRatio: Float) {
        val scale = heightRatio / widthRatio

        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        view.layoutParams.height = Math.round(view.width * scale)
    }

    fun dismissDialog(dialog: Dialog?) {
        if (dialog != null && dialog.isShowing)
            dialog.dismiss()
    }*/

    fun parsePhoneString(phoneString: String): String {
        var phone = phoneString
        if (phone.startsWith("+"))
            phone = phone.replace("+", "")

        if (phone.startsWith("0"))
            phone = "62${phone.substring(1)}"

        return phone
    }

    fun dpToPixels(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    @SuppressLint("MissingPermission")
    fun isNetworkConnected(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val koneksi = connectivity.activeNetworkInfo
        if (koneksi!=null && koneksi.isConnectedOrConnecting){
            return true
        }
        return false
        /* return connectivity.activeNetworkInfo != null*/
    }

    interface ResponseHandler {
        fun onFinish()
    }

    /*fun createFile(context: Context, image: Bitmap, fileName: String): File? {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
        val file = File(Environment.getExternalStorageDirectory(), "$fileName")

        val fileOutputStream: FileOutputStream
        try {
            file.createNewFile()
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.close()
            return file
        } catch (e: FileNotFoundException) {
            Alert.dialog(context, "Maaf terjadi kesalahan, silahkan coba kembali")
            return null
        } catch (e: IOException) {
            Alert.dialog(context, "Maaf terjadi kesalahan, silahkan coba kembali")
            return null
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }*/

    fun deleteCache(context: Context) {
        try {
            val dir: File = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}
