package com.yun.common.utils;

/**
 * Created by hornsey on 2015/10/8.
 */


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremy.Lai
 * @version V1.0
 * @date 2011-9-27
 * @description SD card operate permissio
 */
public class FileUtil {

    /**
     * judge if have SD Card
     *
     * @return
     */
    public static boolean avaiableSDCard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * obtain SD Card root directory
     *
     * @return
     */
    public static File getSDCardRoot() {
        if (avaiableSDCard()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /**
     * obtain SD Card root directory
     *
     * @return
     */
    public static String getSDCardRootPath() {
        if (avaiableSDCard()) {
            return getSDCardRoot().getPath();
        }
        return null;
    }

    /**
     * obtain SD Card remain room
     *
     * @param type return unit：KB、MB、GB
     * @return
     */
    public static long getAvailableSize(String type) {
        if (avaiableSDCard()) {
            StatFs stat = new StatFs(getSDCardRootPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            if (!TextUtils.isEmpty(type)) {
                if (type.equals("KB")) {
                    return (availableBlocks * blockSize) / 1024;
                } else if (type.equals("MB")) {
                    return (availableBlocks * blockSize) / 1024 / 1024;
                } else if (type.equals("GB")) {
                    return (availableBlocks * blockSize) / 1024 / 1024 / 1024;
                }
            }
        }
        return -1;
    }

    /**
     * obtain SD Card room
     *
     * @param type return unit：MB、GB
     * @return
     */
    public static long getAllSize(String type) {
        if (avaiableSDCard()) {
            StatFs stat = new StatFs(getSDCardRootPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getBlockCount();
            if (!TextUtils.isEmpty(type)) {
                if (type.equals("MB")) {
                    return (availableBlocks * blockSize) / 1024;
                } else if (type.equals("GB")) {
                    return (availableBlocks * blockSize) / 1024 / 1024;
                }
            }
        }
        return -1;
    }

    /**
     * obtain file size
     *
     * @param folderPath floder path
     * @param type       return unit：B、KB、MB、GB
     * @return
     */
    public static double getFolderSize(String folderPath, String type) {
        long totalSize = getFolderLength(folderPath);
        System.out.println("folderPath = " + folderPath + " totalSize = " + totalSize);

        if (!TextUtils.isEmpty(type)) {
            if (type.equals("B")) {
                return (double) totalSize;
            } else if (type.equals("KB")) {
                return (double) totalSize / 1024;
            } else if (type.equals("MB")) {
                return (double) totalSize / 1024 / 1024;
            } else if (type.equals("GB")) {
                return (double) totalSize / 1024 / 1024 / 1024;
            }
        }
        return totalSize;
    }

    /**
     * obtain the length of file or folder
     *
     * @param file
     * @return
     */
    public static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();

        long total = 0;
        final File[] children = file.listFiles();

        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);

        return total;
    }

    /**
     * show file size in suitable format
     *
     * @param size
     * @return
     */
    public static String showSize(final long size) {
        if(size < 1024) {
            return size + "B";
        } else if (size < 1024*1024) {
            return size/1024 + "KB";
        } else if (size < 1024*1024*1024) {
            return size/(1024*1024) + "MB";
        } else if (size < 1024*1024*1024*1024) {
            return size/(1024*1024*1024) + "GB";
        } else {
            return size/(1024*1024*1024*1024) + "TB";
        }
    }

    /**
     * show file size in suitable format
     *
     * @param fileName
     * @return
     */
    public static String showSize(final String fileName) {
        long size = getTotalSizeOfFilesInDir(new File(fileName));
        if(size < 1024) {
            return size == 0 ? "0" : "1KB";
        } else if (size < 1024*1024) {
            return size/1024 + "KB";
        } else if (size < 1024*1024*1024) {
            return size/(1024*1024) + "MB";
        } else if (size < 1024*1024*1024*1024) {
            return size/(1024*1024*1024) + "GB";
        } else {
            return size/(1024*1024*1024*1024) + "TB";
        }
    }

    /**
     * copy file
     *
     * @param fromFile copied file, file in SD card absolute path
     * @param toFile   goal file, file in sd card absolute path
     */
    public static void copy(String fromFile, String toFile) {
        try {
            InputStream inputStream = new FileInputStream(fromFile);
            OutputStream outputStream = new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int readedLength = -1;
            while ((readedLength = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, readedLength);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * copy file
     *
     * @param fromFile copied file
     * @param toFile   goal file
     */
    public static void copy(File fromFile, File toFile) {
        try {
            InputStream inputStream = new FileInputStream(fromFile);
            OutputStream outputStream = new FileOutputStream(toFile);
            byte[] b = new byte[1024];
            int readedLength = -1;
            while ((readedLength = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, readedLength);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete floder
     *
     * @param folderPath String file path and name
     * @return boolean
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // delete all content in it
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // delete empty floder

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * establish floder
     *
     * @param path       file path ,head with "/"
     * @param folderName filename
     * @return floder absolute path
     */
    public static String createFolder(String path, String folderName) {
        if (avaiableSDCard()) {
            File dir = getSDCardRoot();
            String root = dir.getPath() + path + "/";
            dir = new File(root, folderName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * save Bitmap into SD card
     *
     * @param filePath   image in sd card absolute path(contain file name、postfix name)
     * @param fileBitmap image Bitmap
     * @param format     save
     *                   format（Bitmap.CompressFormat.PNG、Bitmap.CompressFormat.JPEG)
     * @return
     */
    public static boolean saveBitmap(String filePath, Bitmap fileBitmap,
                                     CompressFormat format) {
        if (!TextUtils.isEmpty(filePath)) {
            if (avaiableSDCard()) {
                File f = new File(filePath);
                try {
                    return doSave(new FileOutputStream(f), fileBitmap, format);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
            }
        } else {
        }
        return false;
    }

    /**
     * save file
     *
     * @param fileName
     * @param fileContent
     */
    public static boolean save(String fileName, String fileContent) {
        if (null != fileName && !"".equals(fileName)) {
            File file = new File(getSDCardRoot(), fileName);
            if (avaiableSDCard()) {
                try {
                    return doSave(new FileOutputStream(file), fileContent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
            }
        } else {
        }
        return false;
    }

    /**
     * save file
     *
     * @param fileName    file in sd card absolute to sd card root director path
     * @param fileContent
     */
    public static boolean save(String fileName, byte[] fileContent) {
        if (null != fileName && !"".equals(fileName)) {
            File file = new File(getSDCardRoot(), fileName);
            Log.v("FileUtil","SDCardFileUtil.save path:" + file.toString());
            if (avaiableSDCard()) {
                try {
                    return doSave(new FileOutputStream(file), fileContent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
            }
        } else {
        }
        return false;
    }

    /**
     * save file
     *
     * @param fileName    file in sd card absolute path
     * @param fileContent
     */
    public static boolean saveToAbsolutePath(String fileName, byte[] fileContent) {
        if (null != fileName && !"".equals(fileName)) {
            File file = new File(fileName);
            if (avaiableSDCard()) {
                try {
                    return doSave(new FileOutputStream(file), fileContent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
            }
        } else {
        }
        return false;
    }

    /**
     * read file from sd card
     *
     * @return
     * @throws IOException
     */
    public static String read(String fileName) throws IOException {
        String readContent = "";
        if (null != fileName && !"".equals(fileName)) {
            File file = new File(getSDCardRoot(), fileName);
            if (avaiableSDCard()) {
                if (file.exists()) {
                    try {
                        readContent = doRead(new FileInputStream(file));
                        return readContent;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } else {
            }
        } else {
        }

        return null;
    }

    /**
     * read file from sd card
     *
     * @return
     * @throws IOException
     */
    public static byte[] readToByte(String fileName) throws IOException {
        byte[] readContent;
        if (null != fileName && !"".equals(fileName)) {
            File file = new File(getSDCardRoot(), fileName);
            if (avaiableSDCard()) {
                if (file.exists()) {
                    try {
                        readContent = doReadToByte(new FileInputStream(file));
                        return readContent;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } else {
            }
        } else {
        }

        return null;
    }

    /**
     * read file from sd card
     *
     * @return
     * @throws IOException
     */
    public static byte[] readToByteFromAbsolutePath(String fileName)
            throws IOException {
        byte[] readContent;
        if (null != fileName && !"".equals(fileName)) {
            File file = new File(fileName);
            if (avaiableSDCard()) {
                if (file.exists()) {
                    try {
                        readContent = doReadToByte(new FileInputStream(file));
                        return readContent;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } else {
            }
        } else {
        }

        return null;
    }

    private static long getFolderLength(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists())
            return 0;
        if (!file.isDirectory())
            return 0;

        String[] tempList = file.list();
        File temp ;
        long totalLen = 0;
        for (int i = 0; i < tempList.length; i++) {
            if (folderPath.endsWith(File.separator)) {
                temp = new File(folderPath + tempList[i]);
            } else {
                temp = new File(folderPath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                totalLen += temp.length();
            }
            if (temp.isDirectory()) {
                totalLen += getFolderLength(folderPath + "/" + tempList[i]);
            }
        }
        return totalLen;
    }

    /**
     * delete all file in floder
     *
     * @param path String floder path
     */
    private static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists())
            return;
        if (!file.isDirectory())
            return;

        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// first delete file in floder
                delFolder(path + "/" + tempList[i]);// second delete empty floder
            }
        }
    }

    private static String doRead(FileInputStream fileInputStream)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len ;
        while ((len = fileInputStream.read(buffer)) != -1) {
            if (len >= buffer.length) {
                os.write(buffer);
            } else {
                os.write(buffer, 0, len);
            }
        }
        os.close();
        fileInputStream.close();
        return new String(os.toByteArray());
    }

    private static byte[] doReadToByte(FileInputStream fileInputStream)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fileInputStream.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        fileInputStream.close();
        return os.toByteArray();
    }

    private static boolean doSave(FileOutputStream fileOutputStream,
                                  String fileContent) {
        try {
            fileOutputStream.write(fileContent.getBytes());
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean doSave(FileOutputStream fileOutputStream,
                                  byte[] fileContent) {
        try {
            fileOutputStream.write(fileContent);
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean doSave(FileOutputStream fileOutputStream,
                                  Bitmap fileBitmap, CompressFormat format) {
        fileBitmap.compress(format, 100, fileOutputStream);
        try {
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存图片到指定位置
     *
     * @param context
     * @param bmp
     * @param fileName
     * @return
     */
    public static void saveBmpToPng(Context context, Bitmap bmp, String fileName) {
        try {
            fileName = fileName + ".png";

            Log.i("debug", "保存文件: " + fileName);

            FileOutputStream fileOut = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            bmp.compress(CompressFormat.PNG, 0, fileOut);
            fileOut.flush();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存bitmap到本地
     ****/

    public static void saveBitmapLocal(String filePath, String name,
                                       Bitmap bitmap, int quality) {
        FileOutputStream outputStream = null;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            outputStream = new FileOutputStream(filePath + name);
            bitmap.compress(CompressFormat.JPEG, quality, outputStream);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param path    文件名称及路径
     * @param bitmap
     * @param quality void
     * @Title: saveBitmapLocal
     * @Description: 保存bitmap到本地
     */
    public static void saveBitmapLocal(String path,
                                       Bitmap bitmap, int quality) {
        FileOutputStream outputStream = null;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            outputStream = new FileOutputStream(path);
            bitmap.compress(CompressFormat.JPEG, quality, outputStream);// 把数据写入
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @param path
     * @param bitmap
     * @param quality
     * @return File
     * @Title: saveBitmapToFile
     * @Description: bitMAP转为file
     */
    public static File saveBitmapToFile(String path,
                                        Bitmap bitmap, int quality) {
        FileOutputStream outputStream = null;
        File file = new File(path.substring(0, path.lastIndexOf("/") + 1));
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            outputStream = new FileOutputStream(path);
            bitmap.compress(CompressFormat.JPEG, quality, outputStream);// 把数据写入文件
            return new File(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    /**
     * @param path
     * @param bitmap
     * @return File
     * @Title: saveBitmapToFile
     * @Description: bitMAP转为file
     */
    public static File saveBitmapToFile(String path,
                                        Bitmap bitmap) {
        return saveBitmapToFile(path, bitmap, 90);

    }


    /**
     * 获取文件夹下所有文件夹的名称
     ****/
    public static List<String> getSubFolderNames(String folderPath) {
        List<String> fileNames = null;
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            fileNames = new ArrayList<String>();
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].exists() && files[i].isDirectory()) {
                fileNames.add(files[i].getName());
            }
        }
        return fileNames;
    }


    /**
     * 获取文件夹下所有文件为jpg的名称
     ****/
    public static List<String> getSubFolderJPGNames(String folderPath) {
        List<String> fileNames = null;
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            fileNames = new ArrayList<String>();
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].exists() && !files[i].isDirectory() && files[i].getName().substring(files[i].getName().lastIndexOf(".")).equals(".jpg")) {
                fileNames.add(files[i].getName());
            }

        }
        return fileNames;
    }


    /**
     * 获取文件夹下所有文件及文件的名称
     ****/
    public static List<String> getSubFolderAndFileNames(String folderPath) {
        List<String> fileNames = null;
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            fileNames = new ArrayList<String>();
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].exists()) {
                fileNames.add(files[i].getName());
            }

        }
        return fileNames;
    }


    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (avaiableSDCard()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                if (files[i].getName().equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }

    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
