package com.example.absensid.ui.student

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import org.jetbrains.anko.toast

class StudentMainActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var alert: Alert

    companion object {
        private const val REQ_PERMISSION = 100
    }

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
            if (locationNotGranted() || storageNotGranted() || cameraNotGranted()) {
                toast("Allow permissions before continuing")
                val intent = Intent(this, PermissionActivity::class.java)
                startActivityForResult(intent, REQ_PERMISSION)
            } else {
                startActivity<StudentFormAbsensiActivity>()
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_PERMISSION && resultCode == Activity.RESULT_OK) {
            val permissions = data?.getIntExtra("permissions", 0)
            if (permissions != null) {
                if (permissions >= 3) {
                    startActivity<StudentFormAbsensiActivity>()
                }
            }
        }
    }
}