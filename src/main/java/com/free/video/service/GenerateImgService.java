package com.free.video.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.free.common.utils.CommandUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class GenerateImgService {

    @Value("${filePath}")
    public String filePath;

    public String generate(String absolutePath) {

        String tmpImgPath = filePath + "img/" + DateUtil.format(new Date(), "yyyy-MM-dd") +
                UUID.randomUUID().toString().replace("-", "") + "/";

        File imgPath = new File(tmpImgPath);
        imgPath.mkdirs();

        log.info("[图片生成] 图片路径：{} File.isDirectory：{}  File.exists：{}", tmpImgPath, imgPath.isDirectory(), imgPath.exists());
        // 计算视频总视频 大小
        String command = "ffmpeg  -i " + absolutePath + " -vf fps=0.003 " + tmpImgPath + "output%d.jpg";

        try {
            CommandUtil.run(command);
        } catch (IOException e) {
            log.error("[图片生成] 失败 视频文件：{}", absolutePath, e);
        }
        log.info("[图片生成] 成功 视频文件：{}", absolutePath);
        return tmpImgPath;
    }

}
