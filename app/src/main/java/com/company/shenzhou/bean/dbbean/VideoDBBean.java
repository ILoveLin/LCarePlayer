package com.company.shenzhou.bean.dbbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * LoveLin
 * <p>
 * Describe 设备视频链接记录表
 */
@Entity
public class VideoDBBean  {
    @Id(autoincrement = true)
    Long id;

    String account;
    String password;
    String title;
    String ip;
    String makeMessage;
    String port;
    String type;  //0播放HD3，1播放一体机
    String tag;  //0播放HD3，1播放一体机

    @Generated(hash = 1835669782)
    public VideoDBBean(Long id, String account, String password, String title,
            String ip, String makeMessage, String port, String type, String tag) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.title = title;
        this.ip = ip;
        this.makeMessage = makeMessage;
        this.port = port;
        this.type = type;
        this.tag = tag;
    }

    @Generated(hash = 995687129)
    public VideoDBBean() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getMakeMessage() {
        return makeMessage;
    }

    public void setMakeMessage(String makeMessage) {
        this.makeMessage = makeMessage;
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
