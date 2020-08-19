package com.free.video.controller;

import cn.hutool.core.bean.BeanUtil;
import com.free.common.config.Const;
import com.free.common.resp.Result;
import com.free.common.utils.CommandUtil;
import com.free.video.dao.ImgFileDao;
import com.free.video.dao.VideoFileDao;
import com.free.video.model.ImgFile;
import com.free.video.model.VideoFile;
import com.free.video.model.VideoFileDto;
import com.free.video.service.GenerateImgService;
import com.free.video.service.ScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private ImgFileDao imgFileDao;

    @Autowired
    private GenerateImgService generateImgService;

    @Value("${imgPath}")
    public String imgPath;

    private final ExecutorService findFileExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService saveFileExecutor = Executors.newSingleThreadExecutor();


    @RequestMapping("list")
    public Result list(VideoFile vedioFile) {
        List<VideoFile> videoFiles = videoFileDao.findAll(Example.of(vedioFile));
        List<VideoFile> videoFileDtos = new ArrayList<>();

        if (! CollectionUtils.isEmpty(videoFiles)) {
            videoFiles.forEach(videoFile -> {
                try {
                    VideoFileDto videoFileDto = new VideoFileDto();
                    BeanUtil.copyProperties(videoFile, videoFileDto);
                    ImgFile imgFileVo = ImgFile.class.newInstance();
                    imgFileVo.setVideoFileId(videoFile.getId());
                    List<ImgFile> imgFiles = imgFileDao.findAll(Example.of(imgFileVo));
                    if (CollectionUtils.isEmpty(imgFiles)) {
                        return;
                    }

                    videoFileDto.setImgPathWeb(imgFiles.get(0).getFilePathWeb());
                    videoFileDtos.add(videoFileDto);

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }

        return new Result("0", "SUCCESS",
                videoFileDtos);
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

    @RequestMapping("generateImg")
    public Result generateImg() throws IllegalAccessException, InstantiationException {
        VideoFile videoFileQuery = VideoFile.class.newInstance();
        videoFileQuery.setScanStatus(Const.NOSCAN);
        List<VideoFile> videoFileList = videoFileDao.findAll(Example.of(videoFileQuery));
        if (! CollectionUtils.isEmpty(videoFileList)) {
            VideoFile videoFile = videoFileList.get(0);
            String filePath = videoFile.getFilePath();
            String imgFlePathStr = generateImgService.generate(filePath);
            saveImg(imgFlePathStr, videoFile);
        }

        return new Result("0", "SUCCESS");
    }

    /**
     * 保存所有图片文件
     *
     * @param imgFlePathStr
     * @param videoFile
     */
    private void saveImg(String imgFlePathStr, VideoFile videoFile) throws IllegalAccessException, InstantiationException {
        File imgFilePath = new File(imgFlePathStr);
        File[] imgFiles = imgFilePath.listFiles();
        for (int i = 0; i < imgFiles.length; i++) {
            File imgFile = imgFiles[i];
            if (! imgFile.isFile()) {
                continue;
            }

            Date now = new Date();
            ImgFile imgFileBo = ImgFile.class.newInstance();
            imgFileBo.setFilePath(imgFile.getAbsolutePath());
            imgFileBo.setFilePathWeb("static/ext/" + imgFile.getAbsolutePath()
                    .replace(imgPath.replaceAll("/", "\\\\"), "")
                    .replace("\\", "/"));
            imgFileBo.setCreateTime(now);
            imgFileBo.setUpdateTime(now);
            imgFileBo.setVideoFileId(videoFile.getId());
            imgFileDao.save(imgFileBo);
        }
    }



    @RequestMapping("execCmd")
    public Result execCmd(String cmd) throws IOException {
        return new Result("0", "SUCCESS", CommandUtil.run(cmd));
    }

}
