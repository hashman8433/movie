package com.free.video.controller;

import com.free.common.resp.Result;
import com.free.video.dao.SystemConfigDao;
import com.free.video.model.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/config")
@Slf4j
public class SystemConfigController {

    @Autowired
    private SystemConfigDao systemConfigDao;

    @Cacheable(cacheNames = "config")
    @RequestMapping("get")
    public Result get() {
        return Result.successObj(systemConfigDao.findAll());
    }

    public Result update(SystemConfig systemConfig) {
        return Result.successObj(systemConfigDao.save(systemConfig));
    }
}
