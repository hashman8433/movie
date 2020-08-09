package com.free.video.service.executor;

import com.free.VideoApplication;
import com.free.video.dao.VideoFileDao;
import com.free.video.model.VideoFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.data.domain.Example;
import org.springframework.orm.hibernate5.SessionHolder;

import java.util.Date;
import java.util.Optional;
import java.util.Queue;

@Slf4j
public class FileSaveExecutor implements Runnable{

    public FileSaveExecutor(Queue<VideoFile> fileQuene) {
        this.fileQuene = fileQuene;
    }

    private Queue<VideoFile> fileQuene;

    @Override
    public void run() {
        VideoFileDao videoFileDao = VideoApplication.getBean(VideoFileDao.class);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        VideoFile videoFile;
        while ((videoFile = fileQuene.poll()) != null) {
            final String fileName = videoFile.getFileName();
            VideoFile file = (VideoFile) ReflectUtils.newInstance(VideoFile.class);
            file.setFileName(fileName);

            if (videoFileDao.findOne(Example.of(file)).isPresent()) {
                continue;
            }
            Date nowDate = new Date();

            videoFile.setCreateTime(nowDate);
            videoFile.setUpdateTime(nowDate);

            videoFileDao.save(videoFile);
            log.info("保存文件: " + videoFile);

        }

    }
}
