package com.free.common.utils;

import com.free.video.dao.SystemConfigDao;
import com.free.video.model.SystemConfig;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemConfigUtils {

    //将管理上下文的applicationContext设置成静态变量，供全局调用
    public static ConfigurableApplicationContext applicationContext;


    private static Map<String, String> configMap;

    public static Map<String, String> getSystemConfig() {
//
//        if (configMap != null) {
//            return configMap;
//        }

//        WebApplicationContext currentWebApplicationContext = ContextLoader.getCurrentWebApplicationContext()
        SystemConfigDao bean = applicationContext.getBean(SystemConfigDao.class);
        List<SystemConfig> systemConfigList = bean.findAll();

        configMap = new HashMap<>();
        if (CollectionUtils.isEmpty(systemConfigList)) {
            return configMap;
        }

        Map<String, String> configMap = new HashMap<>();
        for (SystemConfig systemConfig : systemConfigList) {
            configMap.put(systemConfig.getSysCode(), systemConfig.getSysValue());
        }
        return configMap;
    }

    public static String getConfig(String code) {
        return getSystemConfig().get(code);
    }


}
