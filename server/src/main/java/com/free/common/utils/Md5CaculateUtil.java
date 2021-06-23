package com.free.common.utils;

import com.qiniu.util.Hex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

@Slf4j
public class Md5CaculateUtil {


    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String getMD5(File file) {
        log.info("开始 获取文件md5值 [绝对路径] {}", file.getAbsolutePath());
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }

            String md5Str = new String(Hex.encodeHex(MD5.digest()));
            log.info("完成 获取文件md5值 [绝对路径] {} [md5] {}", file.getAbsolutePath(), md5Str);
            return md5Str;
        } catch (Exception e) {
            log.info("异常 获取文件md5值 [绝对路径] {} [error] {}", file.getAbsolutePath(), e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 求一个字符串的md5值
     * @param target 字符串
     * @return md5 value
     */
    public static String MD5(String target) {
        return DigestUtils.md5Hex(target);
    }

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        File file = new File("G:\\个人合集\\桥本有菜\\FSDSS-194.mp4");
        String md5 = getMD5(file);
        long endTime = System.currentTimeMillis();
        System.out.println("MD5:" + md5 + "\n 耗时:" + ((endTime - beginTime) / 1000) + "s");
    }


}
