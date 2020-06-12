package com.example.absensid.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.*
import kotlin.math.min
import kotlin.math.roundToInt


class FotoPickerActivity : Activity() {
    private var camorientation = 0
    private var namaFile: String? = null
    private val CAMERA = 2
    private var savedPhoto: File? = null
    private var mulai: Boolean = false
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        namaFile = intent.getStringExtra("namaFile")
        mulai = intent.getBooleanExtra("mulaicamera",false)

        if (mulai) {
            startCamera()
            mulai = false
        }
    }

    private  fun startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                val cameraId = manager.cameraIdList[0]
                val characteristics = manager.getCameraCharacteristics(cameraId)
                camorientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
            } catch (e: Exception) {
            }
        }

        val path = getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/.absensid")
        val tempFile = File(path, "/.absensid")
        if (!tempFile.isDirectory) {
            path?.mkdirs()
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val mediaFile = File(path, "$namaFile.jpg")
            fileUri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                Uri.fromFile(mediaFile)
            } else {
                FileProvider.getUriForFile(this, "$packageName.com.example.absensid.provider", mediaFile)
            }

            if (fileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                startActivityForResult(intent, CAMERA)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                savedPhoto = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/.absensid"), "$namaFile.jpg")
                handleSamplingAndRotationBitmap(this, Uri.fromFile(savedPhoto))
                setResult(Activity.RESULT_OK, intent)
            }
        }
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (namaFile != null) {
            outState.putString("nama_file", "")
            outState.putString("nama_file", namaFile)
        }
        outState.putBoolean("mulai", false)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        namaFile = savedInstanceState.getString("nama_file")
        mulai = savedInstanceState.getBoolean("mulai")
    }

    @Throws
    private fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri): Bitmap {
        val maxHeight = 1024
        val maxWidth = 1024
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var imageStream: InputStream  = context.contentResolver.openInputStream(selectedImage)!!
        BitmapFactory.decodeStream(imageStream, null, options)
        imageStream.close()
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        imageStream = context.contentResolver.openInputStream(selectedImage)!!
        var img:Bitmap  = BitmapFactory.decodeStream(imageStream, null, options)!!
        img = rotateImageIfRequired(img, selectedImage)
        val maxImageSize = 1300
        val ratio = min(
                maxImageSize.toFloat() / img.width,
                maxImageSize.toFloat() / img.height)
        val xwidth = (ratio * img.width).roundToInt()
        val xheight = (ratio * img.height).roundToInt()
        img = Bitmap.createScaledBitmap(img, xwidth, xheight, false)
        val bytes = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        try {
            val outputStream =FileOutputStream(savedPhoto!!.path)
            outputStream.write(bytes.toByteArray())
            outputStream.close()
        } catch (e:IOException) {
            e.printStackTrace()
        }
        return img
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
        }
        return inSampleSize
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri?): Bitmap {
        if (selectedImage == null) return img
        return try {
            val ei = ExifInterface(selectedImage.path)
            var orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            if (orientation == -1) {
                orientation = getRotationFromMediaStore(this, selectedImage)
            }
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
                else -> img
            }
        } catch (e:IOException) {
            img
        }
    }

    private fun rotateImage(img: Bitmap , degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    private fun getRotationFromMediaStore(context: Context, imageUri: Uri): Int {
        val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION)
        val cursor = context.contentResolver.query(imageUri, columns, null, null, null) ?: return 0
        cursor.moveToFirst()
        val orientationColumnIndex = cursor.getColumnIndex(columns[1])
        val hasil = cursor.getInt(orientationColumnIndex)
        cursor.close()
        return hasil
    }
}