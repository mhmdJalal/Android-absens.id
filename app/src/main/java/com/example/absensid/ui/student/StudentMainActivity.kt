package com.example.absensid.ui.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.absensid.R
import com.example.absensid.core.Alert
import com.example.absensid.core.LoadingDialog
import com.example.absensid.ui.auth.AuthenticationActivity
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

        btn_signout.setOnClickListener {
            alert.confirmation(message = "Are you sure you want to sign out?", positiveMessage = "YES", negativeMessage = "NO", cancelable = false) {
                alert.dismiss()
                loadingDialog.show()
                Handler().postDelayed({
                    loadingDialog.dismiss()
                    startActivity<AuthenticationActivity>()
                    finish()
                }, 2000)
            }
        }
        btn_seemore.setOnClickListener {
            startActivity<StudentAbsensiActivity>()
        }
    }
}