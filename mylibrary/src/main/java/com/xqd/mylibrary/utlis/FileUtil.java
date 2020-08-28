package com.xqd.mylibrary.utlis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/7.
 * 文件操作工具类
 */
public class FileUtil {
    public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().toString();
    public static FileUtil fileUtils;
    private Context context;
    /**
     * 主目录
     */
    public static String DIR_HOME;
    /**
     * 该文件用来在图库中屏蔽本应用的图片.
     */
    public static String DIR_NO_MEDIA_FILE;
    public static String DOWNLOADFILE;
    public static String RxSDCard_PATH;
    public static String RXCROPSDCARD_PATH;
    public static String VIDEO_PATH;
    public static String LOG_FILE;
    public static String DATA_CACHE;
    public static final int DIR_TYPE_HOME = 0x01;

    /**
     * 默认最小需要的空间
     */
    public static final long MIN_SPACE = 10 * 1024 * 1024;

    public static void init(Context context) {
        fileUtils = new FileUtil(context);
    }

    public FileUtil(Context context) {
        this.context = context;
        DIR_HOME = EXTERNAL_STORAGE + "/" + AppUtil.getAppName(context);
        DIR_NO_MEDIA_FILE = DIR_HOME + "/.nomedia";
        DOWNLOADFILE = DIR_HOME + "/download";
        RxSDCard_PATH = DIR_HOME + "/Rximg";
        RXCROPSDCARD_PATH = DIR_HOME + "/Rximg_crop";
        VIDEO_PATH = DIR_HOME + "/video";
        LOG_FILE = DIR_HOME + "/logs";
        DATA_CACHE = DIR_HOME + "/data";
    }

    /**
     * 通过类型获取目录路径
     *
     * @param type
     * @return
     */
    public static String getPathByType(int type) {
        String dir = "/";
        String filePath;

        switch (type) {
            case DIR_TYPE_HOME:
                filePath = DIR_HOME;
                break;

            default:
                filePath = "";
                break;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        if (file.exists()) {
            if (file.isDirectory()) {
                dir = file.getPath();
            }
        } else {
            // 文件没创建成功，可能是sd卡不存在，但是还是把路径返回
            dir = filePath;
        }

        return dir + "/";
    }

    /**
     * 判断存储空间是否足够,默认需要 \
     *
     * @return
     */
    public static boolean hasEnoughSpace() {
        return hasEnoughSpace(MIN_SPACE);
    }

    /**
     * 判断存储空间是否足够
     *
     * @param needSize
     * @return
     */
    public static boolean hasEnoughSpace(float needSize) {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());

            long blockSize;
            long availCount;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = sf.getBlockSizeLong();
                availCount = sf.getAvailableBlocksLong();
            } else {
                blockSize = sf.getBlockSize();
                availCount = sf.getAvailableBlocks();
            }

            long restSize = availCount * blockSize;
            if (restSize > needSize) {
                return true;
            }
        }
        return false;
    }

    /**
     * SdCard是否存在
     *
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 安卓内部存储剩余容量(MB)
     *
     * @return
     */
    public static double hasDataDirectoryEnoughSpace() {
        File path = Environment.getDataDirectory();
        StatFs sf = new StatFs(path.getPath());

        long blockSize = sf.getBlockSizeLong();
        long availCount = sf.getAvailableBlocksLong();

        long restSize = availCount * blockSize;
        return (double) restSize / 1048576.0D;
    }

    /**
     * 安卓sd卡剩余容量(MB)
     *
     * @return
     */
    public static double hasStorageDirectoryEnoughSpace() {
        if (isSDCardExist()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());

            long blockSize = sf.getBlockSizeLong();
            long availCount = sf.getAvailableBlocksLong();

            long restSize = availCount * blockSize;
            return (double) restSize / 1048576.0D;
        }
        return 0d;
    }

    /**
     * 生成文件路径
     *
     * @param dirType  文件路径类型
     * @param fileName 文件名，需带后缀
     * @return
     */
    public static String createPath(int dirType, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "temp";
        }

        fileName = fileName.replace('/', '_').replace(':', '_').replace("?", "_");

        String filePath = getPathByType(dirType) + File.separator + fileName;

        File file = new File(filePath);
        // 如果文件存在则先删除
        if (file.exists()) {
            file.delete();
        }

        return filePath;
    }

    /**
     * 生成文件
     *
     * @param dirType  文件路径类型
     * @param fileName 文件名，需带后缀
     * @return
     */
    public static File createFile(int dirType, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "temp";
        }

        fileName = fileName.replace('/', '_').replace(':', '_').replace("?", "_");
        String filePath = getPathByType(dirType) + File.separator + fileName;

        File file = new File(filePath);

        try {
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            // ignore
            return null;
        }

        return file;
    }

    /**
     * Delete file or folder.
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);

            if (file.isDirectory()) {
                // 处理目录
                File files[] = file.listFiles();
                if (null != files && files.length > 0) {
                    for (File f : files) {
                        deleteFile(f.getAbsolutePath());
                    }
                    return true;
                } else {
                    // 目录下没有文件或者目录，删除
                    return file.delete();
                }

            } else {
                // 如果是文件，删除
                return file.delete();
            }
        }
        return false;
    }

    /**
     * Delete file or folder.
     *
     * @param deleteFile
     * @return
     */
    public static boolean deleteFile(File deleteFile) {
        if (deleteFile != null) {
            if (!deleteFile.exists()) {
                return true;
            }

            if (deleteFile.isDirectory()) {
                // 处理目录
                File[] files = deleteFile.listFiles();
                if (null != files) {
                    for (File file : files) {
                        deleteFile(file.getAbsolutePath());
                    }
                }
            }

            if (!deleteFile.isDirectory()) {
                // 如果是文件，删除
                return deleteFile.delete();

            } else {
                // 目录下没有文件或者目录，删除
                File[] files = deleteFile.listFiles();
                if (null != files && files.length == 0) {
                    return deleteFile.delete();
                }
            }
        }

        return false;
    }


    public static void inputStreamToFile(InputStream is, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[3072];

            while ((bytesRead = is.read(buffer, 0, 3072)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 避免图片放入到图库中（屏蔽其他软件扫描）.
     */
    public static void hideMediaFile() {
        File file = new File(DIR_NO_MEDIA_FILE);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件
     */

    public static void cretFile() {
        File file = new File(RXCROPSDCARD_PATH);
        File file2 = new File(RxSDCard_PATH);
        File file3 = new File(VIDEO_PATH);
        File file4 = new File(DOWNLOADFILE);
        File file5 = new File(DATA_CACHE);
        File file6 = new File(LOG_FILE);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!file3.exists()) {
            file3.mkdirs();
        }
        if (!file4.exists()) {
            file4.mkdirs();
        }
        if (!file5.exists()) {
            file5.mkdirs();
        }
        if (!file6.exists()) {
            file6.mkdirs();
        }

    }

    public static boolean isfile(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    public static long getFileSize(File file) {
        try {
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    //根据图片地址获取图片名字
    public static String getFileName(String url) throws UnsupportedEncodingException {
        String filename = url.substring(url.lastIndexOf('/') + 1);//根据url的名字截取文件名
        if (filename == null || "".equals(filename.trim()))
            filename = UUID.randomUUID() + ".tmp";//  随机获取文件名
        filename = URLDecoder.decode(filename, "utf-8");
        return filename;
    }

    //通知系统图库
    public static void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }

    public static final int FILE_TYPE_FOR_JPG = 1;
    public static final int FILE_TYPE_FOR_PNG = 2;
    public static final int FILE_TYPE_FOR_MP4 = 3;

    public static int getFileType(String name) {
        int type = FILE_TYPE_FOR_JPG;
        if (TextUtils.isEmpty(name)) {
            return type;
        } else {
            String sub_st = name.substring(name.lastIndexOf(".") + 1, name.length());
            if (sub_st.equals("png")) {
                type = FILE_TYPE_FOR_PNG;
            } else if (sub_st.equals("mp4")) {
                type = FILE_TYPE_FOR_MP4;

            }
        }

        return type;
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }

            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }

    /**
     * copy file
     *
     * @param oldPath String
     * @param newPath String
     * @return boolean
     */
    public static void copyFileFromPath(String oldPath, String newPath) throws IOException {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("copy failed");
            e.printStackTrace();
        }
    }

    /**
     * Copy File from InputStream
     *
     * @param is      InputStream
     * @param newPath String
     * @return boolean
     */
    public static void copyFileFromInputStream(InputStream is, String newPath) throws IOException {
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            if (is != null) {
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = is.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                is.close();
            }
        } catch (Exception e) {
            System.out.println("Copy Failed");
            e.printStackTrace();

        } finally {
            try {
                is.close();
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param oldPath String
     * @param newPath String
     * @return boolean
     * @deprecated Copy all the files in folder
     */
    public void copyFolder(String oldPath, String newPath) throws IOException {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }
}
