package com.free.video.dao;

import com.free.video.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigDao  extends
        JpaRepository<SystemConfig, String> {
}
