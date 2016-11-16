package com.sddd.tfn.ffmpeg264inlinux;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mThreadTransBtn = null;
    private Button mProcessTransBtn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                200);
        mThreadTransBtn = (Button) findViewById(R.id.trans_in_thread_btn);
        mThreadTransBtn.setOnClickListener(this);

        mProcessTransBtn = (Button) findViewById(R.id.trans_in_process_btn);
        mProcessTransBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trans_in_thread_btn:
                threadBtnOnClick();
                break;
            case R.id.trans_in_process_btn:
                processBtnOnClick();
            default:
                break;
        }
    }

    private void threadBtnOnClick() {
        startActivity(new Intent(this, ThreadActivity.class));
    }

    private void processBtnOnClick() {
        startActivity(new Intent(this, ProcessActivity.class));
    }
}
