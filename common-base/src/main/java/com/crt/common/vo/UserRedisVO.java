package com.crt.common.vo;


import lombok.Data;

import java.util.Date;

/**
 * 登陆用户VO
 * @author malin
 */
@Data
public class UserRedisVO{
    /**
     * 用户ID
     */
    private Integer id;
    /**
     * 用户编码
     */
    private Long userCode;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 用户账号状态（0正常、1删除、2冻结）
     */
    private Integer accountNumberState;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号码
     */
    private String tel;
    /**
     * 微信
     */
    private String wechat;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 删除标识
     */
    private Integer deleted;
    /**
     * 失效时间
     */
    private Date invalidAt;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 用户类型(0系统用户 1平台用户 2运营用户)
     */
    private Integer userType;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * qq
     */
    private String qq;
    /**
     * 用户头像存储路径
     */
    private String chatheadFilepath;
    /**
     * 用户证件存储路径
     */
    private String documentsFilepath;
    /**
     * 用户出生年月
     */
    private String birthdate;
    /**
     * 用户英文名
     */
    private String englishName;
    /**
     * 用户办公电话
     */
    private String officeTel;
    /**
     * 办公电话分机
     */
    private String extension;
    /**
     * 用户工作地址
     */
    private String workAddress;
    /**
     * 用户民族
     */
    private String nation;
    /**
     * 用户政治面貌
     */
    private Integer politicsStatus;
    /**
     * 用户户口所在地
     */
    private String accountLocation;
    /**
     * 用户家庭住址
     */
    private String homeAddress;
    /**
     * 用户银行卡号
     */
    private String cardNo;
    /**
     * 开户行
     */
    private String housebank;
    /**
     * 开户行支行
     */
    private String housbankbrn;
    /**
     * 备注
     */
    private String remark;
    /**
     * 组织编码
     */
    private Long orgCode;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * （官网）供应商服务类型
     */
    private Integer serviceType;
    /**
     * 角色编码
     */
    private Long roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 所属专业公司ID
     */
    private Integer attrInt1;
    /**
     * 所属区域公司ID
     */
    private Integer attrInt2;

}
