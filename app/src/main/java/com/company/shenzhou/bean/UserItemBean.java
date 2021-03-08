package com.company.shenzhou.bean;

/**
 * LoveLin
 * <p>
 * Describe
 */
public class UserItemBean {
    private boolean isChose;
    private String name;
    private String position;

    public boolean isChose() {
        return isChose;
    }

    public void setChose(boolean chose) {
        isChose = chose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
