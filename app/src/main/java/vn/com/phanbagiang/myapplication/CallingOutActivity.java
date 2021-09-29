package vn.com.phanbagiang.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.stringee.call.StringeeCall;
import com.stringee.common.StringeeAudioManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.phanbagiang.myapplication.databinding.ActivityCallingOutBinding;

public class CallingOutActivity extends AppCompatActivity {
    private static final String TAG = "CALL_";
    ActivityCallingOutBinding binding = null;

    private StringeeCall stringeeCall;
    private StringeeAudioManager audioManager;

    private boolean isSpeaker = false;

    Timer T = new Timer();
    int count = 0;

    private PowerManager.WakeLock mWakeLock = null;

    public static final int REQUEST_PERMISSION_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallingOutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppConstants.isInCall = true;
        initProximitySensor();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        String userIDCall = getIntent().getStringExtra("KEY_USER");

        binding.tvUserName.setText(userIDCall);
        stringeeCall = new StringeeCall(MyApplication.stringeeClient, MyApplication.stringeeClient.getUserId(), userIDCall);
        audioManager = StringeeAudioManager.create(this);
        audioManager.start(new StringeeAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(StringeeAudioManager.AudioDevice audioDevice, Set<StringeeAudioManager.AudioDevice> set) {
                Log.d(TAG, "audioDevice OUT: ");
            }
        });
        audioManager.setSpeakerphoneOn(isSpeaker);
        stringeeCall.setVideoCall(false);
        stringeeCall.setCallListener(new StringeeCall.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s, int i, String s1) {
                Log.d(TAG, "onSignalingStateChange: ");
                switch (signalingState) {
                    case BUSY:
                        Log.d(TAG, "onSignalingStateChange: BUSY");
                        finish();
                        break;
                    case ENDED:
                        Log.d(TAG, "onSignalingStateChange: ENDED");
                        finish();
                        break;
                    case CALLING:
                        Log.d(TAG, "onSignalingStateChange: CALLING");
                        break;
                    case RINGING:
                        Log.d(TAG, "onSignalingStateChange: RINGING");
                        break;
                    case ANSWERED:
                        Log.d(TAG, "onSignalingStateChange: ANSWERED");
                        T.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.tvTimeCunt.setText("Time=" + count);
                                        count++;
                                    }
                                });
                            }
                        }, 1000, 1000);
                        break;
                }
            }

            @Override
            public void onError(StringeeCall stringeeCall, int i, String s) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onHandledOnAnotherDevice(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s) {
                Log.d(TAG, "onHandledOnAnotherDevice: ");
            }

            @Override
            public void onMediaStateChange(StringeeCall stringeeCall, StringeeCall.MediaState mediaState) {
                Log.d(TAG, "onMediaStateChange: ");
            }

            @Override
            public void onLocalStream(StringeeCall stringeeCall) {
                Log.d(TAG, "onLocalStream: ");
            }

            @Override
            public void onRemoteStream(StringeeCall stringeeCall) {
                Log.d(TAG, "onRemoteStream: ");
            }

            @Override
            public void onCallInfo(StringeeCall stringeeCall, JSONObject jsonObject) {
                Log.d(TAG, "onCallInfo: ");
            }
        });
        stringeeCall.makeCall();

        binding.btnEndCall.setOnClickListener(v -> {
            audioManager.stop();
            stringeeCall.hangup();
            finish();
        });

        binding.imgSpeaker.setOnClickListener(v -> {
            isSpeaker = !isSpeaker;
            audioManager.setSpeakerphoneOn(isSpeaker);
            binding.imgSpeaker.setBackground(isSpeaker ? ContextCompat.getDrawable(this, R.drawable.ic_music_on) : ContextCompat.getDrawable(this, R.drawable.ic_music_off));
        });
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> lstPermissions = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                lstPermissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (stringeeCall.isVideoCall()) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    lstPermissions.add(Manifest.permission.CAMERA);
                }
            }

            if (lstPermissions.size() > 0) {
                String[] permissions = new String[lstPermissions.size()];
                for (int i = 0; i < lstPermissions.size(); i++) {
                    permissions[i] = lstPermissions.get(i);
                }
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CALL);
                return;
            }
        }
    }

    private void endCall() {
        stringeeCall.hangup();
        if (audioManager != null) {
            audioManager.stop();
            audioManager = null;
        }
        Utils.postDelay(new Runnable() {
            @Override
            public void run() {
                AppConstants.isInCall = false;
                finish();
            }
        }, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        T.cancel();
        if (mWakeLock != null && mWakeLock.isHeld()){
            mWakeLock.release();
        }
    }

    private void initProximitySensor(){
        if (mWakeLock == null || !mWakeLock.isHeld()){
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "CALL_:wake_lock");
            mWakeLock.acquire(10 * 60 * 1000L);
        }
    }
}