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
import kotlinx.android.synthetic.main.activity_permission.*
import kotlinx.android.synthetic.main.fragment_permission_storage.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PermissionStorageFragment : Fragment() {

    private val viewModel: PermissionVm by sharedViewModel()
    private lateinit var fragmentContext: Context

    companion object {
        const val PERMISSION_STORAGE = 201
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permission_storage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.permissionsGranted.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                for (item in it) {
                    if (item == "storage") {
                        btn_granted.gone()
                        tv_granted.visible()
                    }
                }
            }
        })

        if (!storageNotGranted()) {
            btn_granted.gone()
            tv_granted.visible()
        }

        btn_next.setOnClickListener {
            (fragmentContext as PermissionActivity).viewPager.currentItem += 1
        }
        btn_granted.setOnClickListener {
            if (storageNotGranted()) {
                requestPermissions()
            }
        }
    }

    private fun storageNotGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(fragmentContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fragmentContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        /*if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentContext as Activity, Manifest.permission.READ_EXTERNAL_STORAGE) ||
            ActivityCompat.shouldShowRequestPermissionRationale(fragmentContext as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {*/
            ActivityCompat.requestPermissions(
                fragmentContext as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_STORAGE
            )
//        }
    }
}
