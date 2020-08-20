package com.free.movie;

import com.free.VideoApplication;
import com.free.base.BaseSpringBootTest;
import com.free.video.model.VideoFile;
import com.free.video.dao.VideoFileDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

public class VideoApplicationTest extends BaseSpringBootTest {

    @Autowired
    private VideoFileDao videoFileDao;

    @Test
    public void testSelect() {
        List<VideoFile> all = videoFileDao.findAll();
        System.out.println(all);
    }

}
