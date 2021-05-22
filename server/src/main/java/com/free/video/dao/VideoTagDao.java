package com.free.video.dao;

import com.free.video.model.VideoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VideoTagDao extends
        JpaRepository<VideoTag, String> {

}