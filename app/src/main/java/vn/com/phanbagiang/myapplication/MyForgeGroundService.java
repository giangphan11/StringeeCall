package vn.com.phanbagiang.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONObject;


/**
 * Created by giangphanba on 9/23/2021.
 */
public class MyForgeGroundService extends Service {

    private static final String TAG = "CALL_";
    private static final String CHANNEL_ID = "113";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");


        // kiểm tra và gán hành động cho service
        if (intent.getAction().equals(AppConstants.Action.STARTFOREGROUND_ACTION)) {
            Log.d(TAG, "STARTFOREGROUND_ACTION: ");
            // code here

            MyApplication.stringeeClient.setConnectionListener(new StringeeConnectionListener() {
                @Override
                public void onConnectionConnected(StringeeClient stringeeClient, boolean b) {
                    Log.d(TAG, "onConnectionConnected: ");
                }

                @Override
                public void onConnectionDisconnected(StringeeClient stringeeClient, boolean b) {
                    Log.d(TAG, "onConnectionDisconnected: ");
                }

                @Override
                public void onIncomingCall(StringeeCall stringeeCall) {
                    try {
                        Log.d(TAG, "onIncomingCall: ");
//                        if (AppConstants.isInCall){
//                            stringeeCall.reject();
//                            Log.d(TAG, "onIncomingCall: reject");
//                            return;
//                        }
                        MyApplication.callsMap.put(stringeeCall.getCallId(), stringeeCall);
                        Intent intent = new Intent(getApplicationContext(), CallingInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("CALL_ID", stringeeCall.getCallId());
                        startActivity(intent);
                    } catch (Exception ex) {
                        Log.d(TAG, "onIncomingCall: " + ex.getMessage());
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
            MyApplication.stringeeClient.connect(MyApplication.TOKEN1);


            // tạo activity lúc ấn vào thông báo
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, 0);

            // tạo thông báo
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    this, "chanel_forgeBackground");
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_back_blue_24)
                    .setContentTitle("App is running on foreground")
                    .setPriority(NotificationManager.IMPORTANCE_LOW)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setChannelId("chanel_forgeBackground")
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setContentText("when an unknown printer took a galley of type and scrambled")
                    .build();

            // thông channel thông báo từ android O (8) trở lên
            createNotificationChannel();

            // kích hoạt thông báo chạy nền (forge ground)
            startForeground(AppConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);


        }
        // stop
        else if (intent.getAction().equals(AppConstants.Action.STOPFOREGROUND_ACTION)) {
            Log.d(TAG, "STOPFOREGROUND_ACTION: ");
            // dừng service
            stopForeground(true);
            stopSelfResult(startId);
        }
        return START_STICKY;
    }
}
