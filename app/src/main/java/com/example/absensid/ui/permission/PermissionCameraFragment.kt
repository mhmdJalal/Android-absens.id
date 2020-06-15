package com.example.absensid.ui.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.absensid.R
import com.example.absensid.util.gone
import com.example.absensid.util.visible
import kotlinx.android.synthetic.main.fragment_permission_camera.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class PermissionCameraFragment : Fragment() {

    private val viewModel: PermissionVm by sharedViewModel()

    private lateinit var fragmentContext: Context

    private var permissionsGranted: List<String> = arrayListOf()

    companion object {
        const val PERMISSION_CAMERA = 202
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permission_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.permissionsGranted.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                permissionsGranted = it
                for (item in it) {
                    if (item == "camera") {
                        btn_granted.gone()
                        tv_granted.visible()
                    }
                }
            }
        })

        if (!cameraNotGranted()) {
            btn_granted.gone()
            tv_granted.visible()
        }

        btn_granted.setOnClickListener {
            if (cameraNotGranted()) {
                requestPermissions()
            }
        }
        btn_finish.setOnClickListener {
            val activity = (fragmentContext as PermissionActivity)
            activity.intent.putExtra("permissions", permissionsGranted.size)
            activity.setResult(Activity.RESULT_OK, activity.intent)
            activity.finish()
        }
    }

    private fun cameraNotGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(fragmentContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentContext as Activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                fragmentContext as Activity,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA
            )
//        }
    }

}
