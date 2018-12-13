package com.github.hollykunge.security.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BASE_RESOURCE_AUTHORITY")
public class ResourceAuthority {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "AUTHORITY_ID")
    private String authorityId;

    @Column(name = "AUTHORITY_TYPE")
    private String authorityType;

    @Column(name = "RESOURCE_ID")
    private String resourceId;

    @Column(name = "RESOURCE_TYPE")
    private String resourceType;

    @Column(name = "PARENT_ID")
    private String parentId;

    @Column(name = "PATH")
    private String path;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CRT_TIME")
    private Date crtTime;

    @Column(name = "CRT_USER")
    private String crtUser;

    @Column(name = "CRT_NAME")
    private String crtName;

    @Column(name = "CRT_HOST")
    private String crtHost;

    @Column(name = "ATTR1")
    private String attr1;

    @Column(name = "ATTR2")
    private String attr2;

    @Column(name = "ATTR3")
    private String attr3;

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

    public ResourceAuthority(String authorityType, String resourceType) {
        this.authorityType = authorityType;
        this.resourceType = resourceType;
    }

    public ResourceAuthority() {
    }

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
     * @return AUTHORITY_ID
     */
    public String getAuthorityId() {
        return authorityId;
    }

    /**
     * @param authorityId
     */
    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    /**
     * @return AUTHORITY_TYPE
     */
    public String getAuthorityType() {
        return authorityType;
    }

    /**
     * @param authorityType
     */
    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    /**
     * @return RESOURCE_ID
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return RESOURCE_TYPE
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * @return PARENT_ID
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return PATH
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
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
     * @return ATTR1
     */
    public String getAttr1() {
        return attr1;
    }

    /**
     * @param attr1
     */
    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    /**
     * @return ATTR2
     */
    public String getAttr2() {
        return attr2;
    }

    /**
     * @param attr2
     */
    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    /**
     * @return ATTR3
     */
    public String getAttr3() {
        return attr3;
    }

    /**
     * @param attr3
     */
    public void setAttr3(String attr3) {
        this.attr3 = attr3;
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
}