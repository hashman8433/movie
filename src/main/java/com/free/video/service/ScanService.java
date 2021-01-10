package com.free.video.service;

import com.free.video.dao.VideoFileDao;
import com.free.video.model.VideoFile;
import com.free.video.service.executor.FileSaveExecutor;
import com.free.video.service.executor.FileScanExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
public class ScanService {

    @Autowired
    private VideoFileDao videoFileDao;

    private final ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

    // @Value("${filePath:''")
    // public String filePath;

    @Autowired
    private SystemConfigService systemConfigService;

    public void scanVideo()  {
        try {
            // 深度优先  todo 队列长度过长没有做控制
            Queue<VideoFile> fileQuene =
                    new LinkedBlockingQueue<>();
            String filePath = systemConfigService.getValueByCode("filePath");

            singleExecutor.execute(new FileScanExecutor(filePath, fileQuene));
            singleExecutor.execute(new FileSaveExecutor(fileQuene));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
