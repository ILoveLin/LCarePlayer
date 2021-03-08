package com.company.shenzhou.bean.dbbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * LoveLin
 * <p>
 * Describe
 */
@Entity
public class UserDBRememberBean {
    @Id(autoincrement = true)
    Long id;

    @Unique
    String username;
    String password;
    String tag;
    String remember;
    int userType = 0;      //0普通用户、1权限用户、2超级管理员  默认为0-普通用户
    @Generated(hash = 495372402)
    public UserDBRememberBean(Long id, String username, String password, String tag,
            String remember, int userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tag = tag;
        this.remember = remember;
        this.userType = userType;
    }
    @Generated(hash = 480470895)
    public UserDBRememberBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getRemember() {
        return this.remember;
    }
    public void setRemember(String remember) {
        this.remember = remember;
    }
    public int getUserType() {
        return this.userType;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }

    







}
