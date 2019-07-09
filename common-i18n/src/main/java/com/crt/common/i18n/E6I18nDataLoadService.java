package com.crt.common.i18n;

import java.util.List;

/**
 * 定义接口，引用方可以实现这个接口来传入自己的国际化常量内容
 * @Author liupengfei@e6yun.com
 * @Date 2019/1/30 16:32
 * @Description
 **/
public interface E6I18nDataLoadService {
    /**
     * 载入国际化的常量配置
     * @return
     */
    List<E6I18nDictByLocaleEntity> loadE6I18nDictByLocaleEntity();
}
