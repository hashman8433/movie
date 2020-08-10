package com.free.video.dao;

import com.free.video.model.ImgFile;
import com.free.video.model.TagFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgFileDao extends
        JpaRepository<ImgFile, String> {
}
