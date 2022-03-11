package cc.banzhi.runmonitor.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;


/**
 * @program: ZRunMonitor
 * @description: 文件管理类
 * @author: zoufengli01
 * @create: 2022/3/10 9:33 下午
 **/
public class FileUtil {
    // 文件大小单位
    public static final int SIZETYPE_B = 1; // 获取文件大小单位为B
    public static final int SIZETYPE_KB = 2; // 获取文件大小单位为KB
    public static final int SIZETYPE_MB = 3; // 获取文件大小单位为MB
    public static final int SIZETYPE_GB = 4; // 获取文件大小单位为GB

    /**
     * 获取文件/文件夹的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        long blockSize = 0;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    blockSize = getDirSize(file);
                } else {
                    blockSize = getFileSize(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算文件/文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        long blockSize = 0;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    blockSize = getDirSize(file);
                } else {
                    blockSize = getFileSize(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小（B）
     *
     * @param file 指定文件
     */
    public static long getFileSize(File file) {
        long size = 0;
        FileInputStream fis = null;
        try {
            if (file != null && file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                size = fis.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹大小（B）
     *
     * @param files 指定文件夹
     */
    public static long getDirSize(File files) {
        long size = 0;
        try {
            if (files != null && files.exists()) {
                File[] fList = files.listFiles();
                if (fList != null) {
                    for (File file : fList) {
                        if (file.isDirectory()) {
                            size = size + getDirSize(file);
                        } else {
                            size = size + getFileSize(file);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    // 获取文件或文件夹大小
    public static synchronized long getFileSize2(File file) {
        long size = 0;
        if (file != null && file.exists()) {
            if (file.isFile()) {
                size = getFileSize(file);
            } else {
                size = getDirSize(file);
            }
        }
        return size;
    }

    /**
     * 转换文件大小（取最大单位）（保留两位小数）
     *
     * @param fileSize 文件大小
     */
    public static String formatFileSize(long fileSize) {
        String fileSizeStr = null;
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileSize <= 0) {
                fileSizeStr = "0 B";
            } else if (fileSize < 1024) {
                fileSizeStr = df.format((double) fileSize) + " B";
            } else if (fileSize < 1048576) {
                fileSizeStr = df.format((double) fileSize / 1024) + " KB";
            } else if (fileSize < 1073741824) {
                fileSizeStr = df.format((double) fileSize / 1048576) + " MB";
            } else {
                fileSizeStr = df.format((double) fileSize / 1073741824) + " GB";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSizeStr;
    }

    /**
     * 转换文件大小，指定转换的单位（保留两位小数）
     *
     * @param fileSize 文件大小
     * @param sizeType 文件大小单位
     */
    public static double formatFileSize(long fileSize, int sizeType) {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            double fileSizeLong = 0;
            switch (sizeType) {
                case SIZETYPE_B:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize));
                    break;
                case SIZETYPE_KB:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize / 1024));
                    break;
                case SIZETYPE_MB:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize / 1048576));
                    break;
                case SIZETYPE_GB:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize / 1073741824));
                    break;
                default:
                    break;
            }
            return fileSizeLong;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取本应用内部缓存大小（B）
     *
     * @param context 上下文对象
     */
    public static long getTotalCacheSize(Context context) {
        long cacheSize = 0;
        try {
            cacheSize = getDirSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getDirSize(context.getExternalCacheDir());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheSize;
    }

    /**
     * 获取本应用内部缓存大小（格式化）
     *
     * @param context 上下文对象
     */
    public static String getFormatTotalCacheSize(Context context) {
        try {
            long cacheSize = getDirSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getDirSize(context.getExternalCacheDir());
            }
            return formatFileSize(cacheSize);
        } catch (Exception e) {
            return null;
        }
    }
}