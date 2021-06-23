package com.free.video.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.free.video.dao.SystemConfigDao;
import com.free.video.model.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zzj
 */
@Service
public class SystemConfigService {

    public static String FILE_PATH_CODE = "filePath";

    @Autowired
    private SystemConfigDao systemConfigDao;

    public String getValueByCode(String code)  {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        SystemConfig query = null;
        try {
        query = SystemConfig.class.newInstance();
        } catch (Exception e) {
            //TODO: handle exception
        }
        query.setSysCode(code);
        List<SystemConfig> systemConfigList = systemConfigDao.findAll(Example.of(query));
        if (CollectionUtils.isNotEmpty(systemConfigList)) {
            return systemConfigList.get(0).getSysValue();
        }

        return null;
    }

}
