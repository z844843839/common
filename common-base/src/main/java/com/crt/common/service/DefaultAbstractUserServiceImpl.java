package com.crt.common.service;

import com.crt.common.context.UserContextHelper;
import com.crt.common.vo.AbstractUser;
import com.crt.common.vo.DefaultAbstractUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author changyandong@e6yun.com
 * @Created Date: 2019/1/18 10:07
 * @ClassName DefaultAbstractUserServiceImpl
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(value = "crt.frame.user.session",havingValue = "token",matchIfMissing = true)
public class DefaultAbstractUserServiceImpl implements AbstractUserService {
    @Override
    public AbstractUser getLoginUser() {
        return new DefaultAbstractUser(UserContextHelper.getUserId(),UserContextHelper.getWebgisUserId(),UserContextHelper.getCorpId(),UserContextHelper.getOrgId(),UserContextHelper.getParentId());
    }
}
