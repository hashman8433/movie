package com.free.video.controller;

import com.free.common.resp.Result;
import com.free.video.dao.VideoFileDao;
import com.free.video.model.VideoFile;
import com.free.video.service.ScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("videoFile")
@Slf4j
public class VideoFileController {

    @Autowired
    private ScanService scanService;

    @Autowired
    private VideoFileDao videoFileDao;

    private final ExecutorService findFileExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService saveFileExecutor = Executors.newSingleThreadExecutor();

    @RequestMapping("list")
    public Result list(VideoFile vedioFile) {
        List<VideoFile> videoFiles = videoFileDao.findAll(Example.of(vedioFile));

        return new Result("0", "SUCCESS",
                videoFiles);
    }


    @RequestMapping("save")
    public Result save(VideoFile vedioFile) {
        videoFileDao.save(vedioFile);
        return new Result("0", "SUCCESS");
    }

    @RequestMapping("updateFiles")
    public Result updateFiles() {
        scanService.scanVideo();
        return new Result("0", "SUCCESS");
    }


}
