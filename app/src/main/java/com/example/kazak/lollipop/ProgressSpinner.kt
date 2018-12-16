package com.example.kazak.lollipop

import android.app.ProgressDialog
import android.content.Context

class ProgressSpinner(context: Context) {
    private var context: Context
    init {
        this.context = context
    }
    private var progressDialog: ProgressDialog? = null
    fun showProgressDialog() {
        if (progressDialog == null) {
            val pd = ProgressDialog(context)
            pd.setCancelable(false)
            pd.setMessage("Loading...")
            progressDialog = pd
        }
        progressDialog?.show()
    }

    fun hideProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}