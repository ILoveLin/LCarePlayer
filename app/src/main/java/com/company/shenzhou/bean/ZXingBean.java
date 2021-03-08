package com.company.shenzhou.bean;

/**
 * LoveLin
 * <p>
 * Describe
 */
public class ZXingBean {

    /**
     * username : root
     * password : root
     * title : 标题
     * ip : 192.168.1.1
     * makemsg : 备注信息
     * port : 7788
     * type : 类型
     */

    private String username;
    private String password;
    private String title;
    private String ip;
    private String makemsg;
    private String port;
    private String type;
    private String micport;

    public String getMicport() {
        return micport;
    }

    public void setMicport(String micport) {
        this.micport = micport;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMakemsg() {
        return makemsg;
    }

    public void setMakemsg(String makemsg) {
        this.makemsg = makemsg;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
