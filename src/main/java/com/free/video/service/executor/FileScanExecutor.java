package com.free.video.service.executor;

import com.free.common.config.Const;
import com.free.common.utils.RegexUtils;
import com.free.common.utils.SystemConfigUtils;
import com.free.video.model.SystemConfig;
import com.free.video.model.VideoFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;


// 获取扫描文件扫描器
@Slf4j
public class FileScanExecutor implements Runnable {

    public FileScanExecutor(String filePath, Queue<VideoFile> fileQuene) {
        this.fileQuene = fileQuene;
        this.filePath = filePath;
    }

    private String filePath;
    private Queue<VideoFile> fileQuene;


    @Override
    public void run() {
        try {
            getAllFileName(filePath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    AtomicInteger num = new AtomicInteger(0);
    /**
     * 获取某个文件夹下的所有文件
     *
     * @param path 文件夹的路径
     * @return
     */
    public void getAllFileName(final String path) throws InterruptedException {

        File file = new File(path);
        File[] tempList = file.listFiles();
        log.info("path: {} 下有{}个文件", path, tempList == null ? 0 : tempList.length);

        for (int i = 0; i < tempList.length; i++) {
            File tempFile = tempList[i];

            if (tempFile.isFile()) {
                String fileName = tempFile.getName();
                if (! RegexUtils.isMatch(SystemConfigUtils.getConfig("videoFileSuffix"), fileName)) {
                    // 非视频 文件不保存
                    continue;
                }

                log.info("找到文件 " + fileName + " num = " + num.addAndGet(1));

                boolean offerFail = true;
                while (offerFail) {

                    VideoFile videoFile = (VideoFile) ReflectUtils.newInstance(VideoFile.class);
                    videoFile.setFilePath(tempFile.getAbsolutePath());
                    videoFile.setFilePathWeb(tempFile.getAbsolutePath());
                    videoFile.setFileName(tempFile.getName());

                    videoFile.setTitle(tempFile.getName());
                    videoFile.setFileSize(String.valueOf(tempFile.getTotalSpace()));
                    videoFile.setScanStatus(Const.NOSCAN);
                    videoFile.setIsDelete(Const.NO_DELETE);

                    offerFail = !fileQuene.add(videoFile);
                    if (offerFail) {
                        Thread.sleep(5000);
                    }
                }
            }

            if (tempFile.isDirectory()) {
                log.info("{} 是文件夹", tempFile.getAbsolutePath());
                getAllFileName(tempList[i].getAbsolutePath());
            }
        }
        return;
    }
}
