package org.zhonghao.gps.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/11/24.
 */
public class NameDates implements Serializable{
    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {

        return passWord;
    }

    public void setPassword(String password) {
        this.passWord = password;
    }

    private String userName;
    private String passWord;

}

