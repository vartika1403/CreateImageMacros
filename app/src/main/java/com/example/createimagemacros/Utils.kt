package com.example.createimagemacros

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

// Retrieving the url to share
 fun getImageToShare(bitmap: Bitmap, context: Context): Uri? {
    val imagefolder = File(context.cacheDir, "images")
    var uri: Uri? = null
    try {
        imagefolder.mkdirs()
        val file = File(imagefolder, "shared_image.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
        uri = FileProvider.getUriForFile(context, "com.anni.shareimage.fileprovider", file)
        context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } catch (e: Exception) {
        Toast.makeText(context, "Error while retrieving the url" + e.message, Toast.LENGTH_LONG).show()
    }
    return uri
}

// change random background color
 fun changeBackGroundColor(view:View) {
    val random = Random
    val color = Color.argb(255, random.nextInt(256), random.nextInt(256),
        random.nextInt(256))
    view.setBackgroundColor(color)
}

fun getBitmapFromView(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(
        view.width, view.height, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

