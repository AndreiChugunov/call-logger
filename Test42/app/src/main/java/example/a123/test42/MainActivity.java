package example.a123.test42;

import android.app.ActivityManager;
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
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_GRANTED = 0;
    private Button btn;

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
                    new String[]
                            {
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },
                    PERMISSION_GRANTED);
            return;
        }
        // Permission already available
        startService(new Intent(this, RecorderService.class));
        btn = (Button)findViewById(R.id.button_start);
        if (isMyServiceRunning()) {
            btn.setText(R.string.main_btn_stop);
            Toast.makeText(this, "Демон работает", Toast.LENGTH_LONG).show();
        }
        else {
            btn.setText(R.string.main_btn_start);
            Toast.makeText(this, "Демон не работает", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (RecorderService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void logoClick(View view) {
        if (isMyServiceRunning()) {
            btn.setText(R.string.main_btn_stop);
            Toast.makeText(this, "Демон работает", Toast.LENGTH_LONG).show();
        }
        else {
            btn.setText(R.string.main_btn_start);
            Toast.makeText(this, "Демон не работает", Toast.LENGTH_LONG).show();
        }
    }

    public void mainBtnClick(View view){
//        Button btn = (Button) view;
        if (isMyServiceRunning()) {
            btn.setText(R.string.main_btn_stop);
            btn.setText(R.string.main_btn_stop);
            stopService(new Intent(this, RecorderService.class));
        }
        else{
            btn.setText(R.string.main_btn_start);
            btn.setText(R.string.main_btn_start);
            startService(new Intent(this, RecorderService.class));
        }
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
            case PERMISSION_GRANTED:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    startService(new Intent(this, RecorderService.class));
                    //запустить демона
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}