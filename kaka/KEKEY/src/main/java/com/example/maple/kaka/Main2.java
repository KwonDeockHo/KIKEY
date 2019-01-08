package com.example.maple.kaka;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.maple.kaka.activity.MainActivity;
import com.example.maple.kaka.app.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class Main2 extends AppCompatActivity  {

    ViewFlipper view;
    //Switch bt_switch;

    ImageView bitmap_adver[] = new ImageView[6];

    private Handler mHandler;
    private  Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActionBarManager.getInstance().CreateAdverActionBar(this, true);

        Intent main_activity_intent = getIntent();
        String ad_db_number = main_activity_intent.getStringExtra("AD_arr");

        //AD_arr

        bitmap_adver[0] = (ImageView)findViewById(R.id.adver_image01);
        bitmap_adver[1] = (ImageView)findViewById(R.id.adver_image02);
        bitmap_adver[2] = (ImageView)findViewById(R.id.adver_image03);
        bitmap_adver[3] = (ImageView)findViewById(R.id.adver_image04);
        bitmap_adver[4] = (ImageView)findViewById(R.id.adver_image05);
        bitmap_adver[5] = (ImageView)findViewById(R.id.adver_image06);


        Glide.with(this).load(AppConfig.URL_AD+ad_db_number+"AD_image1.PNG").override(500,700).into(bitmap_adver[0]);
        Glide.with(this).load(AppConfig.URL_AD+ad_db_number+"AD_image2.PNG").override(500,700).into(bitmap_adver[1]);
        Glide.with(this).load(AppConfig.URL_AD+ad_db_number+"AD_image3.PNG").override(500,700).into(bitmap_adver[2]);
        Glide.with(this).load(AppConfig.URL_AD+ad_db_number+"AD_image4.PNG").override(500,700).into(bitmap_adver[3]);
        Glide.with(this).load(AppConfig.URL_AD+ad_db_number+"AD_image5.PNG").override(500,700).into(bitmap_adver[4]);
        Glide.with(this).load(AppConfig.URL_AD+ad_db_number+"AD_image6.PNG").override(500,700).into(bitmap_adver[5]);


        view = (ViewFlipper) findViewById(R.id.viewFlipper1);


        // 페이지 넘기는 구간시간 설정 1000 = 1초
        view.setFlipInterval(2000);
        view.setInAnimation(this, R.anim.push_left_in);
        view.setOutAnimation(this, R.anim.push_left_out);

        view.startFlipping();


        // 특정 먼저 실행할 부분 설정 플리퍼에 설정한페이지는 처음부터 0으로 시작
        // view.setDisplayedChild(2);


        mRunnable = new Runnable() {
            @Override
            public void run() {

                Check();
            }
        };

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 14000);

    }


    void Check(){
        view.stopFlipping();


        new AlertDialog.Builder(this)
                .setTitle("포인트 획득!")
                .setMessage("포인트를 획득하셨습니다.\n\n 광고 스위치를 종료해주세요")
                .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                        Intent intent = new Intent(Main2.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                }).show(); // 팝업창 보여줌
    }//end Check()

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
