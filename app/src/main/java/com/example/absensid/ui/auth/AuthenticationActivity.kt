package com.example.absensid.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.absensid.R
import kotlinx.android.synthetic.main.activity_authentication.*
import java.util.ArrayList

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val pagerAdapter =
            AuthenticationPagerAdapter(
                supportFragmentManager
            )
        pagerAdapter.addFragmet(LoginStudentFragment())
        pagerAdapter.addFragmet(LoginTeacherFragment())
        viewPager.adapter = pagerAdapter
    }

    internal class AuthenticationPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList: ArrayList<Fragment> = ArrayList<Fragment>()

        override fun getItem(i: Int): Fragment {
            return fragmentList[i]
        }

        override fun getCount(): Int = fragmentList.size


        fun addFragmet(fragment: Fragment) {
            fragmentList.add(fragment)
        }
    }
}
