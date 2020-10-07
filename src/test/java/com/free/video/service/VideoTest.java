package com.free.video.service;

import cn.novelweb.video.edit.VideoEditing;
import cn.novelweb.video.format.FormatConversion;
import cn.novelweb.video.format.callback.ProgressCallback;
import cn.novelweb.video.pojo.ProgramConfig;
import cn.novelweb.video.pojo.VideoParameters;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;

public class VideoTest {

    @Test
    public void testGetVideoInfo() {

        String inputPath = "/Downloads/" +
                "1.mkv";
        VideoParameters videoParameters = FormatConversion.getVideoParameters(
                new File(inputPath));
        System.out.println("影片长度 ：" + videoParameters.getVideoLengthTime() / 60 + " min");
    }

    @Test
    public void testGetJpg() throws InterruptedException {

        String videoPath = "/Downloads/";
        String inputPath =  videoPath +
                "1.mkv";

        String outputPath =  videoPath + "img";

        VideoEditing.init(new ProgramConfig() {{
            setDeBugLog(true);
            setKeepalive(true);
        }});
        CountDownLatch countDownLatch = new CountDownLatch(1);
        VideoEditing.grabbingFrameToJpg(inputPath, outputPath, "20", "60", 2d, 2, new ProgressCallback() {

            @Override
            public void progress(double pro) {
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }
}
