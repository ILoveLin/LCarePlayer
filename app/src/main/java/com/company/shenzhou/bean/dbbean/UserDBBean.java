package com.company.shenzhou.bean.dbbean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * LoveLin
 * <p>
 * Describe用户表    普通用户、权限用户、超级管理员
 */
@Entity
public class UserDBBean {
    @Id(autoincrement = true)
    Long id;

    @Unique
    String username;
    String password;
    String tag;
    int userType = 0;      //0普通用户、1权限用户、2超级管理员  默认为0-普通用户

    @Generated(hash = 292792474)
    public UserDBBean(Long id, String username, String password, String tag,
            int userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tag = tag;
        this.userType = userType;
    }

    @Generated(hash = 202817274)
    public UserDBBean() {
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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
