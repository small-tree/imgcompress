package com.fanchain.imgcompress

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import kotlinx.android.synthetic.main.compress_imginfo_layout.*
import kotlinx.android.synthetic.main.pick_img_layout.*
import java.io.File
import java.util.ArrayList
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class MyDelegate {

    var originalPath = ""
    operator fun getValue(activity: Activity, property: KProperty<*>): String {
        return originalPath
    }

    operator fun setValue(activity: Activity, property: KProperty<*>, s: String) {
        originalPath = s
    }
}

class AcBitmapDelegate {
    var originalPath: Bitmap? = null
    operator fun getValue(activity: Activity, property: KProperty<*>): Bitmap {
        return originalPath!!
    }

    operator fun setValue(activity: Activity, property: KProperty<*>, s: Bitmap) {
        originalPath = s
    }
}

var Activity.originalPath: String by MyDelegate()
var Activity.originalBitmap: Bitmap by AcBitmapDelegate()

fun Activity.initPickView() {

    btn_pick.setOnClickListener {
        ImagePicker.getInstance().isMultiMode = true
        ImagePicker.getInstance().selectLimit = 1
        ImagePicker.getInstance().isShowCamera = true

        startActivityForResult(Intent(this, ImageGridActivity::class.java), 1)
    }
}

fun Activity.initPickResult(requestCode: Int, resultCode: Int, data: Intent?) {

    if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
        val images = data!!.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
        originalPath = images[0].path
        tv_image_path.setText(originalPath)
        originalBitmap = BitmapFactory.decodeFile(originalPath)
        iv_imageview.setImageBitmap(originalBitmap)
        showImgInfo(originalPath, originalBitmap!!)
    }

}


fun Activity.showImgInfo(path: String, bitmap: Bitmap) {
    val builder = StringBuilder()
    builder.append("文件大小：${File(path).length() / 1024} kb" + NEW_LINE)
    builder.append("width:" + bitmap.width + NEW_LINE)
    builder.append("height:" + bitmap.height + NEW_LINE)
    tv_image_info.setText(builder.toString())
}

fun Activity.showCompressImgInfo(path: String, bitmap: Bitmap) {
    val builder = StringBuilder()
    builder.append("文件大小：${File(path).length() / 1024} kb" + NEW_LINE)
    builder.append("width:" + bitmap.width + NEW_LINE)
    builder.append("height:" + bitmap.height + NEW_LINE)
    tv_compress_info.setText(builder.toString())
}

