package com.sofac.fxmharmony;

public class Constants {

    public static final String CLOUD_API_KEY = "AIzaSyDZJmgv9fvQnQkc5vvCx8bPFcUqSYDVhnM";

    /*Имя хранения preference*/
    public final static String IS_AUTHORIZATION = "IS_AUTHORIZATION";

    public final static String GOOGLE_CLOUD_PREFERENCE = "GOOGLE_CLOUD_PREFERENCE";
    public static final String APP_PREFERENCES = "APP_PREFERENCES";

    public final static String USER_ID_PREF = "USER_ID_PREF";
    public final static String ONE_PUSH_MESSAGE_DATA = "ONE_PUSH_MESSAGE_DATA";
    public final static String ONE_POST_MESSAGE_DATA = "ONE_POST_MESSAGE_DATA";
    public final static String POST_ID = "POST_ID";

    public final static String PUSH_MESSAGES_STATE = "PUSH_MESSAGES_STATE";

    //public final static String BASE_URL = "http://52.211.242.225:8080/"; // a.sofac.kr
    //public final static String BASE_URL = "http://www.sofac.kr/"; // http://swfac.kr/
    //public final static String BASE_URL = "http://52.57.116.160:8080/"; //Test server
    public final static String BASE_URL = "http://192.168.0.58/"; //Zegna
    //public final static String BASE_URL = "http://192.168.1.5/"; //FreeMax
    //public final static String BASE_URL = "http://192.168.1.36/"; //Maxx

    public final static String PART_CONTROLLER = "data/ajax/request.php"; //Part app controller php

    //File path
    public final static String PART_ESTIMATE = "data/files/estimate/"; //Part estimate
    public final static String PART_MESSAGE = "data/files/message/"; //Part message
    public final static String PART_POST = "data/files/post/"; //Part post
    public final static String PART_TOSS = "data/files/toss/"; //Part toss
    public final static String PART_AVATAR = "control/design/images/avatar/"; //Part toss

    public final static String SPLIT_FILES = ";"; //Split files from server

    //PreviewPhotoActivity
    public final static String LINK_IMAGE = "LINK_IMAGE";
    public final static String NAME_IMAGE = "NAME_IMAGE";

    public final static String LINK_VIDEO = "LINK_VIDEO";
    public final static String NAME_VIDEO = "NAME_VIDEO";

    //NavActivity
    public final static String TYPE_GROUP = "TYPE_GROUP";

    // Push message state
    public final static int PUSH_ON = 10001;
    public final static int PUSH_OFF = 10002;

    public final static int REQUEST_TAKE_FILE = 11111;
    public final static int REQUEST_TAKE_GALLERY_VIDEO = 11112;
    public final static int REQUEST_TAKE_PHOTO = 11113;

    public final static int AVATAR_IMAGE_SIZE = 128;

}
