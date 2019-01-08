package com.example.maple.kaka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.maple.kaka.activity.MainActivity;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ActionBarManager.getInstance().CreateLogoActionBar(this, "상세페이지");

    }
}
