package vn.com.phanbagiang.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import vn.com.phanbagiang.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CALL_";

    private ActivityMainBinding binding = null;

    private CompositeDisposable disposable = new CompositeDisposable();

    private int CHECK_BATTERY_OPTIMIZE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();
        //checkForBatteryOptimizations();
    }

    private void addEvents() {
        binding.btnShowUserId.setOnClickListener(v -> {
            if (MyApplication.stringeeClient.isConnected())
                binding.textView.setText(MyApplication.stringeeClient.getUserId());
            else
                binding.textView.setText("Hok co ket noi!!!");
        });

        binding.btnUser1.setOnClickListener(v -> {
            if (MyApplication.stringeeClient.isConnected()) {
                MyApplication.stringeeClient.disconnect();
            }
            MyApplication.stringeeClient.connect(MyApplication.TOKEN1);
            Toast.makeText(this, "switch to User1", Toast.LENGTH_SHORT).show();
        });

        binding.btnUser2.setOnClickListener(v -> {
            if (MyApplication.stringeeClient.isConnected()) {
                MyApplication.stringeeClient.disconnect();
            }
            MyApplication.stringeeClient.connect(MyApplication.TOKEN4);
            Toast.makeText(this, "switch to User2", Toast.LENGTH_SHORT).show();
        });

        binding.btnCall.setOnClickListener(v -> {
            if (MyApplication.stringeeClient.isConnected()) {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECK_BATTERY_OPTIMIZE){
            //checkForBatteryOptimizations();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: MAIN");
    }
}