package com.free.video.controller;

import com.free.common.resp.Result;
import com.free.video.dao.ImgFileDao;
import com.free.video.model.ImgFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("imgFile")
@Slf4j
public class ImgFileController {

    @Autowired
    private ImgFileDao imgFileDao;

    @RequestMapping("list")
    public Result list(ImgFile imgFile) {
        List<ImgFile> imgFileList = imgFileDao.findAll(Example.of(imgFile));
        return new Result("0", "SUCCESS",
                imgFileList);
    }


}
