package com.alpha.fyg.androiddemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    private EditText etTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String cacherDir = FileUtils.getCacheDir(this);
        Log.e("MainActivity", cacherDir);
        String externalCacherDir = FileUtils.getExternalCacheDir(this);
        Log.e("MainActivity", externalCacherDir);
        String externalFileDir = FileUtils.getExternalFileDir(this);
        String externalStorageDir = FileUtils.getExternalStorageDirectory();
        String fileDir = FileUtils.getFileDir(this);
        Log.e("MainActivity", externalFileDir);
        Log.e("MainActivity", externalStorageDir);
        Log.e("MainActivity", fileDir);


        File encFile = new File(FileUtils.getExternalStorageDirectory(), "2012年至2016年广州市高标范围20180320.enc");
        if (encFile.exists()) {
            encFile.delete();
            Log.e("MainActivity", "enfFileDelete");
//            FileUtils.fileChannelCopy(encFile, FileUtils.getFileDir(this));


        }
        File targetFile = new File(FileUtils.getFileDir(this), "2012年至2016年广州市高标范围20180320.enc");
        if (targetFile.exists()) {
            targetFile.delete();
            Log.e("MainActivity", "targetFileDelete");
        }

        etTest = findViewById(R.id.et_test);
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
                .withRootPath("/storage/emulated/0")
                .withNotFoundBooks("至少选择一个文件")
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withFileSize(500 * 1024)//指定文件大小为500K
                .withChooseMode(true)//文件夹选择模式
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
                Toast.makeText(getApplicationContext(), "选中的路径为:" + path, Toast.LENGTH_SHORT).show();
                Log.i("LeonFilePicker", path);
            }
        }
    }

    public void btnTest(View view) {
        File file = new File("/storage/usb0/test.txt");
        Log.d("MainActivity", "file.exists():" + file.exists());
        if (file.exists()) {

//            userinfo = new ArrayList<>();
            // 建立一个输入流对象reader
            InputStreamReader reader = null;
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            BufferedReader br = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file), "GBK");
                br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
//                    userinfo.add(line);   // 一次读入一行数据
                    Log.d("MainActivity", line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//        String info = etTest.getText().toString().trim();
//        List<String> infoList = new ArrayList<>();
//        infoList.add(info);
//        Log.e("MainActivity", "infoList.size():" + infoList.size());
//        Log.e("MainActivity", "info:" + info + "====hhhh");
//        Toast.makeText(this, infoList.get(0), Toast.LENGTH_SHORT).show();
//        etTest.setText(infoList.get(0));
    }
}
