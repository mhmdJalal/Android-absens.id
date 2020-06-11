package com.example.absensid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.absensid.R
import com.example.absensid.ui.auth.AuthenticationActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.jetbrains.anko.startActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity<AuthenticationActivity>()
            finish()
        }, 1500)

        val myanim = AnimationUtils.loadAnimation(this, R.anim.splashanim)
        image_splash.startAnimation(myanim)
    }
}
