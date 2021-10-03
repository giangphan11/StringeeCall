package vn.com.phanbagiang.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.common.StringeeAudioManager;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StatusListener;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.phanbagiang.myapplication.databinding.ActivityCallingInBinding;

public class CallingInActivity extends AppCompatActivity  {
    private static final String TAG = "CALL_";

    private ActivityCallingInBinding binding = null;

    private StringeeCall stringeeCall;
    private StringeeAudioManager audioManager;

    private boolean isSpeaker = false;

    // sound

    AudioManager am = null;

    MediaPlayer mp = null;

    private PowerManager.WakeLock mWakeLock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppConstants.isInCall = true;

        //player.set
        activeRingTune();
        addLockScreenFlags();
        initProximitySensor();

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        String userID = getIntent().getStringExtra("CALL_ID");
        stringeeCall = MyApplication.callsMap.get(userID);
        binding.tvUser.setText(userID);

        stringeeCall.setCallListener(new StringeeCall.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s, int i, String s1) {
                switch (signalingState) {
                    case BUSY:
                        Log.d(TAG, "onSignalingStateChange: BUSY");
                        break;
                    case ENDED:
                        Log.d(TAG, "onSignalingStateChange: ENDED");
                        stopRingtone(false);
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
                        break;
                }
            }

            @Override
            public void onError(StringeeCall stringeeCall, int i, String s) {
                Log.d(TAG, "onError: "+s);
            }

            @Override
            public void onHandledOnAnotherDevice(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s) {
                Log.d(TAG, "onHandledOnAnotherDevice: ");
                if (signalingState == StringeeCall.SignalingState.ANSWERED || signalingState == StringeeCall.SignalingState.BUSY) {
                    Utils.reportMessage(getApplicationContext(), "This call is handled on another device.");
                }
            }

            @Override
            public void onMediaStateChange(StringeeCall stringeeCall, StringeeCall.MediaState mediaState) {
                Log.d(TAG, "onMediaStateChange: "+ mediaState);
                switch (mediaState) {
                    case CONNECTED:
                        Log.d(TAG, "onMediaStateChange: CONNECTED");
                        break;
                    case DISCONNECTED:
                        Log.d(TAG, "onMediaStateChange: DISCONNECTED");
                        break;
                }
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

        addEvents();
    }

    private void initProximitySensor(){
        if (mWakeLock == null || !mWakeLock.isHeld()){
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "CALL_:wake_lock");
            mWakeLock.acquire(10 * 60 * 1000L);
        }
    }

    private void initAudio(){
        // Initialize audio manager to manage the audio routing
        audioManager = StringeeAudioManager.create(this);
        audioManager.start(new StringeeAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(StringeeAudioManager.AudioDevice selectedAudioDevice, Set<StringeeAudioManager.AudioDevice> availableAudioDevices) {
            }
        });
        //audioManager.setSpeakerphoneOn(false); // false: Audio Call, true: Video Call
        audioManager.audioManager.setMode(AudioManager.MODE_NORMAL);
        stringeeCall.ringing(new StatusListener() {
            @Override
            public void onSuccess() {
            }
        });
    }

    private void addLockScreenFlags(){
        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        else{

            w.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            w.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            w.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            KeyguardManager KeyguardManager = (KeyguardManager) (getSystemService(Context.KEYGUARD_SERVICE));
            KeyguardManager.requestDismissKeyguard(this, null);
        }
        else{
            w.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
    }

    private void addEvents(){
        binding.btnCancel.setOnClickListener(v ->{
            stringeeCall.hangup();
            if (audioManager != null){
                audioManager.stop();
            }
            finish();
        });

        binding.btnAnswer.setOnClickListener(v->{
            initAudio();
            if(audioManager!= null){
                audioManager.setSpeakerphoneOn(isSpeaker);
            }
            stopRingtone(false);
            stringeeCall.answer();
        });

        binding.imgSpeaker.setOnClickListener(v ->{
            isSpeaker = !isSpeaker;
            audioManager.setSpeakerphoneOn(isSpeaker);
        });
    }


    private void activeRingTune(){
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_NORMAL);
        mp = new MediaPlayer();
        try {
            mp.setDataSource(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            mp.setAudioStreamType(AudioManager.STREAM_RING);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            //exception ca
            // ught in the end zone
        }
    }

    private void stopRingtone(boolean isRelease){
        if (mp != null && mp.isPlaying()){
            mp.stop();
            if (isRelease) mp = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRingtone(true);
        if (audioManager != null){
            audioManager.stop();
            audioManager.audioManager.setMode(AudioManager.MODE_NORMAL);
        }
        if (mWakeLock.isHeld()){
            mWakeLock.release();
        }
        //finishAndRemoveTask();
    }

}