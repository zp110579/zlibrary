package com.zee.scan.zxing.encode

import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.zee.utils.UIUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * 生成二维码
 */
object QRCodeUtil {
    fun getBitmapByRes(resId: Int): Bitmap? {
        return BitmapFactory.decodeResource(UIUtils.getResources(), resId)
    }

    private fun getBitmapByDrawable(drawable: Drawable?): Bitmap? {
        drawable?.let {
            return Bitmap.createBitmap(
                    it.intrinsicWidth,
                    it.intrinsicHeight,
                    if (it.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
            )
        }
        return null
    }

    fun createQRCode(content: String, widthPix: Int, heightPix: Int, iconRes: Int): Bitmap? {
        return createQRCode(content, widthPix, heightPix, getBitmapByRes(iconRes))
    }

    fun createQRCode(content: String, widthPix: Int, heightPix: Int): Bitmap? {
        return createQRCode(content, widthPix, heightPix, null)
    }

    fun createQRCode2(content: String, widthPix: Int, heightPix: Int, iconDrawable: Drawable?): Bitmap? {
        return createQRCode(content, widthPix, heightPix, getBitmapByDrawable(iconDrawable))
    }

    /**
     * 将本地图片文件转换成可解码二维码的 Bitmap。为了避免图片太大，这里对图片进行了压缩。感谢 https://github.com/devilsen 提的 PR
     *
     * @param picturePath 本地图片文件路径
     */
    fun getDecodeAbleBitmap(picturePath: String): Bitmap? {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(picturePath, options)
            var sampleSize = options.outHeight / 400
            if (sampleSize <= 0) {
                sampleSize = 1
            }
            options.inSampleSize = sampleSize
            options.inJustDecodeBounds = false

            return BitmapFactory.decodeFile(picturePath, options)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 创建二维码
     *
     * @param content   content
     * @param widthPix  widthPix
     * @param heightPix heightPix
     * @param logoBm    logoBm
     * @return 二维码
     */
    fun createQRCode(content: String, widthPix: Int, heightPix: Int, logoBm: Bitmap?): Bitmap? {
        try {
            if (TextUtils.isEmpty(content)) {
                return null
            }
            // 配置参数  // 容错级别
            val hints = mapOf(EncodeHintType.CHARACTER_SET to "utf-8", EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000.toInt()
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff.toInt()
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            var qrBitmap: Bitmap? = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            qrBitmap?.let { bitmap ->
                bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
                logoBm?.let { logoBitmap ->
                    qrBitmap = addIcon2QrCode(bitmap, logoBitmap)
                }
                //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
                //return bmpConpressor.compressBitmap(bitmap)
            }
            return qrBitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private fun addIcon2QrCode(src: Bitmap, logo: Bitmap): Bitmap? {
        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height
        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }
        //logo大小为二维码整体大小的1/5
        val scaleFactor = srcWidth * 1.0f / 5f / logoWidth.toFloat()
        var bitmap: Bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(src, 0F, 0F, null)
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2F, srcHeight / 2F)
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2F, (srcHeight - logoHeight) / 2F, null)
            //canvas.save(Canvas.ALL_SAVE_FLAG)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            e.stackTrace
            return null
        }
        return bitmap
    }
}
