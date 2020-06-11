package com.example.absensid.ui.auth

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.absensid.R
import com.example.absensid.core.LoadingDialog
import com.example.absensid.ui.student.StudentMainActivity
import kotlinx.android.synthetic.main.fragment_login_student.*
import org.jetbrains.anko.startActivity

/**
 * A simple [Fragment] subclass.
 */
class LoginStudentFragment : Fragment() {

    private lateinit var fragmentContext: Context
    private lateinit var loadingDialog: LoadingDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(fragmentContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login.setOnClickListener {
            loadingDialog.show()
            Handler().postDelayed({
                loadingDialog.dismiss()
                fragmentContext.startActivity<StudentMainActivity>()
                activity?.finish()
            }, 3000)
        }
    }

}
