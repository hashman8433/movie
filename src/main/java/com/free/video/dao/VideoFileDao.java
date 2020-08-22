package com.free.video.dao;

import com.free.video.model.VideoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoFileDao extends
        JpaRepository<VideoFile, String> {

    //根据用户名查询模糊用户
    @Query("select t from VideoFile t where " +
            "(?1 is null or t.fileName like CONCAT('%',?1,'%'))")
    List<VideoFile> findByFileNameLike(String fileName);
}
