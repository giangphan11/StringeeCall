package vn.com.phanbagiang.myapplication;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StatusListener;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONObject;

import java.util.HashMap;

import vn.com.phanbagiang.myapplication.firebase.MyFirebaseMessagingService;

/**
 * Created by giangphanba on 9/23/2021.
 */
public class MyApplication extends Application {

    private static final String TAG = "CALL_";
    public static String STARTFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.startforeground";
    public static final String TOKEN1 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhLTE2MzIzODk3NjYiLCJpc3MiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhIiwiZXhwIjoxNjM0OTgxNzY2LCJ1c2VySWQiOiJ1c2VyMSJ9.YDbAFuFJMGb3aapPbVtpr9mdiAdstlol3BqnXSqAq74";
    public static final String TOKEN2 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhLTE2MzIzOTA3NDEiLCJpc3MiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhIiwiZXhwIjoxNjM0OTgyNzQxLCJ1c2VySWQiOiJ1c2VyMiJ9.U1WSBGYhtDsraXSBEQATjtBv7VOycI8zIQ0hdHvYDXw";
    public static final String TOKEN3 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhLTE2MzI3OTQzNDgiLCJpc3MiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhIiwiZXhwIjoxNjM1Mzg2MzQ4LCJ1c2VySWQiOiJ1c2VyMyJ9.2B9ma2FcOIn58EGVlDoGBZds8ghlV03SZXsgli2eAEM";
    public static final String TOKEN4 = "eyJjdHkiOiJzdHJpbmdlZS1hcGk7dj0xIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhLTE2MzI3OTQzNzQiLCJpc3MiOiJTS3Q1QUZoVHd4RUtKZjg0amw4RlU3UUlFUGFxMlE0STRhIiwiZXhwIjoxNjM1Mzg2Mzc0LCJ1c2VySWQiOiJ1c2VyNCJ9.UhzF6x97mqeQ0RWGQZ5d9R1iGEGlYmPWw_ow0znQCtA";

    public static boolean isStartFromSplash = false;
    public static boolean isActive = false;
    public static boolean isCalling = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
