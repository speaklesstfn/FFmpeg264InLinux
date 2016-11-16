package com.sddd.tfn.ffmpeg264inlinux;

import android.text.TextUtils;

import java.io.File;

/**
 * 文件工具类
 * Created by tfn on 16-11-16.
 */

public class FileUtils {

    /**
     * 重置文件，如果文件已存在，则删除
     *
     * @param filePath 文件路径
     */
    public static void resetFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        boolean isExist = isFileExist(filePath);
        if (isExist) {
            deleteFile(filePath);
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 文件是否存在
     */
    private static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 文件是否成功删除
     */
    private static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return true;
        }

        boolean result = true;

        try {
            File file = new File(filePath);
            if (file.exists()) {
                result = file.delete();
            } else {
                result = true;
            }
        } catch (Exception e) {
            result = false;
        }

        return result;
    }
}
