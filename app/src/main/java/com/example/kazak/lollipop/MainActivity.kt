package com.example.kazak.lollipop

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.usb.UsbDevice.getDeviceId
import android.content.Context.TELEPHONY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getIMEI()
    }

    fun getIMEI(){
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = telephonyManager.deviceId
        findViewById<TextView>(R.id.IMEIView).apply {
            text = imei
        }
    }
}
