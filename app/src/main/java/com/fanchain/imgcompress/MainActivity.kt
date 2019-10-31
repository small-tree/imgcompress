package com.fanchain.imgcompress

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Toast
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList
import kotlin.math.log


fun extLogE(msg: String) {
    Log.e("imgcompress", msg);
}

fun showToast(msg: String, context: Context) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

class MainActivity : AppCompatActivity() {

    val NEW_LINE = "\\\r\\\n"


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

        btn_pick.setOnClickListener {
            ImagePicker.getInstance().isMultiMode = true
            ImagePicker.getInstance().selectLimit = 1
            ImagePicker.getInstance().isShowCamera = true

            startActivityForResult(Intent(this, ImageGridActivity::class.java), 1)
        }


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
                showToast("压缩质量最低 1", this)
                return@setOnClickListener
            }


//            var compressFilePath = filesDir.getAbsolutePath() + "" + System.currentTimeMillis() + File(originalPath).getName()
            File(Environment.getExternalStorageDirectory().absolutePath + "/imgcompress/").mkdir()
            var compressFilePath = Environment.getExternalStorageDirectory().absolutePath + "/imgcompress/" + System.currentTimeMillis() + File(originalPath).getName()
            var format = Bitmap.CompressFormat.valueOf(findViewById<RadioButton>(rg_group.checkedRadioButtonId).text.toString())
            var compressImg = File(compressFilePath)
            if (compressImg != null && compressImg.exists()) {
                compressImg.delete()
            }
            compressImg.createNewFile()
            var outputStream = FileOutputStream(File(compressFilePath))
            originalBitmap!!.compress(format, sb_quality.progress, outputStream)
            outputStream.flush()
            outputStream.close()

            val decodeFile = BitmapFactory.decodeFile(compressFilePath)
            iv_compress.setImageBitmap(decodeFile)
            showCompressImgInfo(compressFilePath, decodeFile)
            showToast("压缩成功", this)
        }

    }

    var originalPath = ""
    var originalBitmap: Bitmap? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            val images = data!!.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
            originalPath = images[0].path
            tv_image_path.setText(originalPath)
            originalBitmap = BitmapFactory.decodeFile(originalPath)
            iv_imageview.setImageBitmap(originalBitmap)
            showImgInfo(originalPath, originalBitmap!!)
        }
    }

    fun showImgInfo(path: String, bitmap: Bitmap) {
        val builder = StringBuilder()
        builder.append("文件大小：${File(path).length() / 1024} kb" + NEW_LINE)
        builder.append("width:" + bitmap.width + NEW_LINE)
        builder.append("height:" + bitmap.height + NEW_LINE)
        tv_image_info.setText(builder.toString())
    }

    fun showCompressImgInfo(path: String, bitmap: Bitmap) {
        val builder = StringBuilder()
        builder.append("文件大小：${File(path).length() / 1024} kb" + NEW_LINE)
        builder.append("width:" + bitmap.width + NEW_LINE)
        builder.append("height:" + bitmap.height + NEW_LINE)
        tv_compress_info.setText(builder.toString())
    }


}
