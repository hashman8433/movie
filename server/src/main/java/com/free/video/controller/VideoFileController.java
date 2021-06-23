package com.free.video.controller;

import cn.hutool.core.codec.Base64;
import com.free.common.config.Const;
import com.free.common.resp.Result;
import com.free.common.utils.CommandUtil;
import com.free.video.dao.ImgFileDao;
import com.free.video.dao.SystemConfigDao;
import com.free.video.dao.VideoFileDao;
import com.free.video.model.ImgFile;
import com.free.video.model.VideoFile;
import com.free.video.model.option.SearchOption;
import com.free.video.service.GenerateImgService;
import com.free.video.service.ScanService;
import com.free.video.service.SystemConfigService;
import com.free.video.service.VideoFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    private VideoFileService videoFileService;

    // @Value("${filePath:''}")
    // public String filePath;

    @Autowired
    private SystemConfigDao systemConfigDao;

    @Autowired
    private SystemConfigService systemConfigService;

    private final ExecutorService findFileExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService saveFileExecutor = Executors.newSingleThreadExecutor();


    /**
     * 获取文件列表
     * @param videoFile
     * @return
     */
    @Cacheable(cacheNames = "videoFileList", key = "#videoFile")
    @RequestMapping("list")
    public Result list(@RequestBody @Validated(value = SearchOption.class) VideoFile videoFile) {
        if (videoFile.getFileName() == null){
            videoFile.setFileName("");
        }

        Page<VideoFile> videoFiles = videoFileDao.findByName(videoFile.getFileName(),
                PageRequest.of(videoFile.getPageNo() - 1, videoFile.getPageSize(),
                        Sort.by("scanStatus")));

        if (CollectionUtils.isEmpty(videoFiles.getContent())) {
            return new Result("0", "SUCCESS",
                    videoFiles);
        }

        List<ImgFile> imgFileList = imgFileDao.findAll();
        if (CollectionUtils.isEmpty(imgFileList)) {
            return new Result("0", "SUCCESS",
                    videoFiles);
        }

        Map<String, List<ImgFile>> fileIdMapImgList = imgFileList.stream().collect(Collectors.groupingBy(ImgFile::getVideoFileId));

        videoFiles.forEach(videoFileTemp -> {

            // 选出第一张图片作为 封面
            List<ImgFile> imgFiles = fileIdMapImgList.get(videoFileTemp.getId());
            if (CollectionUtils.isEmpty(imgFiles)) {
                // 没有图片信息
                videoFileTemp.setImgPathWeb("/static/logo.png");
                return;
            }

            videoFileTemp.setImgPathWeb(imgFiles.get(0).getFilePathWeb());
        });

        return new Result("0", "SUCCESS",
                videoFiles);
    }


    @RequestMapping("save")
    public Result save(VideoFile vedioFile) {
        videoFileDao.save(vedioFile);
        return new Result("0", "SUCCESS");
    }

    /**
     * 扫描配置目录下的 视频文件
     * @return
     */
    @RequestMapping("updateFiles")
    public Result updateFiles() {
        scanService.scanVideo();
        return new Result("0", "SUCCESS");
    }

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 检查视频图片 是否已经生成
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @RequestMapping("checkImgIsExist")
    public Result checkImgIsExist() throws IllegalAccessException, InstantiationException {
        VideoFile videoFileQuery = VideoFile.class.newInstance();
        videoFileQuery.setScanStatus(Const.SCAN_SUCCESS);
        List<VideoFile> videoFileList = videoFileDao.findAll(Example.of(videoFileQuery));

        if (CollectionUtils.isEmpty(videoFileList)) {
            return Result.SUCCESS;
        }

        int imgFileNoExistSize = 0;

        for (VideoFile videoFile : videoFileList) {
            ImgFile imgFileQuery = ImgFile.class.newInstance();
            imgFileQuery.setVideoFileId(videoFile.getId());


            List<ImgFile> imgFileList = imgFileDao.findAll(Example.of(imgFileQuery));
            if (CollectionUtils.isEmpty(imgFileList)) {
                log.info("影片：{} 的图片为空 ", videoFile.getFileName());
                videoFileService.resetScanStatus(videoFile);
                imgFileNoExistSize++;
                continue;
            }


            boolean imgFileNoExists = false;
            for (ImgFile imgFile : imgFileList) {
                File imgFileObj = new File(imgFile.getFilePath());
                if (! imgFileObj.exists()) {
                    imgFileNoExists = true;
                    break;
                }
            }


            if (imgFileNoExists) { // 生成的图片文件不存在
                imgFileNoExistSize++;
                log.info("影片：{} 的生成图片 不存在 ", videoFile.getFileName());

                videoFileService.resetScanStatus(videoFile);

                ImgFile imgFileDel = ImgFile.class.newInstance();
                imgFileDel.setVideoFileId(videoFile.getId());
                imgFileDao.delete(imgFileDel);

            }

        }

        String checkLog = String.format("影片的生成图片 不存在 数量：%s", imgFileNoExistSize);
        log.info(checkLog);

        return Result.success("检查完成", checkLog);

    }

    /**
     * 生成视频预览图片
     * @return
     */
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

                    generateImgAndSave(videoFile, filePath);

                    log.info("[图片生成完毕] 视频:{}", videoFile.getFilePath());
                    int index = i + 1;
                    log.info("已完成 第{}个视频 剩余{}", index, videoFileList.size() - index);
                }
                log.info("已扫描{}个视频文件", videoFileList.size());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return new Result("0", "SUCCESS", "已经开始生成图片");
    }

    /**
     * 生成图片和 保存图片和视频的关联状态
     *
     * @param videoFile
     * @param filePath
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void generateImgAndSave(VideoFile videoFile, String filePath) throws IllegalAccessException, InstantiationException {

        try {
            String imgFlePathStr = generateImgService.generate(filePath);

            saveImg(imgFlePathStr, videoFile);

            videoFile.setScanStatus(Const.SCAN_SUCCESS);
            videoFileDao.save(videoFile);
        } catch (IllegalAccessException|InstantiationException e) {
            videoFile.setScanStatus(Const.SCAN_FAIL);
            videoFileDao.save(videoFile);
            e.printStackTrace();
        }
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
        if (ArrayUtils.isEmpty(imgFiles)) {
//            throw new IllegalArgumentException("该文件夹下没有 生成图片");
            log.info("{} 该文件夹下没有 生成图片", imgFlePathStr);
            return;
        }

        for (int i = 0; i < imgFiles.length; i++) {
            File imgFile = imgFiles[i];
            if (! imgFile.isFile()) {
                continue;
            }

            String filePath = systemConfigService.getValueByCode(SystemConfigService.FILE_PATH_CODE);
            Date now = new Date();
            ImgFile imgFileBo = ImgFile.class.newInstance();

            imgFileBo.setFilePath(imgFile.getAbsolutePath());
            // 生成图片base64 保存在数据库中
            imgFileBo.setFileContent(Base64.encode(imgFile.getAbsolutePath()));
            imgFileBo.setFilePathWeb(imgFile.getAbsolutePath()
                    .replace(filePath.replaceAll("/", "\\\\"), "")
                    .replace("\\", "/"));

            imgFileBo.setCreateTime(now);
            imgFileBo.setUpdateTime(now);
            imgFileBo.setVideoFileId(videoFile.getId());
            imgFileDao.save(imgFileBo);
        }
    }


    /**
     * 执行命令
     * @param cmd
     * @return
     * @throws IOException
     */
    @RequestMapping("execCmd")
    public Result execCmd(String cmd) throws IOException {
        return new Result("0", "SUCCESS",
                CommandUtil.run(new String[]{"ls", cmd}));
    }

}
