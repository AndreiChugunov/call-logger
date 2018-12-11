package example.a123.test42;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class RecorderService extends Service {

    public static RecordWaveTask recordTask = null;
    static Context ctx = null;
    static File filesDir;

    public RecorderService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        recordTask = new RecordWaveTask();
        filesDir = getFilesDir();
        super.onCreate();
    }

    @Override
    public void onDestroy(){

    }

    protected static void launchTask() {
        switch (recordTask.getStatus()) {
            case RUNNING:
                Log.d("MyTag", "RecorderService.launchTask(): Task already running");
                return;
            case FINISHED:
                Log.d("MyTag", "RecorderService.launchTask(): Task finished. Starting new...");
                recordTask = new RecordWaveTask();
                Log.d("MyTag", "RecorderService.launchTask(): Task finished. New task created");
                break;
            case PENDING:
                if (recordTask.isCancelled()) {
                    Log.d("MyTag", "RecorderService.launchTask(): Task pending. Starting new...");
                    recordTask = new RecordWaveTask();
                    Log.d("MyTag", "RecorderService.launchTask(): Task pending. New task created");
                }
        }

        File wavFile = new File(filesDir, "recording_" + System.currentTimeMillis() / 1000 + ".wav");
        if (ctx!=null) {
            Toast.makeText(ctx, wavFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
        Log.d("MyTag", wavFile.getAbsolutePath());
        recordTask.execute(wavFile);
    }

}
