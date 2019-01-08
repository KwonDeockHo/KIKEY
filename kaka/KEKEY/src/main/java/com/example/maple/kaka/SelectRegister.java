package com.example.maple.kaka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.maple.kaka.activity.RegisterActivity;
import com.example.maple.kaka.activity.memberRegister;

public class SelectRegister extends AppCompatActivity {
    Button register_member, register_vendor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_register);
        ActionBarManager.getInstance().CreateSimpleActionBar(this, "회원가입");

        register_member = (Button)findViewById(R.id.register_member);
        register_vendor = (Button)findViewById(R.id.register_vendor);

        register_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent member = new Intent(SelectRegister.this, memberRegister .class);
                SelectRegister.this.startActivity(member);
            }
        });
        register_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vendor = new Intent(SelectRegister.this, RegisterActivity.class);
                SelectRegister.this.startActivity(vendor);
            }
        });
    }
}
