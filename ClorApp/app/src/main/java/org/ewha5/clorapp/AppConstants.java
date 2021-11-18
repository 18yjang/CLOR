package org.ewha5.clorapp;

import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;

public class AppConstants {


    public static final int REQUEST_CODE_BUTTON1 = 301;
    public static final int REQUEST_CODE_BUTTON2 = 302;
    public static final int REQUEST_CODE_BUTTON3 = 303;


    public static final int REQ_SHOW_COMBINATION = 101;
    public static final int REQ_SHOW_COLORS = 102;

    public static final int REQ_PHOTO_CAPTURE = 103;
    public static final int REQ_PHOTO_SELECTION = 104;

    public static final int CONTENT_PHOTO = 105;
    public static final int CONTENT_PHOTO_EX = 106;

    public static final int FINAL_FIGURE = 200;
    public static final int MAIN = 201;
    public static final int STATICS = 202;

    public static String FOLDER_PHOTO;

    public static final String KEY_URI_PHOTO = "URI_PHOTO";

    public static String DATABASE_NAME = "clor.db";

    public static final int MODE_INSERT = 1;
    public static final int MODE_MODIFY = 2;


    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH시");
    public static SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM월 dd일");
    public static SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormat5 = new SimpleDateFormat("yyyy-MM-dd");


    private static Handler handler = new Handler();
    private static final String TAG = "AppConstants";
    public static void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, data);
            }
        });
    }


}
