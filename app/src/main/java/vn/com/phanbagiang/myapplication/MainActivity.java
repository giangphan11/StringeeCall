package vn.com.phanbagiang.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StatusListener;
import com.stringee.listener.StringeeConnectionListener;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

import vn.com.phanbagiang.myapplication.databinding.ActivityMainBinding;
import vn.com.phanbagiang.myapplication.firebase.MyFirebaseMessagingService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CALL_";

    private ActivityMainBinding binding = null;

    private CompositeDisposable disposable = new CompositeDisposable();

    public static StringeeClient stringeeClient;

    private int CHECK_BATTERY_OPTIMIZE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: Main");
        initStringee();
        addEvents();
    }

    int dem;



    private void initStringee(){
        stringeeClient = new StringeeClient(this);
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
                    Log.d(TAG, "onIncomingCall: MAIN");
                    dem++;
                    if (dem ==1){
                        Utils.callsMap.put(stringeeCall.getCallId(), stringeeCall);
                        Intent intent = new Intent(getApplicationContext(), CallingInActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("CALL_ID", stringeeCall.getCallId());
                        startActivityForResult(intent, 123);
                        Log.d(TAG, "GO TO CALL OUT");
                    }
                    Log.d(TAG, "onIncomingCall: DEM ="+dem);
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
                Log.d(TAG, "onConnectionError: "+stringeeError.message);
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
        stringeeClient.connect(MyApplication.TOKEN3);
    }

    private void addEvents() {
        binding.btnShowUserId.setOnClickListener(v -> {
            if (stringeeClient.isConnected())
                binding.textView.setText(stringeeClient.getUserId());
            else
                binding.textView.setText("Hok co ket noi!!!");
        });

        binding.btnUser1.setOnClickListener(v -> {
            if (stringeeClient.isConnected()) {
                stringeeClient.disconnect();
            }
            stringeeClient.connect(MyApplication.TOKEN1);
            Toast.makeText(this, "switch to User1", Toast.LENGTH_SHORT).show();
        });

        binding.btnUser2.setOnClickListener(v -> {
            if (stringeeClient.isConnected()) {
                stringeeClient.disconnect();
            }
            stringeeClient.connect(MyApplication.TOKEN4);
            Toast.makeText(this, "switch to User2", Toast.LENGTH_SHORT).show();
        });

        binding.btnCall.setOnClickListener(v -> {
            if (stringeeClient.isConnected()) {
                Intent intent = new Intent(this, CallingOutActivity.class);
                intent.putExtra("KEY_USER", binding.etUser.getText().toString().trim());
                startActivity(intent);
            } else
                binding.textView.setText("Hok co ket noi!!!");


        });

        binding.btnStartForge.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, MyForgeGroundService.class);
            intent.setAction(AppConstants.Action.STARTFOREGROUND_ACTION);
            // khởi chạy dịch vụ nền
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        });

        binding.btnStopBackground.setOnClickListener(v ->{
            // lấy tạo intent và set hành động đóng dịch vụ
            Intent stopIntent = new Intent(MainActivity.this, MyForgeGroundService.class);
            stopIntent.setAction(AppConstants.Action.STOPFOREGROUND_ACTION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(stopIntent);
            } else {
                startService(stopIntent);
            }
        });
        binding.btnCloseApp.setOnClickListener(v->{
//            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//            homeIntent.addCategory( Intent.CATEGORY_HOME );
//            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(homeIntent);
            finishAndRemoveTask();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 115 && requestCode == 123) {
            if (MyApplication.isCalling){
                finishAndRemoveTask();
            }
        }
    }

    private void checkForBatteryOptimizations(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning");
                builder.setMessage("Che do pin dang bat tiet kiem pin, vui long tat no de chay ngam");

                builder.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        }
    }

    @Override
    protected void onStop() {
        dem = 0;
        super.onStop();
        disposable.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dem = 0;
        Log.d(TAG, "onStart: MAIN");
        MyApplication.isActive = true;
    }

    @Override
    protected void onDestroy() {
        MyApplication.isCalling = false;
        MyApplication.isActive = false;
        dem = 0;
        Log.d(TAG, "onDestroy: MAIN");
        super.onDestroy();
    }
}