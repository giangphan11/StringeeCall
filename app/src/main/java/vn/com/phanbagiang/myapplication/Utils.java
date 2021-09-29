package vn.com.phanbagiang.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by giangphanba on 9/28/2021.
 */
public class Utils {
    public static void reportMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void postDelay(Runnable runnable, long delayMillis) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delayMillis);
    }
}
