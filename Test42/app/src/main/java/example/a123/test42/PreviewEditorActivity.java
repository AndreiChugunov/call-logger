package example.a123.test42;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLConnection;


public class PreviewEditorActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static final int READ_REQUEST_CODE = 42;
    private EditText mEditText;
    private File currentFile;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_editor);

        mEditText = findViewById(R.id.editText);
        mEditText.setText(readInit(RecordedCallService.filepath));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }
/*
        ActionMode.Callback actionModeCallBack = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.my_menu_two, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_bold:
                        int startSelection = mEditText.getSelectionStart();
                        int endSelection = mEditText.getSelectionEnd();

                        CharSequence selectedText = mEditText.getText().subSequence(startSelection, endSelection);
                        SpannableString string = new SpannableString(selectedText);
                        string.setSpan(new StyleSpan(Typeface.BOLD), 0, selectedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mEditText.setText(mEditText.getText().replace(startSelection, endSelection, string, 0, string.length()));
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // actionMode = null;
                // toggleButton.setChecked(false);
                return;
            }
        };
*/
        mEditText.setTextIsSelectable(true);
      //  mEditText.setCustomSelectionActionModeCallback(actionModeCallBack);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.isEnabled() && mEditText.isFocusable()) {
                    mEditText.post(new Runnable() {
                        @Override
                        public void run() {
                            Context context = getApplicationContext();
                            final InputMethodManager imm =
                                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load:
                openFile();
                return true;
            case R.id.action_share:
                shareFile();
                return true;
            case R.id.action_save:
                write();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareFile() {
        Intent intentShareFile = new Intent("android.intent.action.SEND");
        intentShareFile.setType(URLConnection.guessContentTypeFromName(currentFile.getName()));
        Intent data = new Intent();
        data.setData(Uri.fromFile(currentFile));
        intentShareFile.putExtra(Intent.EXTRA_STREAM, data.getData());
        startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }

    private void write() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                m_Text += ".rtf";

                try {
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/records");
                    dir.mkdirs();
                    if (new File(dir, m_Text).exists()) {
                        Toast.makeText(getApplicationContext(), "File with a name " + m_Text + " already exists.", Toast.LENGTH_LONG).show();
                        write();
                    } else {
                        File file = new File(dir, m_Text);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(mEditText.getEditableText().toString().getBytes());
                        Field field = FileOutputStream.class.getDeclaredField("path");
                        field.setAccessible(true);
                        String path = (String) field.get(fileOutputStream);
                        System.out.println(path);
                        currentFile = new File(path);
                        fileOutputStream.close();
                        Toast.makeText(getApplicationContext(), m_Text + " saved successfully", Toast.LENGTH_LONG).show();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private String readInit(String path) {
        File file = new File(path);
        currentFile = file;
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    private String read(String input) {
        File file = new File(Environment.getExternalStorageDirectory(), input);
        currentFile = file;
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    private void openFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();

                System.out.println(path);
                path = path.substring(path.indexOf(":") + 1);
               //Toast.makeText(this, "" + path, Toast.LENGTH_SHORT).show();
                mEditText.setText(read(path));
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
