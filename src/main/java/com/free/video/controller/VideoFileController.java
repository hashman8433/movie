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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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

    @Value("${filePath}")
    public String filePath;

    private final ExecutorService findFileExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService saveFileExecutor = Executors.newSingleThreadExecutor();


    @Cacheable(cacheNames = "vedioFileList", key = "#vedioFile")
    @RequestMapping("list")
    public Result list(@RequestBody VideoFile vedioFile) {
//        List<VideoFile> videoFiles = videoFileDao.findAll(Example.of(vedioFile));
        List<VideoFile> videoFiles = videoFileDao.findByFileNameLike(vedioFile.getFileName());
        List<VideoFileDto> videoFileDtos = new ArrayList<>();
        List<VideoFileDto> videoFileDtoEnds = new ArrayList<>();

        if (CollectionUtils.isEmpty(videoFiles)) {
            return new Result("0", "SUCCESS",
                    videoFiles);
        }

        List<ImgFile> imgFileList = imgFileDao.findAll();
        if (CollectionUtils.isEmpty(imgFileList)) {
            return new Result("0", "SUCCESS",
                    videoFiles);
        }

        Map<String, List<ImgFile>> fileIdMapImgList = imgFileList.stream().collect(Collectors.groupingBy(ImgFile::getVideoFileId));

        videoFiles.forEach(videoFile -> {

            VideoFileDto videoFileDto = new VideoFileDto();
            BeanUtil.copyProperties(videoFile, videoFileDto);

            // 选出第一张图片作为 封面
            List<ImgFile> imgFiles = fileIdMapImgList.get(videoFileDto.getId());
            if (CollectionUtils.isEmpty(imgFiles)) {
                // 没有图片信息
                videoFileDto.setImgPathWeb("/static/logo.png");
                videoFileDtoEnds.add(videoFileDto);
                return;
            }

            videoFileDto.setImgPathWeb(imgFiles.get(0).getFilePathWeb());
            videoFileDtos.add(videoFileDto);
        });

        videoFileDtos.addAll(videoFileDtoEnds);
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

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @RequestMapping("generateImg")
    public Result generateImg() {

        executorService.execute(() -> {

            try {
                VideoFile videoFileQuery = VideoFile.class.newInstance();
                videoFileQuery.setScanStatus(Const.NOSCAN);
                List<VideoFile> videoFileList = videoFileDao.findAll(Example.of(videoFileQuery));

                if (CollectionUtils.isEmpty(videoFileList)) {
                    return;
                }

                log.info("已发现 {} 个视频未生成图片", videoFileList.size());
                for (int i = 0; i < videoFileList.size(); i++) {

                    VideoFile videoFile = videoFileList.get(i);
                    String filePath = videoFile.getFilePath();
                    String imgFlePathStr = generateImgService.generate(filePath);

                    videoFile.setScanStatus(Const.SCAN_SUCCESS);
                    videoFileDao.save(videoFile);
                    saveImg(imgFlePathStr, videoFile);

                    log.info("[图片生成完毕] 视频:{}", videoFile.getFilePath());
                    int index = i + 1;
                    log.info("已完成 第{}个视频 剩余{}", index, videoFileList.size() - index);
                }
                log.info("已扫描{}个视频文件", videoFileList.size());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });

        return new Result("0", "SUCCESS", "已经开始生成图片");
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
            imgFileBo.setFilePathWeb(imgFile.getAbsolutePath()
                    .replace(filePath.replaceAll("/", "\\\\"), "")
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
