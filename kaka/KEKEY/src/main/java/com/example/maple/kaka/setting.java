package com.example.maple.kaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.maple.kaka.activity.LoginActivity;
import com.example.maple.kaka.activity.MainActivity;
import com.example.maple.kaka.helper.SQLiteHandler;
import com.example.maple.kaka.helper.SessionManager;

import org.w3c.dom.Text;

import java.util.HashMap;

public class setting extends AppCompatActivity implements View.OnClickListener{
    TextView btn_point_in, btn_point_out;
    TextView btn_setting_01, btn_setting_02,btn_setting_03,btn_setting_04,btn_setting_05,btn_setting_06;
    TextView btn_setting_07,btn_setting_08,btn_setting_09,btn_setting_10,btn_setting_11, btn_setting_12, btn_setting_13, btn_setting_14;

    TextView tv_ven_name, tv_ven_email;
    Button bt_logout;


    SQLiteHandler db_KIKI;
    private SessionManager session;
    public static final String USER_STATE = "Custom_State";
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBarManager.getInstance().CreateLogoActionBar(this, "사업자 설정");

        HashMap<String, String> user = new HashMap<>();
        user.clear();

        session = new SessionManager(getApplicationContext());
        db_KIKI = new SQLiteHandler(getApplicationContext());
        user = db_KIKI.getVendorDetails();
        btn_setting_01 = (TextView)findViewById(R.id.btn_setting_01);
        btn_setting_02 = (TextView)findViewById(R.id.btn_setting_02);
        btn_setting_03 = (TextView)findViewById(R.id.btn_setting_03);
        btn_setting_04 = (TextView)findViewById(R.id.btn_setting_04);
        btn_setting_05 = (TextView)findViewById(R.id.btn_setting_05);
        btn_setting_06 = (TextView)findViewById(R.id.btn_setting_06);
        btn_setting_07 = (TextView)findViewById(R.id.btn_setting_07);
        btn_setting_08 = (TextView)findViewById(R.id.btn_setting_08);
        btn_setting_09 = (TextView)findViewById(R.id.btn_setting_09);
        btn_setting_10 = (TextView)findViewById(R.id.btn_setting_10);
        btn_setting_11 = (TextView)findViewById(R.id.btn_setting_11);
        btn_setting_12 = (TextView)findViewById(R.id.btn_setting_12);
        btn_setting_13 = (TextView)findViewById(R.id.btn_setting_13);

        tv_ven_name = (TextView)findViewById(R.id.tv_ven_name);
        tv_ven_email = (TextView)findViewById(R.id.tv_ven_email);
        bt_logout = (Button)findViewById(R.id.btn_logout);
        tv_ven_name.setText(user.get("corname"));
        tv_ven_email.setText(user.get("email"));


        btn_setting_01.setOnClickListener(this);
        btn_setting_02.setOnClickListener(this);
        btn_setting_03.setOnClickListener(this);
        btn_setting_04.setOnClickListener(this);
        btn_setting_05.setOnClickListener(this);
        btn_setting_06.setOnClickListener(this);
        btn_setting_07.setOnClickListener(this);
        btn_setting_08.setOnClickListener(this);
        btn_setting_09.setOnClickListener(this);
        btn_setting_10.setOnClickListener(this);
        btn_setting_11.setOnClickListener(this);
        btn_setting_12.setOnClickListener(this);
        btn_setting_13.setOnClickListener(this);
        bt_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_setting_01 :
                //위치이동
                Intent intent01 = new Intent(setting.this, warring_set.class);
                startActivity(intent01);
                break;
            case R.id.btn_setting_02 :
                //포인트 사용내역
                Intent intent02 = new Intent(setting.this, Pointout.class);
                startActivity(intent02);
                break;
            case R.id.btn_setting_03 :
                //포인트충전
                Intent intent03 = new Intent(setting.this, point_in.class);
                startActivity(intent03);
                break;
            case R.id.btn_setting_04 :
                //QR코드
                Intent intent04 = new Intent(setting.this, warring_set.class);
                startActivity(intent04);
                break;
            case R.id.btn_setting_05 :
                //검색광고 등록/해제
                Intent intent05 = new Intent(setting.this, insert.class);
                startActivity(intent05);
                break;
            case R.id.btn_setting_06 :
                //페이지 광고 등록/해제
                Intent intent06 = new Intent(setting.this, Advertisement.class);
                startActivity(intent06);
                break;
            case R.id.btn_setting_07 :
                //Event 광고 등록/해제
                Intent intent07 = new Intent(setting.this, EventText.class);
                startActivity(intent07);
                break;
            case R.id.btn_setting_08 :
                //화면
                Intent intent08 = new Intent(setting.this, warring_set.class);
                startActivity(intent08);
                break;
            case R.id.btn_setting_09 :
                //소리
                Intent intent09 = new Intent(setting.this, warring_set.class);
                startActivity(intent09);
                break;
            case R.id.btn_setting_10 :
                //공지사항
                Intent intent10 = new Intent(setting.this, warring_set.class);
                startActivity(intent10);
                break;
            case R.id.btn_setting_11 :
                //개인정보변경
                Intent intent11 = new Intent(setting.this, update_vendor.class);
                startActivity(intent11);
                break;
            case R.id.btn_setting_12 :
                //버전정보
                Intent intent12 = new Intent(setting.this, warring_set.class);
                startActivity(intent12);
                break;
            case R.id.btn_setting_13 :
                //고객센터
                Intent intent13 = new Intent(setting.this, warring_set.class);
                startActivity(intent13);
                break;
            case R.id.btn_logout :
                //로그아웃
                session.setLogin(false);
                 db_KIKI.deleteUsers();
                // Launching the login activity
                Intent intent = new Intent(setting.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
