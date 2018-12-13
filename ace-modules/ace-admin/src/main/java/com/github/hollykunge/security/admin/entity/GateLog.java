package com.github.hollykunge.security.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "GATE_LOG")
public class GateLog {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MENU")
    private String menu;

    @Column(name = "OPT")
    private String opt;

    @Column(name = "URI")
    private String uri;

    @Column(name = "CRT_TIME")
    private Date crtTime;

    @Column(name = "CRT_USER")
    private String crtUser;

    @Column(name = "CRT_NAME")
    private String crtName;

    @Column(name = "CRT_HOST")
    private String crtHost;

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
     * @return MENU
     */
    public String getMenu() {
        return menu;
    }

    /**
     * @param menu
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     * @return OPT
     */
    public String getOpt() {
        return opt;
    }

    /**
     * @param opt
     */
    public void setOpt(String opt) {
        this.opt = opt;
    }

    /**
     * @return URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     */
    public void setUri(String uri) {
        this.uri = uri;
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
}