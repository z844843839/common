package com.crt.common.service;


import com.crt.common.vo.AbstractUser;

/**
 * @Description
 * @Author changyandong@e6yun.com
 * @Created Date: 2018/11/12 17:24
 * @InterfaceName AbstractUserService
 * @Version: 1.0
 */
public interface AbstractUserService {
    AbstractUser getLoginUser();
}
