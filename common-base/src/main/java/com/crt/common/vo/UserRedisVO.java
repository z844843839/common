package com.crt.common.vo;


import java.util.Date;

public class UserRedisVO {
    //ID
    private Integer id;
    //用户编码
    private long userCode;
    //真实姓名
    private String realName;
    //账号
    private String accountNumber;
    //用户账号状态（0正常、1删除、2冻结）
    private int accountNumberState;
    //密码
    private String password;
    //手机号码
    private String tel;
    //微信
    private String wechat;
    //电子邮箱
    private String email;
    //删除标识
    private int deleted;
    //失效时间
    private Date invalidAt;
    //创建时间
    private Date createdAt;
    //身份证号
    private String idCard;
    //用户类型(0系统用户 1平台用户 2运营用户)
    private int userType;
    //性别
    private int sex;
    //qq
    private String qq;
    //用户头像存储路径
    private String chatheadFilepath;
    //用户证件存储路径
    private String documentsFilepath;
    //用户出生年月
    private String birthdate;
    //用户英文名
    private String englishName;
    //用户办公电话
    private String officeTel;
    //办公电话分机
    private String extension;
    //用户工作地址
    private String workAddress;
    //用户民族
    private String nation;
    //用户政治面貌
    private Integer politicsStatus;
    //用户户口所在地
    private String accountLocation;
    //用户家庭住址
    private String homeAddress;
    //用户银行卡号
    private String cardNo;
    //开户行
    private String housebank;
    //开户行支行
    private String housbankbrn;
    //备注
    private String remark;
    //组织编码
    private Long orgCode;
    //组织名称
    private String orgName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getUserCode() {
        return userCode;
    }

    public void setUserCode(long userCode) {
        this.userCode = userCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountNumberState() {
        return accountNumberState;
    }

    public void setAccountNumberState(int accountNumberState) {
        this.accountNumberState = accountNumberState;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getInvalidAt() {
        return invalidAt;
    }

    public void setInvalidAt(Date invalidAt) {
        this.invalidAt = invalidAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getChatheadFilepath() {
        return chatheadFilepath;
    }

    public void setChatheadFilepath(String chatheadFilepath) {
        this.chatheadFilepath = chatheadFilepath;
    }

    public String getDocumentsFilepath() {
        return documentsFilepath;
    }

    public void setDocumentsFilepath(String documentsFilepath) {
        this.documentsFilepath = documentsFilepath;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getOfficeTel() {
        return officeTel;
    }

    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getPoliticsStatus() {
        return politicsStatus;
    }

    public void setPoliticsStatus(Integer politicsStatus) {
        this.politicsStatus = politicsStatus;
    }

    public String getAccountLocation() {
        return accountLocation;
    }

    public void setAccountLocation(String accountLocation) {
        this.accountLocation = accountLocation;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getHousebank() {
        return housebank;
    }

    public void setHousebank(String housebank) {
        this.housebank = housebank;
    }

    public String getHousbankbrn() {
        return housbankbrn;
    }

    public void setHousbankbrn(String housbankbrn) {
        this.housbankbrn = housbankbrn;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
