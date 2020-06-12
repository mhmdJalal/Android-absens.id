package com.example.absensid.ui.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.absensid.R
import com.example.absensid.util.gone
import com.example.absensid.util.visible
import kotlinx.android.synthetic.main.activity_permission.*
import kotlinx.android.synthetic.main.fragment_permission_location.*
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class PermissionLocationFragment : Fragment(), View.OnClickListener {

    private val viewModel: PermissionVm by sharedViewModel()

    private lateinit var fragmentContext: Context

    companion object {
        const val PERMISSION_LOCATION = 200
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permission_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.permissionsGranted.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                for (item in it) {
                    if (item == "location") {
                        btn_granted.gone()
                        tv_granted.visible()
                    }
                }
            }
        })

        if (!locationNotGranted()) {
            btn_granted.gone()
            tv_granted.visible()
        }

        btn_next.setOnClickListener(this)
        btn_granted.setOnClickListener(this)
    }

    private fun requestLocationPermissions() {
        if (locationNotGranted()) {
            requestPermissions()
        } else {
            if (!isLocationEnabled()) {
                fragmentContext.toast("Turn on location")
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                fragmentContext.startActivity(intent)
            }
        }
    }

    private fun locationNotGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(fragmentContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fragmentContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentContext as Activity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(fragmentContext as Activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(
                fragmentContext as Activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = fragmentContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            btn_next.id -> {
                (fragmentContext as PermissionActivity).viewPager.currentItem += 1
            }
            btn_granted.id -> {
                if (locationNotGranted()) {
                    requestLocationPermissions()
                }
            }
        }
    }
}
