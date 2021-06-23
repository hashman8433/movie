package com.free.video.controller;

import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.free.common.resp.Result;
import com.free.video.dao.SystemConfigDao;
import com.free.video.model.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/config")
@Slf4j
public class SystemConfigController {

    @Autowired
    private SystemConfigDao systemConfigDao;

    @RequestMapping("get")
    public Result get() {
        return Result.successObj(systemConfigDao.findAll());
    }

    @RequestMapping("update")
    public Result update(@RequestBody List<SystemConfig> systemConfigList) {
        systemConfigList.stream().forEach(systemConfig -> {
            try {

                SystemConfig query = SystemConfig.class.newInstance();
                query.setSysCode(systemConfig.getSysCode());
                List<SystemConfig> oldSystemConfigList = systemConfigDao.findAll(Example.of(query));
                if (CollectionUtils.isNotEmpty(oldSystemConfigList)) {
                    SystemConfig oldSystemConfig = oldSystemConfigList.get(0);
                    systemConfig.setId(oldSystemConfig.getId());
                } 

                systemConfigDao.save(systemConfig);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return Result.success();
    }
}
