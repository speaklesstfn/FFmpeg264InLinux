package com.sddd.tfn.ffmpeg264inlinux;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 单独的线程中进行转码
 * created by tfn on 2016-11-14
 */
public class ThreadActivity extends AppCompatActivity {

    private Button mTransBtn = null;
    private TextView mStartTxt = null;
    private TextView mEndTxt = null;
    private TextView mTotalTxt = null;
    private TextView mResultTxt = null;
    private RelativeLayout mWaitRL = null;

    private String basePath = "/storage/emulated/0/mydata/vivo";
    private String targetPath = basePath + File.separator + "testoo.mp4";
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
//            basePath + File.separator + "VID_20161115_135829.mp4",
//            "-b", "0.2M",
//            "-s", "720x1080",
//            "-r", "24",
//            "-c:v", "libx264",
            targetPath,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        mTransBtn = (Button) findViewById(R.id.thread_trans_btn);
        mTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadTranscodeBtnOnClick();
            }
        });

        mStartTxt = (TextView) findViewById(R.id.thread_start_time_txt);
        mEndTxt = (TextView) findViewById(R.id.thread_end_time_txt);
        mTotalTxt = (TextView) findViewById(R.id.thread_total_time_txt);

        mResultTxt = (TextView) findViewById(R.id.thread_result_txt);
        mResultTxt.setVisibility(View.INVISIBLE);

        mWaitRL = (RelativeLayout) findViewById(R.id.thread_wait_rl);
        mWaitRL.setVisibility(View.INVISIBLE);
    }

    private void threadTranscodeBtnOnClick() {
        new MyAsyncTask().execute(cmds);
    }

    class MyAsyncTask extends AsyncTask<String[], Void, Integer> {

        @Override
        protected void onPreExecute() {
            Logger.d("thread----onPreExecute()");
            FileUtils.resetFile(targetPath);
            startMill = System.currentTimeMillis();
            startTime = sFormat1.format(new Date(startMill));
            mStartTxt.setText(startTime);
            mWaitRL.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String[]... strings) {
            Logger.d("thread----doInBackground()");
            return Player.transcodeVideo(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Logger.d("thread----onPostExecute(),result=" + integer);
            mWaitRL.setVisibility(View.INVISIBLE);
            endMill = System.currentTimeMillis();
            endTime = sFormat1.format(new Date(endMill));
            totalTime = (endMill - startMill) / 1000;
            mEndTxt.setText(endTime);
            mTotalTxt.setText(String.valueOf(totalTime));
            mResultTxt.setVisibility(View.VISIBLE);
            String resultStr = "转码成功！";
            if (integer != 0) {
                resultStr = "转码失败，返回值：" + integer;
            }
            mResultTxt.setText(resultStr);
            super.onPostExecute(integer);
        }
    }
}
