package org.ewha5.clorapp;

import static org.ewha5.clorapp.AppConstants.REQUEST_CODE_BUTTON1;
import static org.ewha5.clorapp.AppConstants.REQUEST_CODE_BUTTON2;
import static org.ewha5.clorapp.AppConstants.REQUEST_CODE_BUTTON3;

import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    /**
     * 데이터베이스 인스턴스
     */
    public static ClorDatabase mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button menu01Button = (Button) findViewById(R.id.button1);
        menu01Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Fragment1.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_BUTTON1);
            }
        });

        Button menu02Button = findViewById(R.id.button2);
        menu02Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Fragment2.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_BUTTON2);
            }
        });

        /*
        Button menu03Button = findViewById(R.id.button3);
        menu03Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDB.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_BUTTON3);
            }
        });
                 */


        setPicturePath();

        AndPermission.with(this)
                .runtime()
                .permission(
                        Permission.CAMERA,
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                .start();

        //데이터베이스 열기
        openDatabase();

    }

    //데이터베이스 method
    protected void onDestroy() {
        super.onDestroy();

        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }
    /**
     * 데이터베이스 열기 (데이터베이스가 없을 때는 만들기)
     */
    public void openDatabase() {
        // open database
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = ClorDatabase.getInstance(this);
        boolean isOpen = mDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }

    }

    public void setPicturePath() {
            String folderPath = getFilesDir().getAbsolutePath();
            AppConstants.FOLDER_PHOTO = folderPath + File.separator + "photo";

            File photoFolder = new File(AppConstants.FOLDER_PHOTO);
            if (!photoFolder.exists()) {
                photoFolder.mkdirs();
            }
        }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void println(String data) {
        Log.d(TAG, data);
    }


}