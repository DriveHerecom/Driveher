package com.yukti.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Vj on 12/16/2015.
 */
public class CacheManager {

    //    private static final long MAX_SIZE = 5242880L; // 5MB
    private static final long MAX_SIZE = 15728640L; // 15MB
    private Context mContext;


    public CacheManager(Context context) {
        mContext = context;
        //File cacheDir = getAppCacheDir();
//        long size = getDirSize(cacheDir);
////        //long newSize = data.length + size;
//        if (size > MAX_SIZE) {
//            deleteDir();
//        }

    }

    public void cacheData(byte[] data, String name) throws IOException {
        File cacheDir = getAppCacheDir();
        File file = new File(cacheDir, name);
        FileOutputStream os = new FileOutputStream(file);
        try {
            os.write(data);
        } finally {
            os.flush();
            os.close();
        }
    }

    public File getAppCacheDir() {
        File appCacheDir = null;
        try {
            if (isExternalStorageAvailable() && isExternalStorageWritable()) {
                appCacheDir = mContext.getExternalCacheDir();
            } else {
                appCacheDir = mContext.getCacheDir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(appCacheDir != null && !appCacheDir.exists()){
            if(appCacheDir.mkdir())
                return appCacheDir;
            else
                return null;
        } else
            return appCacheDir;
    }

    private boolean isExternalStorageAvailable() {
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public byte[] retrieveData(Context context, String name) throws IOException {

        File cacheDir = getAppCacheDir();
        File file = new File(cacheDir, name);

        if (!file.exists()) {
            // Data doesn't exist
            return null;
        }

        byte[] data = new byte[(int) file.length()];
        FileInputStream is = new FileInputStream(file);
        try {
            is.read(data);
        } finally {
            is.close();
        }

        return data;
    }

    private static void cleanDir(File dir, long bytes) {

        long bytesDeleted = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            bytesDeleted += file.length();
            file.delete();

            if (bytesDeleted >= bytes) {
                break;
            }
        }
    }

    public void deleteDir() {
        File cacheDir = getAppCacheDir();
        if (cacheDir.delete()) {
            Log.e("Delete Cache", "Cache directory was deleted successfully.");
        } else {
            Log.e("Error Delete Cache", "Cache directory was not deleted successfully.");
        }
    }

    private static long getDirSize(File dir) {

        long size = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            }
        }

        return size;
    }
}
