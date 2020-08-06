package com.free.video.dao;

import com.free.video.model.VideoFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoFileDao extends
        JpaRepository<VideoFile, String> {

}
