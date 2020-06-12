package com.example.absensid.ui.student

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.SparseArray
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import com.example.absensid.R
import com.example.absensid.core.Alert
import com.example.absensid.ui.FotoPickerActivity
import com.example.absensid.ui.ImageViewerActivity
import com.example.absensid.util.gone
import com.example.absensid.util.underlineText
import com.example.absensid.util.visible
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.android.synthetic.main.activity_student_form_absensi.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class StudentFormAbsensiActivity : AppCompatActivity() {

    private lateinit var alert: Alert

    private var imageSelfie: Bitmap? = null
    private var temporaryImagePath: String? = null
    private var temporaryFileName: String = "image_selfie"
    private var tempNamaGambar: String? = null

    companion object {
        const val REQ_CAMERA = 212
        const val PERMISSION_CAMERA = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_form_absensi)

        alert = Alert(this)

        image_back.setOnClickListener {
            super.onBackPressed()
        }

        tv_location.text = HtmlCompat.fromHtml("<u><font color=#42a5f5>-6.661729, 106.772752</font></u>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        tv_location.setOnClickListener {
            val lat = -6.661729
            val long = 106.772752
            val gmmIntentUri: Uri = Uri.parse("geo:$lat,$long")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.data = gmmIntentUri
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                val uri = "https://www.google.com/maps/search/?api=1&query=$lat,$long"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.data = Uri.parse(uri)
                startActivity(intent)
            }
        }
        layout_image_temp.setOnClickListener {
            photoLayoutOnClick()
        }
        image_selfie.setOnClickListener {
            val urls = ArrayList<String>()
            urls.add(temporaryImagePath.toString())
            startActivity<ImageViewerActivity>("urls" to urls)
        }
        iv_cancel_image.setOnClickListener {
            imageSelfie = null
            temporaryImagePath = null
            tempNamaGambar = null

            card_image_selfie.gone()
            iv_cancel_image.gone()
            layout_image_temp.visible()
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
                val tempImage = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.RGB_565);

                val canvas = Canvas(tempImage)
                canvas.drawBitmap(tempImage, 0f, 0f, null)

                val faceDetector = FaceDetector.Builder(applicationContext)
                    .setTrackingEnabled(false)
                    .build()

                if (!faceDetector.isOperational) {
                    alert.dialog("Could not set up the face detector!")
                    return
                }

                val frame: Frame = Frame.Builder().setBitmap(tempImage).build()
                val faces: SparseArray<Face> = faceDetector.detect(frame)

                val myRectPaint = Paint()
                myRectPaint.strokeWidth = 5f
                myRectPaint.color = Color.RED
                myRectPaint.style = Paint.Style.STROKE

                for (i in 0 until faces.size()) {
                    val thisFace = faces.valueAt(i)
                    val x1 = thisFace.position.x
                    val y1 = thisFace.position.y
                    val x2 = x1 + thisFace.width
                    val y2 = y1 + thisFace.height
                    canvas.drawRoundRect(RectF(x1, y1, x2, y2), 2f, 2f, myRectPaint)
                }
                image_selfie.setImageDrawable(BitmapDrawable(resources, tempImage))

                imageSelfie = tempImage
                temporaryImagePath = photoFile.absolutePath
                tempNamaGambar = photoFile.name

                image_selfie.setImageBitmap(imageSelfie)
                card_image_selfie.visible()
                iv_cancel_image.visible()
                layout_image_temp.gone()

            } catch (e: FileNotFoundException) {
                alert.dialog("Maaf terjadi kesalahan, silahkan coba kembali")
                return
            } catch (e: IOException) {
                alert.dialog("Maaf terjadi kesalahan, silahkan coba kembali")
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
