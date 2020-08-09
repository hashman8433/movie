package com.free.video.dao;

import com.free.video.model.VideoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VideoFileDao extends
        JpaRepository<VideoFile, String> {

}
