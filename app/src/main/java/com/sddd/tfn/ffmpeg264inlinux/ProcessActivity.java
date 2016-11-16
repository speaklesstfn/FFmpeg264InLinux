package com.sddd.tfn.ffmpeg264inlinux;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 单独的进程中进行转码
 * created by tfn on 2016-11-14
 */
public class ProcessActivity extends AppCompatActivity {

    private Button mTransBtn = null;
    private TextView mStartTxt = null;
    private TextView mEndTxt = null;
    private TextView mTotalTxt = null;
    private TextView mResultTxt = null;
    private RelativeLayout mWaitRL = null;

    private String basePath = "/storage/emulated/0";
    private String targetPath = basePath + File.separator + "out_.mp4";
    private SimpleDateFormat sFormat1 = new SimpleDateFormat("HH:mm:ss");
    private String startTime = "00:00:00";
    private String endTime = "00:00:00";
    private long startMill = 0L;
    private long endMill = 0L;
    private long totalTime = 0L;

    private String[] cmds = {
            "ffmpeg",
            "-i",
            basePath + File.separator + "video_20161111_164706.mp4",
            "-b",
            "0.2M",
            "-s",
            "720x1080",
            "-r", "24",
            "-c:v",
            "libx264",
            targetPath,
    };


    private ITranscodeAidlInterface aidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        mTransBtn = (Button) findViewById(R.id.process_trans_btn);
        mTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processTranscodeBtnOnClick();
            }
        });

        mStartTxt = (TextView) findViewById(R.id.process_start_time_txt);
        mEndTxt = (TextView) findViewById(R.id.process_end_time_txt);
        mTotalTxt = (TextView) findViewById(R.id.process_total_time_txt);

        mResultTxt = (TextView) findViewById(R.id.process_result_txt);
        mResultTxt.setVisibility(View.INVISIBLE);

        mWaitRL = (RelativeLayout) findViewById(R.id.process_wait_rl);
        mWaitRL.setVisibility(View.INVISIBLE);

    }

    private void processTranscodeBtnOnClick() {
        Logger.d("process----start button on click");
        FileUtils.resetFile(targetPath);
        startMill = System.currentTimeMillis();
        startTime = sFormat1.format(new Date(startMill));
        mStartTxt.setText(startTime);
        mWaitRL.setVisibility(View.VISIBLE);

        Intent intent = new Intent(getApplicationContext(), TranscodeService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            aidlInterface = ITranscodeAidlInterface.Stub.asInterface(service);
            List<String> commands = Arrays.asList(cmds);
            int ret = -1;
            try {
                ret = aidlInterface.transcode(commands);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Logger.d("process----transcode result=" + ret);
            mWaitRL.setVisibility(View.INVISIBLE);
            endMill = System.currentTimeMillis();
            endTime = sFormat1.format(new Date(endMill));
            totalTime = (endMill - startMill) / 1000;
            mEndTxt.setText(endTime);
            mTotalTxt.setText(String.valueOf(totalTime));
            mResultTxt.setVisibility(View.VISIBLE);
            String resultStr = "转码成功！";
            if (ret != 0) {
                resultStr = "转码失败，返回值：" + ret;
            }
            mResultTxt.setText(resultStr);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlInterface = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aidlInterface != null) {
            unbindService(sc);
        }
    }
}
