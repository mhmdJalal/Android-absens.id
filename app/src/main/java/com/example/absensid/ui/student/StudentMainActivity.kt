package com.example.absensid.ui.student

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.absensid.R
import com.example.absensid.core.Alert
import com.example.absensid.core.LoadingDialog
import com.example.absensid.ui.auth.AuthenticationActivity
import com.example.absensid.ui.permission.PermissionActivity
import kotlinx.android.synthetic.main.activity_student_main.*
import org.jetbrains.anko.startActivity

class StudentMainActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var alert: Alert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)

        alert = Alert(this)
        loadingDialog = LoadingDialog(this)

        requestPermission()

        btn_signout.setOnClickListener {
            alert.confirmation(message = "Are you sure you want to sign out?", positiveMessage = "YES", negativeMessage = "NO", cancelable = false) {
                alert.dismiss()
                loadingDialog.show()
                Handler().postDelayed({
                    loadingDialog.dismiss()
                    startActivity<AuthenticationActivity>()
                    finish()
                }, 1500)
            }
        }
        btn_presence.setOnClickListener {
            startActivity<StudentFormAbsensiActivity>()
        }
        btn_seemore.setOnClickListener {
            startActivity<StudentAbsensiActivity>()
        }
    }

    private fun requestPermission() {
        if (locationNotGranted() || storageNotGranted() || cameraNotGranted()) {
            startActivity<PermissionActivity>()
        }
    }

    private fun locationNotGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    private fun storageNotGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    }

    private fun cameraNotGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }
}