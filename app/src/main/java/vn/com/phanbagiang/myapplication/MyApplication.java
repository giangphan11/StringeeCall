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

    public static StringeeClient stringeeClient;

    public static HashMap<String, StringeeCall> callsMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        stringeeClient = new StringeeClient(this);
        initStringClient();
    }

    private void initStringClient(){
        stringeeClient.setConnectionListener(new StringeeConnectionListener() {
            @Override
            public void onConnectionConnected(StringeeClient stringeeClient, boolean b) {
                Log.d(TAG, "onConnectionConnected: ");
                String tokenFirebase = getSharedPreferences(MyFirebaseMessagingService.KEY_SHARE, MODE_PRIVATE).getString(MyFirebaseMessagingService.KEY_TOKEN, "");
                Log.d(TAG, "onConnectionConnected: TOKEN = "+tokenFirebase);
                stringeeClient.registerPushToken(tokenFirebase, new StatusListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "REGISTER TOKEN FIREBASE SUCCESS");
                    }
                    @Override
                    public void onError(StringeeError stringeeError) {
                        Log.d(TAG, "registerPushToken ERR: "+stringeeError.message);
                    }
                });
            }

            @Override
            public void onConnectionDisconnected(StringeeClient stringeeClient, boolean b) {
                Log.d(TAG, "onConnectionDisconnected: ");
            }

            @Override
            public void onIncomingCall(StringeeCall stringeeCall) {
                try{
                    Log.d(TAG, "onIncomingCall: ");
                    MyApplication.callsMap.put(stringeeCall.getCallId(), stringeeCall);
                    Log.d(TAG, "onIncomingCall:OK");
//                    Log.d(TAG, "onIncomingCall: ");
//                    Intent intent = new Intent(getApplicationContext(), CallingInActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("CALL_ID", stringeeCall.getCallId());
//                    startActivity(intent);
                }
                catch (Exception ex){
                    Log.d(TAG, "onIncomingCall: "+ex.getMessage());
                }
            }

            @Override
            public void onIncomingCall2(StringeeCall2 stringeeCall2) {
                Log.d(TAG, "onIncomingCall2: ");
            }

            @Override
            public void onConnectionError(StringeeClient stringeeClient, StringeeError stringeeError) {
                Log.d(TAG, "onConnectionError: ");
            }

            @Override
            public void onRequestNewToken(StringeeClient stringeeClient) {
                Log.d(TAG, "onRequestNewToken: ");
            }

            @Override
            public void onCustomMessage(String s, JSONObject jsonObject) {
                Log.d(TAG, "onCustomMessage: ");
            }

            @Override
            public void onTopicMessage(String s, JSONObject jsonObject) {
                Log.d(TAG, "onTopicMessage: ");
            }
        });
        stringeeClient.connect(TOKEN3);

// tạo intent và set hành động
//

    }
}
