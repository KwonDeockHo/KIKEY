package com.example.maple.kaka;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.example.maple.kaka.activity.*;

public class Intro extends AppCompatActivity {
    Handler h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        ActionBarManager.getInstance().CreateEmptyActionBar(this, "intro");

        h= new Handler();
        h.postDelayed(mrun,2000);
    }
    Runnable mrun = new Runnable(){
        @Override
        public void run(){
            Intent i = new Intent(Intro.this, LoginActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(i);
            Log.e("intor", "Create intro");
            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        }
    };
    //인트로 중에 뒤로가기를 누를 경우 핸들러를 끊어버려 아무일 없게 만드는 부분
    //미 설정시 인트로 중 뒤로가기를 누르면 인트로 후에 홈화면이 나옴.
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }
}
