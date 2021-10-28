package vn.com.phanbagiang.myapplication.events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.view.View;

import vn.com.phanbagiang.myapplication.R;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    public void quaylai(View view) {
        setResult(11);
        finish();
    }

    public void startWorker(View view) {
        Constraints.Builder builder = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED);

        // Passing params
        Data.Builder data = new Data.Builder();
        data.putInt("GIANG_NUM", 113);

        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(MyWorker.class).
                        addTag("Sync")
                        .setInputData(data.build())
                        .setConstraints(builder.build())
                        .build();

//        OneTimeWorkRequest syncWorkRequest =
//                new OneTimeWorkRequest.Builder(MyWorker.class)
//                        .addTag("Sync")
//                        .setInputData(data.build())
//                        .setConstraints(builder.build())
//                        .build();

        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
    }
}