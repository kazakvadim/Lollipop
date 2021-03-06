package com.example.kazak.lollipop

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.info_fragment.*
import androidx.core.content.ContextCompat.checkSelfPermission
import android.Manifest
import com.example.kazak.lollipop.Helpers.Constants.Companion.IMEI_PERMISSION_CODE
import com.example.kazak.lollipop.Helpers.PermissionHelper


class InfoFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        version_text_view.text = BuildConfig.VERSION_NAME
        request_imei.setOnClickListener{
            request_button(view)
        }
        if (context?.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            getIMEI()
            request_imei.visibility = View.GONE
        } else {
            requestImeiPermission();
        }
    }

    fun getIMEI(){
        val telephonyManager = activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = telephonyManager.deviceId
        imei_text_view.text = imei
    }

    fun request_button(view: View){
        if (context?.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, getString(R.string.permission_granted_text),
                    Toast.LENGTH_SHORT).show()
        } else {
            requestImeiPermission();
        }
    }

    fun requestImeiPermission(){
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
            Toast.makeText(context, getString(R.string.need_permission),
                    Toast.LENGTH_LONG).show()
        }
        requestPermissions(arrayOf(android.Manifest.permission.READ_PHONE_STATE), IMEI_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == IMEI_PERMISSION_CODE){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context, getString(R.string.permission_granted_text_short), Toast.LENGTH_SHORT).show()
                getIMEI()
                request_imei.visibility = View.GONE
            } else {
                Toast.makeText(context, getString(R.string.permission_denied_text_short), Toast.LENGTH_SHORT).show()
            }
        }
    }
}