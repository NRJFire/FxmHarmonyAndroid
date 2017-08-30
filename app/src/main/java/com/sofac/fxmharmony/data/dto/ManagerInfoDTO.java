package com.sofac.fxmharmony.data.dto;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import java.io.Serializable;
import java.util.Date;

public class ManagerInfoDTO extends SugarRecord implements Serializable {

    public ManagerInfoDTO(Long idServer, String name, String phone, String email, Date birthday, PermissionDTO permissions, String avatarImage) {
        this.idServer = idServer;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.permissions = permissions;
        this.avatarImage = avatarImage;
    }

    public ManagerInfoDTO() {
    }

    private Long idServer;

    private String name;

    private String phone;

    private String email;

    private Date birthday;

    @Ignore
    private PermissionDTO permissions;

    private String avatarImage;

    public PermissionDTO getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionDTO permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public Long getIdServer() {
        return idServer;
    }

    public void setIdServer(Long idServer) {
        this.idServer = idServer;
    }

    @Override
    public String toString() {
        return "ManagerInfoDTO{" +
                "idServer=" + idServer +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", permissions=" + permissions +
                ", avatarImage='" + avatarImage + '\'' +
                '}';
    }
}
