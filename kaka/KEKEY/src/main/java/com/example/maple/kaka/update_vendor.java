package com.example.maple.kaka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.maple.kaka.activity.LoginActivity;
import com.example.maple.kaka.activity.RegisterActivity;
import com.example.maple.kaka.activity.memberRegister;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.app.AppController;
import com.example.maple.kaka.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class update_vendor extends AppCompatActivity {

    private static final String TAG = memberRegister.class.getSimpleName();
    private Button btnRegister;
    private TextView update_vendor_email, update_vendor_follow, update_vendor_address;
    private RadioButton up_vendor_gendor_m, up_vendor_gendor_w;
    private EditText update_vendor_phone, update_vendor_name, update_vendor_pass, update_vendor_repass, update_vendor_birth, update_vendor_address_detail;
    private TextView update_vendor_corname, update_vendor_number;
    private RadioGroup up_gendor_check;

    private ProgressDialog pDialog;

    private SQLiteHandler db_kiki;
    private String str_gendor, vendor_uid;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendor);
        ActionBarManager.getInstance().CreateSimpleActionBar(this, "사업자 정보변경");
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        HashMap<String, String> user = new HashMap<>();
        db_kiki = new SQLiteHandler(getApplicationContext());
        user = db_kiki.getVendorDetails();

        update_vendor_email = (TextView)findViewById(R.id.update_ven_email);
        update_vendor_follow = (TextView)findViewById(R.id.update_ven_follow);
        update_vendor_address = (TextView) findViewById(R.id.update_ven_address);

        update_vendor_phone = (EditText) findViewById(R.id.update_ven_phone);
        update_vendor_name = (EditText) findViewById(R.id.update_ven_name);
        update_vendor_pass = (EditText) findViewById(R.id.update_ven_pass);
        update_vendor_repass = (EditText) findViewById(R.id.update_ven_repass);
        update_vendor_birth = (EditText) findViewById(R.id.update_ven_birth);
        update_vendor_address_detail = (EditText)findViewById(R.id.update_ven_address_detail);
        update_vendor_corname = (TextView)findViewById(R.id.update_ven_corname);
        update_vendor_number = (TextView)findViewById(R.id.update_ven_number);

        up_gendor_check = (RadioGroup)findViewById(R.id.update_vendor_select);
        up_vendor_gendor_m = (RadioButton)findViewById(R.id.update_vendor_gendor_m);
        up_vendor_gendor_w = (RadioButton)findViewById(R.id.update_vendor_gendor_w);

        vendor_uid = user.get("uid");
        update_vendor_phone.setText(user.get("phone"));
        update_vendor_email.setText(user.get("email"));
        update_vendor_corname.setText(user.get("corname"));
        update_vendor_number.setText(user.get("number"));
        update_vendor_name.setText(user.get("name"));
        update_vendor_birth.setText(user.get("birth"));
        update_vendor_address.setText(user.get("address"));
        update_vendor_follow.setText(user.get("follow"));
        str_gendor = user.get("gendor");

        update_vendor_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(update_vendor.this, PopupAcitivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });

        if(str_gendor.toString().equals("남")){
            up_vendor_gendor_m.setChecked(true);
        }
        else{
            up_vendor_gendor_w.setChecked(true);
        }

        btnRegister = (Button)findViewById(R.id.venderupdate);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String _phone = update_vendor_phone.getText().toString().trim();
                String _email = update_vendor_email.getText().toString().trim();
                String _number = update_vendor_number.getText().toString().trim();
                String _corname = update_vendor_corname.getText().toString().trim();
                String _name =update_vendor_name.getText().toString().trim();
                String _password = update_vendor_pass.getText().toString().trim();
                String _repassword = update_vendor_repass.getText().toString().trim();
                String _birth = update_vendor_birth.getText().toString().trim();
                String _gendor = str_gendor.toString().trim();
                String _address = update_vendor_address.getText().toString() + update_vendor_address_detail.getText().toString();
                String _follow = update_vendor_follow.getText().toString().trim();


                if (check_register(_phone, _name, _password, _repassword, _birth, _gendor, _address))
                {
                    registerUser(_phone, _email, _number, _corname, _name, _password,  _birth, _gendor, _address, _follow);
                }
            }
        });
    }
    boolean check_register(String _phone, String _name,String _password, String _repassword, String _birth, String _gendor, String _address)
    {
        if(_phone.isEmpty()){
            Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력하세요.", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_birth.isEmpty()){
            Toast.makeText(getApplicationContext(), "생년월일을 입력하세요", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_address.isEmpty()){
            Toast.makeText(getApplicationContext(), "주소를 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(_gendor.isEmpty()){
            Toast.makeText(getApplicationContext(), "성별을 선택해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }
    private void registerUser(final String _phone, final String _email, final String _number, final String _corname, final String _name, final String _password, final String _birth,  final String _gendor, final String _address, final String _follow)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("사업자 정보변경 중입니다.");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_VENDOR_UPDATE, new Response.Listener<String>() {

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
                        String uid = vendor_uid;

                        JSONObject user = jObj.getJSONObject("user");

                        String _phone = user.getString("phone");
                        String _email = user.getString("email");
                        String _number = user.getString("number");
                        String _corname = user.getString("corname");
                        String _name = user.getString("name");
                        String _birth = user.getString("birth");
                        String _gendor = user.getString("gendor");
                        String _address = user.getString("address");
                        String _follow = user.getString("follow");


                        // Inserting row in users table
                        db_kiki.updateVendor(_phone, _email, _number, _corname, _name, _birth, _gendor, _address, _follow,  uid);

                        Toast.makeText(getApplicationContext(), "회원정보 수정 완료!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(update_vendor.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("number", _number);
                params.put("corname", _corname);
                params.put("name", _name);
                params.put("password", _password);
                params.put("birth", _birth);
                params.put("gendor", _gendor);
                params.put("address", _address);
                params.put("follow", _follow);

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
                        update_vendor_address.setText(data);
                }
                break;
        }
    }
}
