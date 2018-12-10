package example.a123.test42;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_RECORD_AUDIO = 0;

    public Context getContext(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                &&
            (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
        {
            // Request permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE },
                    PERMISSION_RECORD_AUDIO);
            return;
        }
        // Permission already available
        //startService(new Intent(this, RecorderService.class));
        startService(new Intent(this, RecordedCallService.class));
    }

//    private void checkTask()
//    {
//        recordTask = (RecordWaveTask) getLastCustomNonConfigurationInstance();
//        if (recordTask == null) {
//            recordTask = new RecordWaveTask(this);
//        } else {
//            recordTask.setContext(this);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    //launchTask();
                    //запустить демона
                } else {
                    startService(new Intent(this, RecorderService.class));
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}