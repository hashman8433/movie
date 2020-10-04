package com.free.video.dao;

import com.free.video.model.VideoFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoFileDao extends
        JpaRepository<VideoFile, String> {

    //根据用户名查询模糊用户
    @Query("select t from VideoFile t where t.fileName like CONCAT('%',:fileName,'%')")
    List<VideoFile> findByFileNameLike(String fileName);

    @Query(value = "select t from VideoFile t where t.fileName like CONCAT('%',:fileName,'%') and t.scanStatus = 1",
            countQuery = "SELECT count(*) FROM  VideoFile t where t.fileName like CONCAT('%',:fileName,'%') and t.scanStatus = 1",
            nativeQuery = false)
    Page<VideoFile> findByName(@Param("fileName") String fileName, Pageable pageable);
}
