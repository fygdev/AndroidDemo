package com.alpha.fyg.androiddemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.util.List;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity {
    public static int REQUESTCODE_FROM_ACTIVITY = 1000;
    public static int REQUESTCODE_FROM_FRAGMENT = 1001;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void btnSelectFile(View view) {
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withTitleStyle(R.style.LFileToolbarTextStyle)
                .withTheme(R.style.LFileTheme)
                .withTitle("文件选择")
                .withIconStyle(Constant.ICON_STYLE_YELLOW)
                .withBackgroundColor("#2A68DE")
//                .withBackIcon(mBackArrawType)
                .withMutilyMode(false)
                .withMaxNum(2)
                .withStartPath("/storage/emulated/0")//指定初始显示路径  /Download
                .withNotFoundBooks("至少选择一个文件")
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withFileSize(500 * 1024)//指定文件大小为500K
                .withChooseMode(false)//文件夹选择模式
                //.withFileFilter(new String[]{"txt", "png", "docx"})
                .start();
        //        .withRequestCode(Consant.REQUESTCODE_FROM_FRAGMENT)
//                .withTitleStyle(R.style.TextStyle)
//                .withTheme(R.style.AppTheme)
//                .withTitle("Open From Fragment")
//                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                //for (String s : list) {
                //    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //}
//                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
                String path = data.getStringExtra("path");
                Toast.makeText(getApplicationContext(), "选中的路径为" + path, Toast.LENGTH_SHORT).show();
                Log.i("LeonFilePicker", path);
            }
        }
    }

}
