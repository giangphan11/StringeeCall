package vn.com.phanbagiang.myapplication.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.GsonBuilder;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StatusListener;

import vn.com.phanbagiang.myapplication.CallingInActivity;
import vn.com.phanbagiang.myapplication.MyApplication;

/**
 * Created by giangphanba on 10/3/2021.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "CALL_";

    public static final String KEY_SHARE  = "KEY_SHARE";
    public static final String KEY_TOKEN  = "KEY_SHARE";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        getSharedPreferences(KEY_SHARE, MODE_PRIVATE).edit().putString(KEY_TOKEN, token).apply();
        MyApplication.stringeeClient.registerPushToken(token, new StatusListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "registerPushToken Success: ");
            }

            @Override
            public void onError(StringeeError stringeeError) {
                Log.d(TAG, "registerPushToken ERR: "+stringeeError.message);
            }
        });
    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            String pushFromStringee = remoteMessage.getData().get("data");
            if (pushFromStringee != null) { // Receive push notification from Stringee Server
                // Connect to Stringee Server here
                Notifi notifi = new GsonBuilder().create().fromJson(pushFromStringee, Notifi.class);
                Log.d(TAG, "ZZZZZZZZZZZZZZ"+notifi.getCallId());
                if (notifi.getCallStatus().equals("started")){
                    Log.d(TAG, "onIncomingCall: ");
                    Intent intent = new Intent(getApplicationContext(), CallingInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("CALL_ID", notifi.getCallId());
                    startActivity(intent);
                }

            }
        }
    }
}
