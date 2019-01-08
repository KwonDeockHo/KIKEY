package com.example.maple.kaka.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.maple.kaka.ActionBarManager;
import com.example.maple.kaka.EmailCheck;
import com.example.maple.kaka.PopupAcitivity;
import com.example.maple.kaka.R;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.app.AppController;
import com.example.maple.kaka.helper.SQLiteHandler;
import com.example.maple.kaka.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class memberRegister extends AppCompatActivity {
    private static final String TAG = memberRegister.class.getSimpleName();
    private Button btnRegister, email_check;
    private EditText member_email, member_name, member_pass, member_repass, member_birth;
    private EditText member_phone, member_address_detail, member_follow;
    private CheckBox termsCheckBox, collectCheckBox;
    private TextView member_address;
    private RadioGroup gendor_check;
    boolean email_check_state;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String str_gendor, terms, collect;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int EMAIL_CHECK_ACTIVITY = 20000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);
        ActionBarManager.getInstance().CreateSimpleActionBar(this, "사용자 회원가입");

        str_gendor = terms = collect = "";
        member_phone = (EditText) findViewById(R.id.member_phone);
        member_email = (EditText) findViewById(R.id.member_email);
        member_name = (EditText) findViewById(R.id.member_name);
        member_pass = (EditText) findViewById(R.id.member_pass);
        member_repass = (EditText) findViewById(R.id.member_repass);
        member_birth = (EditText) findViewById(R.id.member_birth);
        member_address = (TextView) findViewById(R.id.member_address);
        member_address_detail = (EditText) findViewById(R.id.member_address_detail);
        member_follow = (EditText) findViewById(R.id.member_follow);
        gendor_check = (RadioGroup)findViewById(R.id.gendor_select);
        termsCheckBox = (CheckBox)findViewById(R.id.mem_termsCheckBox);
        collectCheckBox = (CheckBox)findViewById(R.id.mem_collectCheckBox);
        email_check = (Button)findViewById(R.id.email_check);
        email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                String email_check = member_email.getText().toString();
                email_check_state = false;
                String User_State = "MEMBER";
                if(!email_check.isEmpty())
                {
                    Intent Email = new Intent(memberRegister.this, EmailCheck.class);
                    Email.putExtra("Email_Address", email_check);
                    Email.putExtra("User_State", User_State);
                    startActivityForResult(Email, EMAIL_CHECK_ACTIVITY);
                }
                else{
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_LONG).show();
                }


            }
        });
        member_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(memberRegister.this, PopupAcitivity.class);
                i.putExtra("data", "Test Popup");
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });
        collectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.getId() == R.id.mem_collectCheckBox){
                    if(b){
                        collect = "true";
                    }
                    else
                        collect = "false";
                }
            }
        });
        termsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.getId() == R.id.mem_termsCheckBox){
                    if(b){
                        terms = "true";
                    }
                    else
                        terms = "false";
                }
            }
        });

        btnRegister = (Button) findViewById(R.id.venderupdate);
        //btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(memberRegister.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        gendor_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int isCheck) {
                if(isCheck == R.id.member_gendor_m){
                    str_gendor = "남";
                }else if(isCheck==R.id.member_gendor_w) {
                    str_gendor = "여";
                }
            }
        });
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String _phone = member_phone.getText().toString().trim();
                String _email = member_email.getText().toString().trim();
                String _name =member_name.getText().toString().trim();
                String _password = member_pass.getText().toString().trim();
                String _repassword = member_repass.getText().toString().trim();
                String _birth = member_birth.getText().toString().trim();
                String _gendor = str_gendor.toString().trim();
                String _address = member_address.getText().toString() + member_address_detail.getText().toString();
                String _follow = member_follow.getText().toString().trim();
                String _terms = terms.toString().trim();
                String _collect = collect.toString().trim();


                if (check_register(_phone, _email, _name, _password, _repassword, _birth, _gendor, _address, _follow, _terms, _collect))
                {
                    registerUser(_phone, _email, _name, _password, _birth,  _gendor, _address, _follow, _terms, _collect);
                }
            }
        });
    }
    boolean check_register(String _phone,String _email, String _name, String _password, String _repassword, String _birth, String _gendor, String _address, String _follow,String _terms, String _collect)
    {
        if(_phone.isEmpty()){
            Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_email.isEmpty()){
            Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_name.isEmpty()){
            Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_password.isEmpty()){
            Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!_password.toString().equals(_repassword.toString())){
            Toast.makeText(getApplicationContext(), _password.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), _repassword.toString(), Toast.LENGTH_LONG).show();

            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_birth.isEmpty()){
            Toast.makeText(getApplicationContext(), "생년월일을 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_address.isEmpty()){
            Toast.makeText(getApplicationContext(), "주소를 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_follow.isEmpty()){
            Toast.makeText(getApplicationContext(), "추천인을 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_gendor.isEmpty()){
            Toast.makeText(getApplicationContext(), "성별을 선택해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_terms.isEmpty()){
            Toast.makeText(getApplicationContext(), "약관동의에 체크해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_collect.isEmpty()){
            Toast.makeText(getApplicationContext(), "정보수집동의에 체크해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!email_check_state){
            Toast.makeText(getApplicationContext(), "Email 중복체크를 해주세요!", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String _phone, final String _email, final String _name, final String _password, final String _birth, final String _gendor, final String _address, final String _follow,final String _terms, final String _collect)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_member_register";

        pDialog.setMessage("회원가입중입니다.");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_MEMBOR_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");

                        String _phone = user.getString("phone");
                        String _email = user.getString("email");
                        String _name = user.getString("name");
                        String _birth = user.getString("birth");
                        String _gendor = user.getString("gendor");
                        String _address = user.getString("address");
                        String _follow = user.getString("follow");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        db.addMember(_phone, _email, _name, _birth, _gendor, _address, _follow, uid, created_at);

                        Toast.makeText(getApplicationContext(), "회원가입 완료!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(memberRegister.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", _phone);
                params.put("email", _email);
                params.put("name", _name);
                params.put("password", _password);
                params.put("birth", _birth);
                params.put("gendor", _gendor);
                params.put("address", _address);
                params.put("follow", _follow);
                params.put("terms", _terms);
                params.put("collect", _collect);

                return params;
            }
        };

        // Adding request to request queue
        Log.e("Adding_Request_strReq", strReq.toString());
        Log.e("tag_string_req", tag_string_req);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode){
            case SEARCH_ADDRESS_ACTIVITY:
                if(resultCode == RESULT_OK){
                    String data = intent.getExtras().getString("data");
                    if (data != null)
                        member_address.setText(data);
                }
                break;
            case EMAIL_CHECK_ACTIVITY:
                if(resultCode == RESULT_OK){
                    String email_result = intent.getExtras().getString("Email");
                    email_check_state = intent.getExtras().getBoolean("Email_result");
                    if(email_result !=null)
                        member_email.setText(email_result);
                }
                break;
        }
    }
}
