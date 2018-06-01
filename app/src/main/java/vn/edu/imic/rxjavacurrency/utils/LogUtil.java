package vn.edu.imic.rxjavacurrency.utils;

import android.util.Log;

/**
 * Created by MyPC on 31/05/2018.
 */

public class LogUtil {
    public static void d(String tag, String message){
        Log.d(tag, message);
    }

    public static void e(String tag, String message){
        Log.e(tag, message );
    }
}
