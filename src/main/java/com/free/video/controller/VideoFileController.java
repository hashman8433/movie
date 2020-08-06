package com.free.video.controller;

import com.free.common.resp.Result;
import com.free.video.dao.VideoFileDao;
import com.free.video.model.VideoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("videoFile")
public class VideoFileController {

    @Autowired
    private VideoFileDao videoFileDao;

    @RequestMapping("list")
    public Result list(VideoFile vedioFile) {
        return new Result("0", "SUCCESS",
                videoFileDao.findAll(Example.of(vedioFile)));
    }


    @RequestMapping("save")
    public Result save(VideoFile vedioFile) {
        videoFileDao.save(vedioFile);
        return new Result("0", "SUCCESS");
    }

}
