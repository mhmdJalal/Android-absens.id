package com.example.absensid.ui.teacher

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.example.absensid.R
import com.example.absensid.core.Alert
import com.example.absensid.core.LoadingDialog
import com.example.absensid.ui.auth.AuthenticationActivity
import com.example.absensid.ui.permission.PermissionActivity
import kotlinx.android.synthetic.main.activity_teacher_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class TeacherMainActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var alert: Alert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_main)

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
            if (locationNotGranted() || storageNotGranted() || cameraNotGranted()) {
                toast("Allow permissions before continuing")
                startActivity<PermissionActivity>()
            } else {
                startActivity<TeacherFormAbsensiActivity>()
            }
        }
        btn_seemore.setOnClickListener {
            startActivity<TeacherAbsensiActivity>()
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
