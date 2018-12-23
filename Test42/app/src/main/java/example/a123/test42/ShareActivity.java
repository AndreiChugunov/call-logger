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
//        Samsung file explorer needs not only custom action (com.sec.android.app.myfiles.PICK_DATA),
//        but also category part (Intent.CATEGORY_DEFAULT) and mime-type should be passed as extra.

//        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
//        intent.putExtra("CONTENT_TYPE", "*/*");
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        startActivityForResult(intent, 7);
//
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType( "*/*");
        startActivityForResult(intent, PICK_FILE_RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_RESULT_CODE && data != null) {
            String filePath = data.getData().getPath();
            System.out.println(filePath);
            File file = new File(filePath);
            Intent intentShareFile = new Intent("android.intent.action.SEND");
            intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
            System.out.println(intentShareFile.getType());
            intentShareFile.putExtra(Intent.EXTRA_STREAM, data.getData());

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
        //stopService(new Intent(ShareActivity.this, RecordedCallService.class));
        super.finish();

    }
}

