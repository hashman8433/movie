package com.free.video.service;

import com.free.common.config.Const;
import com.free.video.dao.VideoFileDao;
import com.free.video.model.VideoFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoFileService {

    @Autowired
    private VideoFileDao videoFileDao;

    /**
     * 重置文件图片生成 状态
     *
     * @param videoFile
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public VideoFile resetScanStatus(VideoFile videoFile) throws IllegalAccessException, InstantiationException {
        VideoFile videoFileUpd = VideoFile.class.newInstance();
        BeanUtils.copyProperties(videoFile, videoFileUpd);
        videoFileUpd.setScanStatus(Const.NOSCAN);
        return videoFileDao.save(videoFileUpd);
    }

}
