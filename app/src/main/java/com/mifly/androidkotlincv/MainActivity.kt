package com.mifly.androidkotlincv

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.OpenCVLoader
import org.opencv.imgproc.Imgproc
import org.opencv.core.Mat
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.android.CameraBridgeViewBase
import android.content.pm.PackageManager
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {
    private val TAG = "OCVSample::Activity"
    private var isGray = false

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i("TAG", "OpenCV loaded successfully")
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_change_color.setOnClickListener { _ ->
            val bitmap = BitmapFactory.decodeResource(
                this@MainActivity.resources, R.drawable.mini
            )

            if (!isGray) {
                val src = Mat()
                val dst = Mat()

                Utils.bitmapToMat(bitmap, src)
                Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY)
                Utils.matToBitmap(dst, bitmap)
                iv_sample.setImageBitmap(bitmap)
                isGray = true
            } else {
                iv_sample.setImageBitmap(bitmap)
                isGray = false
            }
        }

        btn_change_color_native.setOnClickListener { _ ->
            val bitmap = BitmapFactory.decodeResource(
                this@MainActivity.resources, R.drawable.mini
            )
            if (!isGray) {
                val src = Mat()
                val dst = Mat()

                Utils.bitmapToMat(bitmap, src)
                gray(src.getNativeObjAddr(), dst.getNativeObjAddr())
                Utils.matToBitmap(dst, bitmap)
                iv_sample.setImageBitmap(bitmap)
                isGray = true
            } else {
                iv_sample.setImageBitmap(bitmap)
                isGray = false
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    toast("Permission denied to use camera")
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun gray(srcAddr: Long, dstAddr: Long)

    companion object {
     init {
            System.loadLibrary("native-lib")
        }
    }
}
