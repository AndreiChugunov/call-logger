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

    protected static void launchTask() {
        switch (recordTask.getStatus()) {
            case RUNNING:
                Log.d("MyTag", "Task already running");
//                Toast.makeText(ctx, "Task already running...", Toast.LENGTH_SHORT).show();
                return;
            case FINISHED:
                recordTask = new RecordWaveTask();
                break;
            case PENDING:
                if (recordTask.isCancelled()) {
                    recordTask = new RecordWaveTask();
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
