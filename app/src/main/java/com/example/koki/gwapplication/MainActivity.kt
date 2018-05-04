package com.example.koki.gwapplication

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.wikitude.architect.ArchitectStartupConfiguration
import com.wikitude.architect.ArchitectView
import com.wikitude.common.camera.CameraSettings.CameraPosition
import java.io.IOException
import android.support.v4.app.ActivityCompat




class MainActivity : AppCompatActivity() {

    var architectView: ArchitectView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.architectView = this.findViewById(R.id.architectView)
        val config = ArchitectStartupConfiguration()
        config.licenseKey = this.getWikitudeSDKLicenseKey()
        config.features = this.getCameraPosition().ordinal

        val REQUEST_CODE = 1
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)

        try {
            this.architectView!!.onCreate(config) //（3）
        } catch (ex: RuntimeException) {
            this.architectView = null
            Toast.makeText(applicationContext, "can't create Architect View", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (this.architectView != null) {
            // call mandatory live-cycle method of architectView
            this.architectView!!.onPostCreate()
            try {
                this.architectView!!.load(this.getARchitectWorldPath()) //（4）
                this.architectView!!.cullingDistance = 50 * 1000f
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this.architectView?.onResume() //（5）
    }

    override fun onPause() {
        super.onPause()
        this.architectView?.onPause() //（6）
    }

    override fun onDestroy() {
        super.onDestroy()
        this.architectView?.onDestroy()
    }

    private fun getWikitudeSDKLicenseKey(): String {
        return WikitudeSDKConstants.WIKITUDE_SDK_KEY
    }

    private fun getARchitectWorldPath(): String {
        return "wikitude/index.html" /* assets folder */
    }

    private fun getCameraPosition(): CameraPosition {
        return CameraPosition.BACK
    }
}
