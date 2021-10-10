package vn.com.phanbagiang.myapplication.events;

import androidx.appcompat.app.AppCompatActivity;

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
}