package com.example.myfbpic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.myfbpic.Constants;
import com.example.myfbpic.MyPicApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static String writeBitmapFile(final Bitmap bitmap, final PictureTypeEnum pictureType) throws IOException {
        String filePath;
        if (isExternalStorageAvailable()) {
            filePath = getExternalStoragePathToPicture(pictureType);
        } else {
            filePath = getInternalStoragePathToPicture(pictureType);
        }

        writeBitmapFileToStorage(bitmap, filePath);

        return filePath;
    }

    public static boolean removeFilesForStorage() {
        File fileOriginal;
        File fileBlurred;

        if (isExternalStorageAvailable()) {
            fileOriginal = new File(getExternalStoragePathToPicture(PictureTypeEnum.Original));
            fileBlurred = new File(getExternalStoragePathToPicture(PictureTypeEnum.Blurred));
        } else {
            fileOriginal = new File(getInternalStoragePathToPicture(PictureTypeEnum.Original));
            fileBlurred = new File(getInternalStoragePathToPicture(PictureTypeEnum.Blurred));
        }

        return (fileOriginal.delete() && fileBlurred.delete());
    }

    private static String getExternalStoragePathToPicture(final PictureTypeEnum pictureType) {
        String filePath;
        String baseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.APP_DIR_NAME;
        File appDirectory = new File(baseFolderPath);
        appDirectory.mkdirs();


        if (PictureTypeEnum.Original.equals(pictureType)) {
            filePath = baseFolderPath + "original.jpg";
        } else {
            filePath = baseFolderPath + "blurred.jpg";
        }

        return filePath;
    }

    private static String getInternalStoragePathToPicture(final PictureTypeEnum pictureType) {
        File baseFolderPath = MyPicApplication.getContext().getDir("pics", Context.MODE_PRIVATE);
        File file;

        if (PictureTypeEnum.Original.equals(pictureType)) {
            file = new File(baseFolderPath, "original.jpg");
        } else {
            file = new File(baseFolderPath, "blurred.jpg");
        }

        return file.getAbsolutePath();
    }

    private static void writeBitmapFileToStorage(final Bitmap bitmap, final String filePath) throws IOException {
        FileOutputStream stream = new FileOutputStream(filePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        stream.flush();
        stream.close();
    }

    private static boolean isExternalStorageAvailable() {
        boolean externalStorageAvailable;
        boolean externalStorageWritable;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            externalStorageAvailable = externalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            externalStorageAvailable = true;
            externalStorageWritable = false;
        } else {
            externalStorageAvailable = externalStorageWritable = false;
        }

        return (externalStorageAvailable && externalStorageWritable);
    }

}
