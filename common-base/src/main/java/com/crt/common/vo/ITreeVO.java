package com.crt.common.vo;

import java.util.List;

/**
 * @Auther: caolu@e6yun.com
 * @Date: 2019/3/1 18:34
 * @Description: list转成tree的通用方法接口定义（BuildTreeUtil）
 */
public interface ITreeVO {

    Object getId();

    Object getPid();

    List<ITreeVO> getChildren();

    void setChildren(List<ITreeVO> children);
}
