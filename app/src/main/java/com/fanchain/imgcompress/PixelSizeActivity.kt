package com.fanchain.imgcompress

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioButton
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_quality_compress.*
import kotlinx.android.synthetic.main.compress_imginfo_layout.*
import java.io.File
import java.io.FileOutputStream

class PixelSizeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pixel_size)

        initPickView()


        et_quality.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    sb_quality.progress = if (s.toString().toInt() > 100) 100 else s.toString().toInt()
                }
            }
        })

        sb_quality.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                et_quality.setText("${progress}")
                et_quality.setSelection(et_quality.text.toString().length)
            }

        })

        btn_compress.setOnClickListener {
            if (originalPath.isNullOrEmpty()) {
                showToast("先选择一张图片", this)
                return@setOnClickListener
            }
            if (sb_quality.progress < 1) {
                showToast("缩放比例最低 1", this)
                return@setOnClickListener
            }

            var compressWidht = (originalBitmap!!.width * (sb_quality.progress / 100.0)).toInt()
            var compressHeight = (originalBitmap!!.height * (sb_quality.progress / 100.0)).toInt()
            var compressImg = Bitmap.createBitmap(compressWidht, compressHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(compressImg)
            val rect = Rect(0, 0, compressWidht, compressHeight)
            canvas.drawBitmap(originalBitmap!!, null, rect, null)

            File(Environment.getExternalStorageDirectory().absolutePath + "/imgcompress/").mkdir()

            var compressFilePath = Environment.getExternalStorageDirectory().absolutePath + "/imgcompress/pixelsize_" + System.currentTimeMillis() + File(originalPath).getName()
            var outputStream = FileOutputStream(File(compressFilePath))
            compressImg!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val decodeFile = BitmapFactory.decodeFile(compressFilePath)
            iv_compress.setImageBitmap(decodeFile)
            showCompressImgInfo(compressFilePath, decodeFile)
            showToast("压缩成功", this)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initPickResult(requestCode, resultCode, data)
    }
}
