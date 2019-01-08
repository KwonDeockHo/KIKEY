package com.example.maple.kaka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Pointout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointout);

        ActionBarManager.getInstance().CreateSimpleActionBar(this, "포인트사용내역");

    }
}
