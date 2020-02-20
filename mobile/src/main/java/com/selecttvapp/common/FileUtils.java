package com.selecttvapp.common;

import android.os.Environment;
import android.util.Log;

import com.selecttvapp.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/*Create a log file in internal storage...*/
public class FileUtils  {
    private static final int TEN_MB = 10 * 1024 * 1024;

    public static boolean writeToFile(String tag, String response,
                                      String name) {
        String BuildVersion = "";

        try {
            BuildVersion = "Build version:: " + BuildConfig.VERSION_NAME;
        } catch (Exception e) {

        }

        String Tag = "Request Url/Tag is :: " + tag;
        String currentTime = "TimeStamp is :: " + Calendar.getInstance().getTime().toString();
        String error = "Error/Response is :: " + response;
        response = "\n\n" + BuildVersion + "\n" + Tag + "\n" + currentTime + "\n" + error;

        File f = getOutputMediaFile(name);
        if (f == null)
            return false;


        if (f.length() > TEN_MB) {
            f.delete();
        }

        if (!f.exists()) {

            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        byte[] b = response.getBytes();
        try {
            FileOutputStream fos = new FileOutputStream(f, true);
            fos.write(b);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static File getOutputMediaFile(String title) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (!hasStorage(true)) {
            return null;
        }
        File mediaStorageDir = new File(Environment
                .getExternalStorageDirectory().toString() + "/.selectTv/");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("SelectTv", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile = new File(mediaStorageDir, title + ".txt");
        return mediaFile;
    }

    public static boolean checkFileExists(String title) {

        String mediaStorageDir = Environment.getExternalStorageDirectory()
                .toString() + "/.selectTv/";

        File mediaFile = new File(mediaStorageDir, title + ".txt");
        if (mediaFile != null) {
            if (mediaFile.exists())
                return true;
        }
        return false;
    }

    public static File getFileExists(String title) {
        String mediaStorageDir = Environment.getExternalStorageDirectory()
                .toString() + "/.selectTv/";
        File mediaFile = new File(mediaStorageDir, title + ".txt");
        return mediaFile;
    }

    static public boolean hasStorage(boolean requireWriteAccess) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                boolean writable = checkFsWritable();
                return writable;
            } else {
                return true;
            }
        } else if (!requireWriteAccess
                && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static boolean checkFsWritable() {

        String directoryName = Environment.getExternalStorageDirectory()
                .toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        return directory.canWrite();
    }


    /*Method to use delete log file from internal storage...*/
    public static void deleteLogFile() {
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + "/.selectTv/");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
