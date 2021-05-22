package com.free.video.service;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import com.free.base.BaseSpringBootTest;
import com.free.common.utils.RegexUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

public class ScanServiceTest extends BaseSpringBootTest {


    @Value("${filePath:''}")
    private String filePath;

    @Test
    public void scanVideo() throws InterruptedException {

        final Queue fileQuene = new ArrayBlockingQueue(10000);

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
                        System.out.println("找到文件 " + tempFile.getAbsolutePath() +
                                " num = " + num.addAndGet(1));

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

        System.out.println("E:\\迅雷下载\\NASH331\\nash00331.mp4".replace("E:\\迅雷下载", ""));
        System.out.println("E:/迅雷下载");
        System.out.println("E:/迅雷下载".replaceAll("/", "\\\\"));

        RegexUtils.isMatch("^.*.(mp4|avi)$", "asdfas.mp4");
        RegexUtils.isMatch("^.*.(mp4|avi)$", "asdfas.avi");
    }


}