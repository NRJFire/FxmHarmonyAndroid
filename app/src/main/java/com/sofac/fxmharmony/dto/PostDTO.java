package com.sofac.fxmharmony.dto;

import android.text.TextUtils;

import com.sofac.fxmharmony.util.TypeFiles;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.sofac.fxmharmony.Constants.SPLIT_FILES;

public class PostDTO implements Serializable {

    private Long id;
    private Long user_id;
    private String name;
    private String date;
    private String body_original;
    private String body_ru;
    private String body_en;
    private String body_ko;
    private String files;
    private String avatar;
    private String type;

    public PostDTO(Long id, Long user_id, String name, String date, String body_original, String body_ru, String body_en, String body_ko, String files, String avatar, String type) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.date = date;
        this.body_original = body_original;
        this.body_ru = body_ru;
        this.body_en = body_en;
        this.body_ko = body_ko;
        this.files = files;
        this.avatar = avatar;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateParser.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody_original() {
        return body_original;
    }

    public void setBody_original(String body_original) {
        this.body_original = body_original;
    }

    public String getBody_ru() {
        return body_ru;
    }

    public void setBody_ru(String body_ru) {
        this.body_ru = body_ru;
    }

    public String getBody_en() {
        return body_en;
    }

    public void setBody_en(String body_en) {
        this.body_en = body_en;
    }

    public String getBody_ko() {
        return body_ko;
    }

    public void setBody_ko(String body_ko) {
        this.body_ko = body_ko;
    }

    private String[] getFiles() {
        //Timber.e("!! GET FILES !!!" + Arrays.toString(TextUtils.split(files, SPLIT_FILES)));
        return TextUtils.split(files, SPLIT_FILES);
    }

    private void setFiles(String[] files) {
        this.files = TextUtils.join(SPLIT_FILES, files);
    }


    public ArrayList<String> getImages() {
        return new TypeFiles(getFiles()).getImages();
    }

    public void setImages() {
    }

    public ArrayList<String> getMovies() {
        return new TypeFiles(getFiles()).getMovies();
    }

    public void setMovies() {
    }

    public ArrayList<String> getDocs() {
        return new TypeFiles(getFiles()).getDocs();
    }

    public void setDocs() {
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", body_original='" + body_original + '\'' +
                ", body_ru='" + body_ru + '\'' +
                ", body_en='" + body_en + '\'' +
                ", body_ko='" + body_ko + '\'' +
                ", files='" + files + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
