package com.sofac.fxmharmony.dto;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Maxim on 31.08.2017.
 */

public class ManagerDTO extends SugarRecord implements Serializable {

    private String name;
    private String email;
    private String phone;
    private String access;

    public ManagerDTO(){}

    public ManagerDTO(String name, String email, String phone, String access) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.access = access;
    }

    public String getName() {
        if (name != null) return name;
        else return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        if (email != null) return email;
        else return "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        if (phone != null) return phone;
        else return "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setAccess(String access) {
        this.access = access;
    }

    public String getAccess() {
        if (access != null) return access;
        else return "";
    }


    @Override
    public String toString() {
        return "ManagerDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", access='" + access + '\'' +
                '}';
    }
}
