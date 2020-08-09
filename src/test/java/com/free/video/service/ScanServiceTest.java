package com.free.video.service;

import com.free.common.utils.RegexUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ScanServiceTest {

    @Test
    public void scanVideo() throws InterruptedException {
        final String filePath = "E:\\迅雷下载";

        final Queue fileQuene = new ArrayBlockingQueue(100);

        ExecutorService findFileExecutor = Executors.newSingleThreadExecutor();
        ExecutorService saveFileExecutor = Executors.newSingleThreadExecutor();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        findFileExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    getAllFileName(filePath);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }

            AtomicInteger num = new AtomicInteger(0);
            /**
             * 获取某个文件夹下的所有文件
             *
             * @param path 文件夹的路径
             * @return
             */
            public void getAllFileName(String path) throws InterruptedException {

                boolean flag = false;
                File file = new File(path);
                File[] tempList = file.listFiles();

                for (int i = 0; i < tempList.length; i++) {
                    File tempFile = tempList[i];
                    if (tempFile.isFile()) {
//                        if(tempFile.getName();
                        String fileName = tempFile.getName();
                        System.out.println("输入文件 " + fileName + " num = " + num.addAndGet(1));

                        boolean offerFail = true;
                        while (offerFail) {
                            offerFail = ! fileQuene.offer(fileName);
                            if (offerFail) {
                                Thread.sleep(5000);
                            }
                        }
                    }
                    if (tempFile.isDirectory()) {
                        getAllFileName(tempList[i].getAbsolutePath());
                    }
                }
                return;
            }
        });

        countDownLatch.await();

    }

    @Test
    public void testRegex() {


        RegexUtils.isMatch("^.*.(mp4|avi)$", "asdfas.mp4");
        RegexUtils.isMatch("^.*.(mp4|avi)$", "asdfas.avi");
    }


}