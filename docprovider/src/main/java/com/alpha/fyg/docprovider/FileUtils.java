package com.alpha.fyg.docprovider;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private FileUtils() {
    }

    /**
     * 读取txt文件，将其转化为列表，每一行为一个item
     *
     * @param path 文件路径
     * @return 返回读取的结果集
     */
//    public static List<String> readTxt(String path) {
//        List<String> list = new ArrayList<>();
////        File file = new File(path);
//
//        File file = new File(getExternalStorageDirectory(), path);
//        if (!file.exists()) {
//            return null;
////            Toast.makeText(OffLineLoginActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
//        } else {
//            InputStreamReader isr = null;
//            BufferedReader br = null;
//            try {
//                isr = new InputStreamReader(new FileInputStream(file), "GBK");
//                br = new BufferedReader(isr);
//
//                //str = "";
//                String mimeTypeLine;
//                while ((mimeTypeLine = br.readLine()) != null) {
//                    //str = str+mimeTypeLine;
//                    list.add(mimeTypeLine);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if(isr!=null){
//                    try {
//                        isr.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//               if (br!=null){
//                   try {
//                       br.close();
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                   }
//               }
//            }
//            return list;
//        }
//
//
//    }


//****系统文件目录**********************************************************************************************

    /**
     * @return 程序系统文件目录
     */
    public static String getFileDir(Context context) {
        return String.valueOf(context.getFilesDir());
    }

    /**
     * @param context    上下文
     * @param customPath 自定义路径
     * @return 程序系统文件目录绝对路径
     */
    public static String getFileDir(Context context, String customPath) {
        String path = context.getFilesDir() + formatPath(customPath);
        mkdir(path);
        return path;
    }

//****系统缓存目录**********************************************************************************************

    /**
     * @return 程序系统缓存目录
     */
    public static String getCacheDir(Context context) {
        return String.valueOf(context.getCacheDir());
    }

    /**
     * @param context    上下文
     * @param customPath 自定义路径
     * @return 程序系统缓存目录
     */
    public static String getCacheDir(Context context, String customPath) {
        String path = context.getCacheDir() + formatPath(customPath);
        mkdir(path);
        return path;
    }

//****Sdcard文件目录**********************************************************************************************

    /**
     * @return 内存卡文件目录
     */
    public static String getExternalFileDir(Context context) {
        return String.valueOf(context.getExternalFilesDir(""));
    }

    /**
     * @param context    上下文
     * @param customPath 自定义路径
     * @return 内存卡文件目录
     */
    public static String getExternalFileDir(Context context, String customPath) {
        String path = context.getExternalFilesDir("") + formatPath(customPath);
        mkdir(path);
        return path;
    }

//****Sdcard缓存目录**********************************************************************************************

    /**
     * @return 内存卡缓存目录
     */
    public static String getExternalCacheDir(Context context) {
        return String.valueOf(context.getExternalCacheDir());
    }

    /**
     * @param context    上下文
     * @param customPath 自定义路径
     * @return 内存卡缓存目录
     */
    public static String getExternalCacheDir(Context context, String customPath) {
        String path = context.getExternalCacheDir() + formatPath(customPath);
        mkdir(path);
        return path;
    }
    //****SDCard根目录****************************************************************************************************

    /**
     * @return SDCard根目录
     */
    public static String getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * @param customPath 自定义路径
     * @return SDCard 路径
     */
    public static String getExternalStorageDirectory(String customPath) {
        String path = Environment.getExternalStorageDirectory() + formatPath(customPath);
        mkdir(path);
        return path;
    }
//****公共文件夹**********************************************************************************************

    /**
     * @return 公共下载文件夹
     */
    public static String getPublicDownloadDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }


//****相关工具**********************************************************************************************

    /**
     * 创建文件夹
     *
     * @param DirPath 文件夹路径
     */
    public static void mkdir(String DirPath) {
        File file = new File(DirPath);
        if (!(file.exists() && file.isDirectory())) {
            file.mkdirs();
        }
    }

    /**
     * 格式化文件路径
     * 示例：  传入 "sloop" "/sloop" "sloop/" "/sloop/"
     * 返回 "/sloop"
     */
    private static String formatPath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        while (path.endsWith("/")) {
            path = new String(path.toCharArray(), 0, path.length() - 1);
        }
        return path;
    }

    /**
     * @return 存储卡是否挂载(存在)
     */
    public static boolean isMountSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取所有SD卡根目录
     *
     * @return
     */
    public static List<String> getStoragePath(Context mContext) {
        List<String> list = new ArrayList<>();
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
//                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
//                Log.e("----------","-----------"+path);
                list.add(path);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 复制文件
     *
     * @param src        源文件
     * @param targetPath 目标文件夹
     */
    public static boolean fileChannelCopy(File src, String targetPath) {

        boolean result = false;

        if (src.isFile() && src.exists()) {

            Log.e("FileUtils", targetPath);
            File target = new File(targetPath, src.getName());

            if (target.exists()) {

                target.delete(); // delete file
                try {
                    target.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;
            Log.e("FileUtils", "A");
            try {
                fi = new FileInputStream(src);
                fo = new FileOutputStream(target);
                Log.e("FileUtils", "B");
                //得到对应的文件通道
                in = fi.getChannel();
                //得到对应的文件通道
                out = fo.getChannel();
                Log.e("FileUtils", "C");
                //连接两个通道，并且从in通道读取，然后写入out通道
                in.transferTo(0, in.size(), out);
                result = true;
                Log.e("FileUtils", "test:" + 123);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fi != null) {
                    try {
                        fi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fo != null) {
                    try {
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        return result;

    }

    /**
     * 读取图片信息list
     *
     * @param path 图片信息路径
     * @return 返回图片信息list
     */
    public static List<String> readImageInfo(String path) {
        List<String> imageInfo = null;

        File file = new File(path);
        if (file.exists()) {
            imageInfo = new ArrayList<>();
            // 建立一个输入流对象reader
            InputStreamReader reader = null;
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            BufferedReader br = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file), "GBK");

                br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    // 一次读入一行数据
                    imageInfo.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return imageInfo;
    }


}
