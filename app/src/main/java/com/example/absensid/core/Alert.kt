package com.example.absensid.core

import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.absensid.R
import com.example.absensid.util.gone
import com.example.absensid.util.visible
import kotlinx.android.synthetic.main.dialog_confirmation_default.view.*

class Alert(private val context: Context) {

    private var builder: AlertDialog.Builder = AlertDialog.Builder(context)
    private var alert: AlertDialog = builder.create()
    private var v: View? = null

    fun dialog(error: String) {
        builder.apply {
            setMessage(error)
            setPositiveButton("OK")  { _, _ ->  }
        }
        alert = builder.create()
        if (!alert.isShowing) {
            alert.show()
        }
    }

    fun dialog(error: String, cancelable: Boolean = true, okButton: (DialogInterface, Int) -> Unit) {
        builder.apply {
            setMessage(error)
            setCancelable(cancelable)
            setPositiveButton("OK", DialogInterface.OnClickListener(okButton))
        }
        alert = builder.create()
        if (!alert.isShowing) {
            alert.show()
        }
    }

    fun confirmation(@Nullable title: String? = null, message: String, positiveMessage: String?, negativeMessage: String?, cancelable: Boolean = false, listener: (View) -> Unit) {
        v = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation_default, null)
        builder.apply {
            setView(v)
            setCancelable(cancelable)
        }

        if (v != null) {
            with(v!!) {
                if (title.isNullOrEmpty()) {
                    tv_title_confirmation.gone()
                } else {
                    tv_title_confirmation.visible()
                }

                if (negativeMessage != null) {
                    tv_no.text = negativeMessage
                }
                if (positiveMessage != null) {
                    tv_yes.text = positiveMessage
                }

                tv_title_confirmation.text = title
                tv_content_confirmation.text = message
                tv_yes.setOnClickListener(listener)
                tv_no.setOnClickListener {
                    alert.dismiss()
                }
            }
        }

        alert = builder.create()
        if (!alert.isShowing) {
            alert.show()
        }
    }

    fun confirmation(@Nullable title: String? = null, message: Spanned, positiveMessage: String?, @Nullable negativeMessage: String?, cancelable: Boolean = false, listener: (View) -> Unit) {
        v = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation_default, null)
        builder = AlertDialog.Builder(context)
        builder.apply {
            setView(v)
            setCancelable(cancelable)
        }

        if (v != null) {
            with(v!!) {
                if (title.isNullOrEmpty()) {
                    tv_title_confirmation.gone()
                } else {
                    tv_title_confirmation.visible()
                }

                if (negativeMessage != null) {
                    tv_no.text = negativeMessage
                }
                if (positiveMessage != null) {
                    tv_yes.text = positiveMessage
                }

                tv_title_confirmation.text = title
                tv_content_confirmation.text = message
                tv_yes.setOnClickListener(listener)
                tv_no.setOnClickListener {
                    alert.dismiss()
                }
            }
        }

        alert = builder.create()
        if (!alert.isShowing) {
            alert.show()
        }
    }

    fun permissionDialog(title: String, error: String, negativeButton: String, positiveButton: String, okButton: (DialogInterface, Int) -> Unit) {
        builder.apply {
            setTitle(title)
            setMessage(error)
            setPositiveButton(positiveButton, DialogInterface.OnClickListener(okButton))
            setNegativeButton(negativeButton) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }
        alert = builder.create()
        if (!alert.isShowing) {
            alert.show()
        }
    }

    fun isShowing(): Boolean {
        return alert.isShowing
    }

    fun show() {
        alert.show()
    }

    fun dismiss() {
        alert.dismiss()
    }

}
