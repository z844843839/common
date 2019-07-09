package com.crt.common.apollo.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;

/**
 * @Description
 * @Author changyandong@e6yun.com
 * @Created Date: 2019/4/22 11:11
 * @InterfaceName ConfigChangeService
 * @Version: 1.0
 */
public interface ConfigChangeService {
    /**
     * 自定义配置监听在此实现
     * @param changeEvent
     */
    void detectConfigChanges(ConfigChangeEvent changeEvent);
}
