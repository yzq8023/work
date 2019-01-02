package com.github.hollykunge.security.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "TOTAL_USER")
public class TotalUser {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "BIRTHDAY")
    private String birthday;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "XH")
    private String xh;

    @Column(name = "TECH_POST_NAME")
    private String techPostName;

    @Column(name = "TECH_POST_CODE")
    private String techPostCode;

    @Column(name = "TECHNICAL_POST_NAME")
    private String technicalPostName;

    @Column(name = "TECHNICAL_POST_CODE")
    private String technicalPostCode;

    @Column(name = "TECH_LEVEL_NAME")
    private String techLevelName;

    @Column(name = "TECH_LEVEL_CODE")
    private String techLevelCode;

    @Column(name = "TECHNICAL_MC_NAME")
    private String technicalMcName;

    @Column(name = "TECHNICAL_MC_TCODE")
    private String technicalMcTcode;

    @Column(name = "PERSON_NAME")
    private String personName;

    @Column(name = "HIGHEST_INDUSTRIAL")
    private String highestIndustrial;

    @Column(name = "INNER_ID")
    private String innerId;

    @Column(name = "OFFICE_ADDR")
    private String officeAddr;

    @Column(name = "IDENTITY_NO")
    private String identityNo;

    @Column(name = "XQAH")
    private String xqah;

    @Column(name = "POLICITAL_NAME")
    private String policitalName;

    @Column(name = "POLICITAL_CODE")
    private String policitalCode;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "XING")
    private String xing;

    @Column(name = "PSN_SECRET_LEVEL")
    private String psnSecretLevel;

    @Column(name = "DEPT_TYYH_ORG_CODE")
    private String deptTyyhOrgCode;

    @Column(name = "DEPT_NAME")
    private String deptName;

    @Column(name = "DEPT_CODE")
    private String deptCode;

    @Column(name = "IN_BU_DATE")
    private String inBuDate;

    @Column(name = "MING")
    private String ming;

    @Column(name = "IN_PARTY_DATE")
    private String inPartyDate;

    @Column(name = "MARRY_NAME")
    private String marryName;

    @Column(name = "MARRY_CODE")
    private String marryCode;

    @Column(name = "IDENTITY_TYPE")
    private String identityType;

    @Column(name = "HOME_TOWN")
    private String homeTown;

    @Column(name = "HR_PSN_CODE")
    private String hrPsnCode;

    @Column(name = "IN_OUT_NO")
    private String inOutNo;

    @Column(name = "INFO_SECRET_LEVEL_NAME")
    private String infoSecretLevelName;

    @Column(name = "INFO_SECRET_LEVEL_CODE")
    private String infoSecretLevelCode;

    @Column(name = "END_FLAG")
    private String endFlag;

    @Column(name = "OFFICE_TEL")
    private String officeTel;

    @Column(name = "CODE")
    private String code;

    @Column(name = "HIGHEST_EDU")
    private String highestEdu;

    @Column(name = "DYCN")
    private String dycn;

    @Column(name = "FC_FLAG")
    private String fcFlag;

    @Column(name = "NATIONALITY_NAME")
    private String nationalityName;

    @Column(name = "UNIT_NAME")
    private String unitName;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "CRT_USER")
    private String crtUser;

    @Column(name = "CRT_NAME")
    private String crtName;

    @Column(name = "CRT_HOST")
    private String crtHost;

    @Column(name = "CRT_TIME")
    private Date crtTime;

    @Column(name = "UPD_USER")
    private String updUser;

    @Column(name = "UPD_NAME")
    private String updName;

    @Column(name = "UPD_HOST")
    private String updHost;

    @Column(name = "UPD_TIME")
    private Date updTime;

    @Column(name = "ATTR4")
    private String attr4;

    @Column(name = "ATTR5")
    private String attr5;

    @Column(name = "ATTR6")
    private String attr6;

    @Column(name = "ATTR7")
    private String attr7;

    @Column(name = "ATTR8")
    private String attr8;

    @Column(name = "CAN_ADMIN")
    private byte[] canAdmin;

    @Column(name = "CAN_FORK")
    private byte[] canFork;

    @Column(name = "CAN_CREATE")
    private byte[] canCreate;

    /**
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return USERNAME
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return PASSWORD
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return BIRTHDAY
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return EMAIL
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return TYPE
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return DESCRIPTION
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return XH
     */
    public String getXh() {
        return xh;
    }

    /**
     * @param xh
     */
    public void setXh(String xh) {
        this.xh = xh;
    }

    /**
     * @return TECH_POST_NAME
     */
    public String getTechPostName() {
        return techPostName;
    }

    /**
     * @param techPostName
     */
    public void setTechPostName(String techPostName) {
        this.techPostName = techPostName;
    }

    /**
     * @return TECH_POST_CODE
     */
    public String getTechPostCode() {
        return techPostCode;
    }

    /**
     * @param techPostCode
     */
    public void setTechPostCode(String techPostCode) {
        this.techPostCode = techPostCode;
    }

    /**
     * @return TECHNICAL_POST_NAME
     */
    public String getTechnicalPostName() {
        return technicalPostName;
    }

    /**
     * @param technicalPostName
     */
    public void setTechnicalPostName(String technicalPostName) {
        this.technicalPostName = technicalPostName;
    }

    /**
     * @return TECHNICAL_POST_CODE
     */
    public String getTechnicalPostCode() {
        return technicalPostCode;
    }

    /**
     * @param technicalPostCode
     */
    public void setTechnicalPostCode(String technicalPostCode) {
        this.technicalPostCode = technicalPostCode;
    }

    /**
     * @return TECH_LEVEL_NAME
     */
    public String getTechLevelName() {
        return techLevelName;
    }

    /**
     * @param techLevelName
     */
    public void setTechLevelName(String techLevelName) {
        this.techLevelName = techLevelName;
    }

    /**
     * @return TECH_LEVEL_CODE
     */
    public String getTechLevelCode() {
        return techLevelCode;
    }

    /**
     * @param techLevelCode
     */
    public void setTechLevelCode(String techLevelCode) {
        this.techLevelCode = techLevelCode;
    }

    /**
     * @return TECHNICAL_MC_NAME
     */
    public String getTechnicalMcName() {
        return technicalMcName;
    }

    /**
     * @param technicalMcName
     */
    public void setTechnicalMcName(String technicalMcName) {
        this.technicalMcName = technicalMcName;
    }

    /**
     * @return TECHNICAL_MC_TCODE
     */
    public String getTechnicalMcTcode() {
        return technicalMcTcode;
    }

    /**
     * @param technicalMcTcode
     */
    public void setTechnicalMcTcode(String technicalMcTcode) {
        this.technicalMcTcode = technicalMcTcode;
    }

    /**
     * @return PERSON_NAME
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * @return HIGHEST_INDUSTRIAL
     */
    public String getHighestIndustrial() {
        return highestIndustrial;
    }

    /**
     * @param highestIndustrial
     */
    public void setHighestIndustrial(String highestIndustrial) {
        this.highestIndustrial = highestIndustrial;
    }

    /**
     * @return INNER_ID
     */
    public String getInnerId() {
        return innerId;
    }

    /**
     * @param innerId
     */
    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    /**
     * @return OFFICE_ADDR
     */
    public String getOfficeAddr() {
        return officeAddr;
    }

    /**
     * @param officeAddr
     */
    public void setOfficeAddr(String officeAddr) {
        this.officeAddr = officeAddr;
    }

    /**
     * @return IDENTITY_NO
     */
    public String getIdentityNo() {
        return identityNo;
    }

    /**
     * @param identityNo
     */
    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    /**
     * @return XQAH
     */
    public String getXqah() {
        return xqah;
    }

    /**
     * @param xqah
     */
    public void setXqah(String xqah) {
        this.xqah = xqah;
    }

    /**
     * @return POLICITAL_NAME
     */
    public String getPolicitalName() {
        return policitalName;
    }

    /**
     * @param policitalName
     */
    public void setPolicitalName(String policitalName) {
        this.policitalName = policitalName;
    }

    /**
     * @return POLICITAL_CODE
     */
    public String getPolicitalCode() {
        return policitalCode;
    }

    /**
     * @param policitalCode
     */
    public void setPolicitalCode(String policitalCode) {
        this.policitalCode = policitalCode;
    }

    /**
     * @return GENDER
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return XING
     */
    public String getXing() {
        return xing;
    }

    /**
     * @param xing
     */
    public void setXing(String xing) {
        this.xing = xing;
    }

    /**
     * @return PSN_SECRET_LEVEL
     */
    public String getPsnSecretLevel() {
        return psnSecretLevel;
    }

    /**
     * @param psnSecretLevel
     */
    public void setPsnSecretLevel(String psnSecretLevel) {
        this.psnSecretLevel = psnSecretLevel;
    }

    /**
     * @return DEPT_TYYH_ORG_CODE
     */
    public String getDeptTyyhOrgCode() {
        return deptTyyhOrgCode;
    }

    /**
     * @param deptTyyhOrgCode
     */
    public void setDeptTyyhOrgCode(String deptTyyhOrgCode) {
        this.deptTyyhOrgCode = deptTyyhOrgCode;
    }

    /**
     * @return DEPT_NAME
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * @param deptName
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * @return DEPT_CODE
     */
    public String getDeptCode() {
        return deptCode;
    }

    /**
     * @param deptCode
     */
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    /**
     * @return IN_BU_DATE
     */
    public String getInBuDate() {
        return inBuDate;
    }

    /**
     * @param inBuDate
     */
    public void setInBuDate(String inBuDate) {
        this.inBuDate = inBuDate;
    }

    /**
     * @return MING
     */
    public String getMing() {
        return ming;
    }

    /**
     * @param ming
     */
    public void setMing(String ming) {
        this.ming = ming;
    }

    /**
     * @return IN_PARTY_DATE
     */
    public String getInPartyDate() {
        return inPartyDate;
    }

    /**
     * @param inPartyDate
     */
    public void setInPartyDate(String inPartyDate) {
        this.inPartyDate = inPartyDate;
    }

    /**
     * @return MARRY_NAME
     */
    public String getMarryName() {
        return marryName;
    }

    /**
     * @param marryName
     */
    public void setMarryName(String marryName) {
        this.marryName = marryName;
    }

    /**
     * @return MARRY_CODE
     */
    public String getMarryCode() {
        return marryCode;
    }

    /**
     * @param marryCode
     */
    public void setMarryCode(String marryCode) {
        this.marryCode = marryCode;
    }

    /**
     * @return IDENTITY_TYPE
     */
    public String getIdentityType() {
        return identityType;
    }

    /**
     * @param identityType
     */
    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    /**
     * @return HOME_TOWN
     */
    public String getHomeTown() {
        return homeTown;
    }

    /**
     * @param homeTown
     */
    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    /**
     * @return HR_PSN_CODE
     */
    public String getHrPsnCode() {
        return hrPsnCode;
    }

    /**
     * @param hrPsnCode
     */
    public void setHrPsnCode(String hrPsnCode) {
        this.hrPsnCode = hrPsnCode;
    }

    /**
     * @return IN_OUT_NO
     */
    public String getInOutNo() {
        return inOutNo;
    }

    /**
     * @param inOutNo
     */
    public void setInOutNo(String inOutNo) {
        this.inOutNo = inOutNo;
    }

    /**
     * @return INFO_SECRET_LEVEL_NAME
     */
    public String getInfoSecretLevelName() {
        return infoSecretLevelName;
    }

    /**
     * @param infoSecretLevelName
     */
    public void setInfoSecretLevelName(String infoSecretLevelName) {
        this.infoSecretLevelName = infoSecretLevelName;
    }

    /**
     * @return INFO_SECRET_LEVEL_CODE
     */
    public String getInfoSecretLevelCode() {
        return infoSecretLevelCode;
    }

    /**
     * @param infoSecretLevelCode
     */
    public void setInfoSecretLevelCode(String infoSecretLevelCode) {
        this.infoSecretLevelCode = infoSecretLevelCode;
    }

    /**
     * @return END_FLAG
     */
    public String getEndFlag() {
        return endFlag;
    }

    /**
     * @param endFlag
     */
    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    /**
     * @return OFFICE_TEL
     */
    public String getOfficeTel() {
        return officeTel;
    }

    /**
     * @param officeTel
     */
    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    /**
     * @return CODE
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return HIGHEST_EDU
     */
    public String getHighestEdu() {
        return highestEdu;
    }

    /**
     * @param highestEdu
     */
    public void setHighestEdu(String highestEdu) {
        this.highestEdu = highestEdu;
    }

    /**
     * @return DYCN
     */
    public String getDycn() {
        return dycn;
    }

    /**
     * @param dycn
     */
    public void setDycn(String dycn) {
        this.dycn = dycn;
    }

    /**
     * @return FC_FLAG
     */
    public String getFcFlag() {
        return fcFlag;
    }

    /**
     * @param fcFlag
     */
    public void setFcFlag(String fcFlag) {
        this.fcFlag = fcFlag;
    }

    /**
     * @return NATIONALITY_NAME
     */
    public String getNationalityName() {
        return nationalityName;
    }

    /**
     * @param nationalityName
     */
    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    /**
     * @return UNIT_NAME
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * @return MOBILE
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return CRT_USER
     */
    public String getCrtUser() {
        return crtUser;
    }

    /**
     * @param crtUser
     */
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    /**
     * @return CRT_NAME
     */
    public String getCrtName() {
        return crtName;
    }

    /**
     * @param crtName
     */
    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    /**
     * @return CRT_HOST
     */
    public String getCrtHost() {
        return crtHost;
    }

    /**
     * @param crtHost
     */
    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    /**
     * @return CRT_TIME
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * @param crtTime
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * @return UPD_USER
     */
    public String getUpdUser() {
        return updUser;
    }

    /**
     * @param updUser
     */
    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    /**
     * @return UPD_NAME
     */
    public String getUpdName() {
        return updName;
    }

    /**
     * @param updName
     */
    public void setUpdName(String updName) {
        this.updName = updName;
    }

    /**
     * @return UPD_HOST
     */
    public String getUpdHost() {
        return updHost;
    }

    /**
     * @param updHost
     */
    public void setUpdHost(String updHost) {
        this.updHost = updHost;
    }

    /**
     * @return UPD_TIME
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * @param updTime
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * @return ATTR4
     */
    public String getAttr4() {
        return attr4;
    }

    /**
     * @param attr4
     */
    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    /**
     * @return ATTR5
     */
    public String getAttr5() {
        return attr5;
    }

    /**
     * @param attr5
     */
    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    /**
     * @return ATTR6
     */
    public String getAttr6() {
        return attr6;
    }

    /**
     * @param attr6
     */
    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    /**
     * @return ATTR7
     */
    public String getAttr7() {
        return attr7;
    }

    /**
     * @param attr7
     */
    public void setAttr7(String attr7) {
        this.attr7 = attr7;
    }

    /**
     * @return ATTR8
     */
    public String getAttr8() {
        return attr8;
    }

    /**
     * @param attr8
     */
    public void setAttr8(String attr8) {
        this.attr8 = attr8;
    }

    /**
     * @return CAN_ADMIN
     */
    public byte[] getCanAdmin() {
        return canAdmin;
    }

    /**
     * @param canAdmin
     */
    public void setCanAdmin(byte[] canAdmin) {
        this.canAdmin = canAdmin;
    }

    /**
     * @return CAN_FORK
     */
    public byte[] getCanFork() {
        return canFork;
    }

    /**
     * @param canFork
     */
    public void setCanFork(byte[] canFork) {
        this.canFork = canFork;
    }

    /**
     * @return CAN_CREATE
     */
    public byte[] getCanCreate() {
        return canCreate;
    }

    /**
     * @param canCreate
     */
    public void setCanCreate(byte[] canCreate) {
        this.canCreate = canCreate;
    }
}