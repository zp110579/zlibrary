package com.zee.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.zee.log.ZLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageCompressUtils {


    /**
     * 只是质量的压缩不改变高度和宽度
     *
     * @param bit
     * @param quality
     * @return
     */
    public static Bitmap getJPGBitMap(Bitmap bit, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bm;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            inSampleSize *= 2;
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static File getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 1280f;//这里设置高度
        float ww = 800f;//这里设置宽度
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressJPGImage(bitmap, srcPath, 1000);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 获得文件的最大大小
     *
     * @param path
     * @param fixFileSize kb 压缩后的文件不能大于fileSize
     * @return
     */
    public static File compressImageBigSize(String path, int fixFileSize) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return compressJPGImage(bitmap, path, fixFileSize);
    }

    private static File compressJPGImage(Bitmap image, String path, int fileSize) {
        File oldFile = new File(path);
        File newFile = new File(oldFile.getParent() + "/" + "temp.jpg");
        try {
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;


        while (baos.toByteArray().length / 1024 > fileSize) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;//每次都减少10
        }
        try {
            OutputStream out = new FileOutputStream(newFile);
            out.write(baos.toByteArray());
            out.close();
            baos.close();
            image.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n文件压缩信息:");
        stringBuilder.append("\n");
        stringBuilder.append("原文件地址:" + oldFile.getAbsolutePath());
        stringBuilder.append("(" + oldFile.length() / 1024 + "KB)");
        stringBuilder.append("\n");
        stringBuilder.append("新文件地址:" + newFile.getAbsoluteFile());
        stringBuilder.append("(" + newFile.length() / 1024 + "KB)");
        ZLog.i(stringBuilder.toString());
        return newFile;
    }

    public void init(String path) {

    }
}
