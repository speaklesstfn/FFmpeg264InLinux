package com.sddd.tfn.ffmpeg264inlinux;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.orhanobut.logger.Logger;

import java.util.List;

public class TranscodeService extends Service {
    ITranscodeAidlInterface.Stub aidlInterface = new ITranscodeAidlInterface.Stub() {
        @Override
        public int transcode(List<String> commands) throws RemoteException {
            return TranscodeService.this.transcode(commands);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("TranscodeService:onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("TranscodeService:onBind()");
        return aidlInterface;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d("TranscodeService:onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("TranscodeService:onDestroy()");
    }

    private int transcode(List<String> commands) {
        String[] cmds = new String[commands.size()];
        commands.toArray(cmds);
        for (String cmd : cmds) {
            Logger.d("TranscodeService,transcode cmd: " + cmd);
        }
        int ret = Player.transcodeVideo(cmds);
        Logger.d("TranscodeService,transcode result:" + ret);
        return ret;
    }
}
