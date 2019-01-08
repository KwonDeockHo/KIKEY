package com.example.maple.kaka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.maple.kaka.activity.LoginActivity;
import com.example.maple.kaka.helper.SQLiteHandler;
import com.example.maple.kaka.helper.SessionManager;

import java.util.HashMap;

public class member_setting extends AppCompatActivity implements View.OnClickListener{
    TextView btn_point_in, btn_point_out;
    TextView member_setting_01, member_setting_02,member_setting_03, member_setting_04;
    TextView member_setting_08, member_setting_09,member_setting_10,member_setting_11, member_setting_12, member_setting_13, member_setting_14;

    TextView tv_ven_name, tv_ven_email;
    Button bt_logout;

    SQLiteHandler db_KIKI;
    private SessionManager session;
    public static final String USER_STATE = "Custom_State";
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_setting);
        ActionBarManager.getInstance().CreateMemLogoActionBar(this, "설정");

        HashMap<String, String> user = new HashMap<>();
        user.clear();

        session = new SessionManager(getApplicationContext());
        db_KIKI = new SQLiteHandler(getApplicationContext());
        user = db_KIKI.getMemberDetails();


        member_setting_01 = (TextView)findViewById(R.id.member_setting_01);
        member_setting_02 = (TextView)findViewById(R.id.member_setting_02);
        member_setting_03 = (TextView)findViewById(R.id.member_setting_03);
        member_setting_04 = (TextView)findViewById(R.id.member_setting_04);

        member_setting_08 = (TextView)findViewById(R.id.member_setting_08);
        member_setting_09 = (TextView)findViewById(R.id.member_setting_09);
        member_setting_10 = (TextView)findViewById(R.id.member_setting_10);
        member_setting_11 = (TextView)findViewById(R.id.member_setting_11);
        member_setting_12 = (TextView)findViewById(R.id.member_setting_12);
        member_setting_13 = (TextView)findViewById(R.id.member_setting_13);


        tv_ven_name = (TextView)findViewById(R.id.tv_ven_name);
        tv_ven_email = (TextView)findViewById(R.id.tv_ven_email);
        bt_logout = (Button)findViewById(R.id.btn_logout);
        tv_ven_name.setText(user.get("name"));
        tv_ven_email.setText(user.get("email"));

        member_setting_01.setOnClickListener(this);
        member_setting_02.setOnClickListener(this);
        member_setting_03.setOnClickListener(this);
        member_setting_04.setOnClickListener(this);

        member_setting_08.setOnClickListener(this);
        member_setting_09.setOnClickListener(this);
        member_setting_10.setOnClickListener(this);
        member_setting_11.setOnClickListener(this);
        member_setting_12.setOnClickListener(this);
        member_setting_13.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.member_setting_01 :
                //위치이동
                Intent intent01 = new Intent(member_setting.this, warring_set.class);
                startActivity(intent01);
                break;
            case R.id.member_setting_02 :
                //포인트 사용내역
                Intent intent02 = new Intent(member_setting.this, Pointout.class);
                startActivity(intent02);
                break;
            case R.id.member_setting_03 :
                //포인트충전
                Intent intent03 = new Intent(member_setting.this, point_in.class);
                startActivity(intent03);
                break;
            case R.id.member_setting_04 :
                //QR코드
                Intent intent04 = new Intent(member_setting.this, point_in.class);
                startActivity(intent04);
                break;
            case R.id.member_setting_08 :
                //화면
                Intent intent08 = new Intent(member_setting.this, warring_set.class);
                startActivity(intent08);
                break;
            case R.id.member_setting_09 :
                //소리
                Intent intent09 = new Intent(member_setting.this, warring_set.class);
                startActivity(intent09);
                break;
            case R.id.member_setting_10 :
                //공지사항
                Intent intent10 = new Intent(member_setting.this, warring_set.class);
                startActivity(intent10);
                break;
            case R.id.member_setting_11 :
                //개인정보변경
                Intent intent11 = new Intent(member_setting.this, update_member_register.class);
                startActivity(intent11);
                break;
            case R.id.member_setting_12 :
                //버전정보
                Intent intent12 = new Intent(member_setting.this, warring_set.class);
                startActivity(intent12);
                break;
            case R.id.member_setting_13 :
                //고객센터
                Intent intent13 = new Intent(member_setting.this, warring_set.class);
                startActivity(intent13);
                break;
            case R.id.btn_logout :
                //로그아웃
                session.setLogin(false);

                db_KIKI.deleteUsers();
                // Launching the login activity
                Intent intent = new Intent(member_setting.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
