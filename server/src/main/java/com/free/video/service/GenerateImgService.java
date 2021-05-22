package com.free.video.service;

import cn.hutool.core.date.DateUtil;
import cn.novelweb.video.edit.VideoEditing;
import cn.novelweb.video.format.FormatConversion;
import cn.novelweb.video.pojo.ProgramConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class GenerateImgService {

    // @Value("${filePath:''}")
    // public String filePath;

    @Autowired
    public SystemConfigService systemConfigService;

    public String generate(final String absolutePath) {
        String filePath = systemConfigService.getValueByCode(SystemConfigService.FILE_PATH_CODE);

        String tmpImgPath = filePath + "img/" + DateUtil.format(new Date(), "yyyy-MM-dd") +
                UUID.randomUUID().toString().replace("-", "") + "/";

        File imgPath = new File(tmpImgPath);
        imgPath.mkdirs();

        log.info("[图片生成] 影片：{} 图片路径：{} File.isDirectory：{}  File.exists：{}", absolutePath,
                 tmpImgPath, imgPath.isDirectory(), imgPath.exists());

        try {

            VideoEditing.init(new ProgramConfig() {{
                setDeBugLog(true);
                setKeepalive(true);
            }});

            // 每个视频只需要 10张图片
            double videoLengthTime = FormatConversion.getVideoParameters(
                    new File(absolutePath)).getVideoLengthTime();
            double frequency = 1d / (videoLengthTime / 11d);


            CountDownLatch countDownLatch = new CountDownLatch(1);
            VideoEditing.grabbingFrameToJpg(
                    absolutePath,
                    tmpImgPath, frequency, 2, pro -> {
                        log.info("影片:{} 生成图片进度:{}", absolutePath, pro);
                        if (pro == 1d) {
                            countDownLatch.countDown();
                        }
                    });
            countDownLatch.await();
        } catch (Exception e) {
            log.info("影片：{} 截图生成异常", absolutePath);
            e.printStackTrace();
        }

        log.info("[图片生成-结束]  视频文件：{}", absolutePath);
        return tmpImgPath;
    }

}
