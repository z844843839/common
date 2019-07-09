package com.crt.common.web;

import com.crt.common.service.AbstractUserService;
import com.crt.common.vo.AbstractUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 所有的Controller的父类
 */
public abstract class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired(required = false)
    private AbstractUserService abstractUserService;

    public AbstractUser getLoginUser() {
        return abstractUserService.getLoginUser();
    }

    /**
     * 把前台传来的时间戳转换为Date类型
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
    }

    private class MyDateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) {
            try {
                if (StringUtils.isBlank(text)) {
                    setValue(null);
                    return;
                }
                long timeStamp = new Long(text);
                Date date = new Date(timeStamp);
                setValue(date);
            } catch (Exception e) {
                logger.error("从前台传来的时间戳错误，请检查前台传来的时间戳");
            }
        }
    }
}
