package com.alphamap.materialdesigndemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author fyg
 */
public class MainActivity extends AppCompatActivity {
    public static final String FRUIT_NAME= "fruit_name";
    public static final String FRUIT_IMAGE_ID= "fruit_image_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
