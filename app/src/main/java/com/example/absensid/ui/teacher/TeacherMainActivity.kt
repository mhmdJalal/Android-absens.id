package com.example.absensid.ui.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.absensid.R
import com.example.absensid.ui.auth.AuthenticationActivity
import kotlinx.android.synthetic.main.activity_teacher_main.*
import org.jetbrains.anko.startActivity

class TeacherMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_main)

        btn_signout.setOnClickListener {
            startActivity<AuthenticationActivity>()
            finish()
        }
    }
}
