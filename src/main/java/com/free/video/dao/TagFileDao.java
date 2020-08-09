package com.free.video.dao;

import com.free.video.model.TagFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TagFileDao extends
        JpaRepository<TagFile, String> {
}
