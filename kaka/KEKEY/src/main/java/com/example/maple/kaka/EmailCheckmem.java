package com.example.maple.kaka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmailCheckmem extends Activity implements View.OnClickListener{
    TextView email_check_txt;
    Button btn_email_ok, btn_email_cencel;
    private ProgressDialog pDialog;
    String _email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_email_check);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //Data 가져오기
        Intent intent = getIntent();
        _email = intent.getStringExtra("Email_Address");

        email_check_txt = (TextView)findViewById(R.id.email_check_txt);
        btn_email_ok = (Button)findViewById(R.id.btn_email_ok);
        btn_email_cencel = (Button)findViewById(R.id.btn_email_cancel);

        Email_Check(_email.toString());
        Log.e("CREATE", _email);
        btn_email_ok.setOnClickListener(this);
        btn_email_cencel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_email_ok){
            Intent intent = new Intent();
            intent.putExtra("Email_result", _email);
            setResult(RESULT_OK, intent);
            //액티비티(팝업) 닫기
            finish();
        }
        else if(view.getId() == R.id.btn_email_cancel){
            Intent intent = new Intent();
            intent.putExtra("Email_result", _email);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    private void Email_Check(final String _email)
    {
        // Tag used to cancel the request
        String tag_string_req = "email_check";
        pDialog.setMessage("중복확인 중입니다 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_MEMBER_EMAIL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    Log.e("onResponse_try", response);
                    JSONObject Email_jObj = new JSONObject(response);
                    boolean error = Email_jObj.getBoolean("error");
                    Log.e("error", error+"");
                    if (!error) {
                        JSONObject user = Email_jObj.getJSONObject("user");
                        String _email = user.getString("email");
                        boolean errorMsg = Email_jObj.getBoolean("error_msg");
                        int errorPosition = Email_jObj.getInt("error_position");
                    }else {
                        boolean errorMsg = Email_jObj.getBoolean("error_msg");
                        int errorPosition = Email_jObj.getInt("error_position");
                        if (!errorMsg) {
                            email_check_txt.setText("사용가능한 아이디 입니다.");
                        }else{
                            email_check_txt.setText("이미 존재하는 아이디 입니다.");
                        }
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_check", _email);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
