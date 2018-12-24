package example.a123.test42;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;

public class ShareActivity extends Activity {

    private static final int PICK_FILE_RESULT_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType( "*/*");
//        startActivityForResult(intent, PICK_FILE_RESULT_CODE);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File file = new File(RecordedCallService.filepath);
        Intent data = new Intent();
        data.setData(Uri.fromFile(file));
        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        System.out.println(intentShareFile.getType());
        intentShareFile.putExtra(Intent.EXTRA_STREAM, data.getData());

        startActivity(Intent.createChooser(intentShareFile, "Share File"));

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_FILE_RESULT_CODE && data != null) {
//            String filePath = data.getData().getPath();
//            System.out.println(filePath);
//            File file = new File(filePath);
//            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//            intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
//            System.out.println(intentShareFile.getType());
//            intentShareFile.putExtra(Intent.EXTRA_STREAM, data.getData());
//
//            startActivity(Intent.createChooser(intentShareFile, "Share File"));
//        }
//        //stopService(new Intent(ShareActivity.this, RecordedCallService.class));
//        super.finish();
//
//    }
}

