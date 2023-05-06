package com.example.oneread.utils;

public final class AppConstants {

    public static final String baseUrl = "https://reading-api.onrender.com";
    public static final String DB_NAME = "BOOK_DB";
    public static final String DOWNLOAD_PATH = "/OneRead/";

    public static final int NUMBER_OF_THREADS = 4;

    public static final int MY_MICRO_REQUEST_CODE = 4;
    public static final int MY_CAMERA_REQUEST_CODE = 2;
    public static final int MY_RESULT_LOAD_IMAGE = 3;
    public static final int PICK_FILES = 5;
    public static final int LOGIN_REQUEST_CODE = 1;
    public static final int REQUEST_WRITE_EXTERNAL_CODE = 69;

    public static final String STATUS_CODE_SUCCESS = "success";
    public static final String STATUS_CODE_FAILED = "failed";

    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final long NULL_INDEX = -1L;

    public static final String TIMESTAMP_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static MODE mode = MODE.OFFLINE;

    private AppConstants() {}
}
