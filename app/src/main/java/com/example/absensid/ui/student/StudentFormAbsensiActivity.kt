package com.example.absensid.ui.student

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.SparseArray
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import com.example.absensid.R
import com.example.absensid.core.Alert
import com.example.absensid.ui.FotoPickerActivity
import com.example.absensid.ui.ImageViewerActivity
import com.example.absensid.util.gone
import com.example.absensid.util.underlineText
import com.example.absensid.util.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_student_form_absensi.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class StudentFormAbsensiActivity : AppCompatActivity() {

    private lateinit var alert: Alert
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var imageSelfie: Bitmap? = null
    private var temporaryImagePath: String? = null
    private var temporaryFileName: String = "image_selfie"
    private var tempNamaGambar: String? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    companion object {
        const val REQ_CAMERA = 212
        const val PERMISSION_CAMERA = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_form_absensi)

        alert = Alert(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        image_back.setOnClickListener {
            super.onBackPressed()
        }

        tv_location.setOnClickListener {
            val gmmIntentUri: Uri = Uri.parse("geo:$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.data = gmmIntentUri
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                val uri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.data = Uri.parse(uri)
                startActivity(intent)
            }
        }
        image_sync_location.setOnClickListener {
            getLastLocation()
        }
        layout_image_temp.setOnClickListener {
            photoLayoutOnClick()
        }
        image_selfie.setOnClickListener {
            val urls = ArrayList<String>()
            urls.add(temporaryImagePath.toString())
            startActivity<ImageViewerActivity>("urls" to urls, "isFile" to true)
        }
        iv_cancel_image.setOnClickListener {
            val file = File(temporaryImagePath)
            if (file.exists()) {
                file.delete()
            }
            imageSelfie = null
            temporaryImagePath = null
            tempNamaGambar = null

            card_image_selfie.gone()
            iv_cancel_image.gone()
            layout_image_temp.visible()
        }
        btn_save.setOnClickListener {
            alert.confirmation(message = "Is the data appropriate?", cancelable = false, positiveMessage = "Yes", negativeMessage = "No") {
                alert.dismiss()
                finish()
            }
        }
    }

    private fun getLastLocation() {
        toast("Start location synchronization...")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    val latLong = "${location.latitude}, ${location.longitude}"
                    tv_location.text = HtmlCompat.fromHtml("<u><font color=#42a5f5>$latLong</font></u>", HtmlCompat.FROM_HTML_MODE_COMPACT)
                } else {
                    toast("Failed to get location")
                }
            }
    }

    private fun photoLayoutOnClick() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this as Activity, permissions, PERMISSION_CAMERA)
        } else
            openCamera()
    }

    private fun openCamera() {
        val namaFile = temporaryFileName
        val intent = Intent(this, FotoPickerActivity::class.java)
        intent.putExtra("namaFile", namaFile)
        intent.putExtra("mulaicamera", true)
        startActivityForResult(intent, REQ_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) return

        if (resultCode != Activity.RESULT_OK) {
            alert.dialog("Maaf terjeadi kesalahan, silahkan coba kembali")
            return
        }

        if (requestCode == REQ_CAMERA) {
            try {
                val path = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/.absensid")
                val xnmiamge = data?.getStringExtra("namaFile") ?: return
                val photoFile = File(path, "$xnmiamge.jpg")
                val img = BitmapFactory.decodeFile(photoFile.absolutePath)

                val highAccuracyOpts = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .build()

                val image = InputImage.fromBitmap(img, 0)
                val detector = FaceDetection.getClient(highAccuracyOpts)
                val result = detector.process(image)
                    .addOnSuccessListener { faces ->
                        if (faces.isNotEmpty()) {
                            if (faces.size == 1) {
                                imageSelfie = Bitmap.createScaledBitmap(img, img.width, img.height, false)
                                temporaryImagePath = photoFile.absolutePath
                                tempNamaGambar = photoFile.name

                                image_selfie.setImageBitmap(imageSelfie)
                                card_image_selfie.visible()
                                iv_cancel_image.visible()
                                layout_image_temp.gone()
                            } else {
                                toast("More than one face, please take your own picture")
                            }
                        } else {
                            toast("No faces in the image were detected")
                        }
                    }
                    .addOnFailureListener { e ->
                        toast("Failed to scan image")
                    }

            } catch (e: FileNotFoundException) {
                alert.dialog("Sorry, an error occurred, please try again")
                return
            } catch (e: IOException) {
                alert.dialog("Sorry, an error occurred, please try again")
                return
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            alert.dialog("Maaf dengan tidak mengijinkan akses kamera dan penyimpanan, " +
                    "tahapan penambahan foto tidak dapat dilanjutkan.")
        }
    }
}
