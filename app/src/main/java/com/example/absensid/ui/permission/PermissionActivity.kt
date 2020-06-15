package com.example.absensid.ui.permission

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.absensid.R
import com.example.absensid.core.Alert
import kotlinx.android.synthetic.main.activity_permission.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PermissionActivity : AppCompatActivity() {

    private val viewModel: PermissionVm by viewModel()

    lateinit var pagerAdapter: PermissionPagerAdapter

    private var permissionsGranted: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        if (locationGranted()) {
            permissionsGranted.add("location")
        }
        if (storageGranted()) {
            permissionsGranted.add("storage")
        }
        if (cameraGranted()) {
            permissionsGranted.add("camera")
        }

        pagerAdapter =
            PermissionPagerAdapter(
                supportFragmentManager
            )

        pagerAdapter.addFragmet(PermissionLocationFragment())
        pagerAdapter.addFragmet(PermissionStorageFragment())
        pagerAdapter.addFragmet(PermissionCameraFragment())
        viewPager.adapter = pagerAdapter
        circleIndicator.setViewPager(viewPager)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        intent.putExtra("permissions", permissionsGranted.size)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun locationGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun storageGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun cameraGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    class PermissionPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList: ArrayList<Fragment> = ArrayList<Fragment>()

        override fun getItem(i: Int): Fragment {
            return fragmentList[i]
        }

        override fun getCount(): Int = fragmentList.size

        fun addFragmet(fragment: Fragment) {
            fragmentList.add(fragment)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionLocationFragment.PERMISSION_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted.add("location")
                viewModel.addPermissionsGranted(permissionsGranted)
                Log.e("REQ_LOCATION", "GRANTED")
            } else {
                Log.e("REQ_LOCATION", "DENIED")
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Alert(this).permissionDialog("Perizinan Lokasi", "Dengan tidak mengizinkan permintaan lokasi,\nmungkin beberapa service tidak akan berjalan lancar, buka Pengaturan Aplikasi untuk mengizinkan.", "Dismiss", "Pengaturan") { _: DialogInterface, _: Int ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, 0)
                    }
                }
                return
            }
        } else if (requestCode == PermissionStorageFragment.PERMISSION_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted.add("storage")
                viewModel.addPermissionsGranted(permissionsGranted)
            }
        } else if (requestCode == PermissionCameraFragment.PERMISSION_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted.add("camera")
                viewModel.addPermissionsGranted(permissionsGranted)
            }
        }
    }
}
