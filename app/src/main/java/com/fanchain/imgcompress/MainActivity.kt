package com.fanchain.imgcompress

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import com.lzy.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private fun initImgPicker() {
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        imagePicker.isShowCamera = false
        imagePicker.isCrop = false
        imagePicker.isSaveRectangle = false
        imagePicker.selectLimit = 4
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initImgPicker()

        btn_quality.setOnClickListener {
            extStartActivity(QualityCompressActivity::class.java)
        }

        btn_simple.setOnClickListener {
            extStartActivity(SimpleSizeActivity::class.java)
        }

        btn_size.setOnClickListener {
            extStartActivity(PixelSizeActivity::class.java)
        }
    }
}
