package com.example.userblinketclone

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.example.userblinketclone.databinding.ProgressDialogBinding

object Utils
{

    private var dialog : AlertDialog? = null
    fun hideDialog()
    {
        dialog?.dismiss()

    }


    fun showDialog(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val progress = ProgressDialogBinding.inflate(inflater)
        progress.tvLoading.text = message
        dialog = AlertDialog.Builder(context)
            .setView(progress.root)
            .setCancelable(false)
            .create()
        dialog?.show()
    }


    fun showToast(context: Context, message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }
}



