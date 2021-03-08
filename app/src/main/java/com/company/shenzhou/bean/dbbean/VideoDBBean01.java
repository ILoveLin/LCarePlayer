package com.company.shenzhou.bean.dbbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * LoveLin
 * <p>
 * Describe
 */
@Entity
public class VideoDBBean01 {
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
    String micport;  //0播放HD3，1播放一体机
    @Generated(hash = 1300160908)
    public VideoDBBean01(Long id, String account, String password, String title,
            String ip, String makeMessage, String port, String type, String tag,
            String micport) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.title = title;
        this.ip = ip;
        this.makeMessage = makeMessage;
        this.port = port;
        this.type = type;
        this.tag = tag;
        this.micport = micport;
    }
    @Generated(hash = 48313302)
    public VideoDBBean01() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getMakeMessage() {
        return this.makeMessage;
    }
    public void setMakeMessage(String makeMessage) {
        this.makeMessage = makeMessage;
    }
    public String getPort() {
        return this.port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getMicport() {
        return this.micport;
    }
    public void setMicport(String micport) {
        this.micport = micport;
    }
}
