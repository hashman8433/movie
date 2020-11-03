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

        String videoPath = "F:\\迅雷下载\\2\\";
        String inputPath =  videoPath +
                "1.mp4";

        String outputPath =  videoPath + "img";
        File outputPathDir = new File(outputPath);
        if (! outputPathDir.exists()) {
            outputPathDir.mkdirs();
        }

        VideoEditing.init(new ProgramConfig() {{
            setDeBugLog(true);
            setKeepalive(true);
        }});
        CountDownLatch countDownLatch = new CountDownLatch(1);
        VideoEditing.grabbingFrameToJpg(
                inputPath, outputPath, 0.0016, 2, new ProgressCallback() {

            @Override
            public void progress(double pro) {
                System.out.println("pro = " + pro);
                if (pro == 1d) {
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
    }

    @Test
    public void testCount() {
        System.out.println(1d / (3600 / 10d));
    }
}
