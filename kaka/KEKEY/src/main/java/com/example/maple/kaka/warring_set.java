package com.example.maple.kaka;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class warring_set extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warring_set);
        ActionBarManager.getInstance().CreateSimpleActionBar(this, "준비 중");

    }
}
