package com.free.movie;

import com.free.VideoApplication;
import com.free.video.model.VideoFile;
import com.free.video.dao.VideoFileDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VideoApplication.class)
public class VideoApplicationTest {

    @Autowired
    private VideoFileDao videoFileDao;

    @Test
    public void testSelect() {
        List<VideoFile> all = videoFileDao.findAll();
        System.out.println(all);
    }

}
