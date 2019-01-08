package com.example.maple.kaka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.maple.kaka.activity.LoginActivity;
import com.example.maple.kaka.activity.MainActivity;
import com.example.maple.kaka.activity.RegisterActivity;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.app.AppController;
import com.example.maple.kaka.helper.SQLiteHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EventText extends AppCompatActivity{
    private static final String TAG = EventText.class.getSimpleName();

    Switch Text_Event_Switch;
    EditText text_event_edt;
    Button text_event_ok;
    String vendor_number, text_event_on, text_event_value;
    int text_event_count = 0;
    SharedPreferences prefs;
    public static final String TEXT_EVENT = "TEXT_EVENT_STATE";

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_text);
        SQLiteHandler db_KIKI;
        db_KIKI = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = new HashMap<>();
        user = db_KIKI.getVendorDetails();
        vendor_number = user.get("number");
        text_event_on = "FALSE";
        ActionBarManager.getInstance().CreateLogoActionBar(this, "EVENT 광고");

        Text_Event_Switch = (Switch)findViewById(R.id.Text_Event_Switch);
        text_event_edt = (EditText)findViewById(R.id.text_event_edt);
        text_event_ok = (Button)findViewById(R.id.text_event_ok);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        text_event_count = prefs.getInt(TEXT_EVENT, 0);

        text_event_value = text_event_edt.getText().toString();
        Text_Event_Switch.setChecked(false);
        Text_Event_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean onCheck) {
                if(onCheck == true){
                    text_event_on = "TRUE";
                }
                else
                    text_event_on = "FALSE";
            }
        });
        text_event_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendor_number = vendor_number.toString();
                text_event_on = text_event_on.toString();
                text_event_value = text_event_edt.getText().toString();


                if (check_value(text_event_value))
                {
                    text_event(vendor_number, text_event_on, text_event_value);
                }


                prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(TEXT_EVENT, text_event_count++);
                editor.commit();
            }
        });
    }
    boolean check_value(String value){

        if(value.isEmpty()){
            Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
    private void text_event(final String _number, final String _state, final String _value)
    {
        // Tag used to cancel the request
        String tag_string_req = "TEXT_EVENT";

        pDialog.setMessage("이벤트 광고 등록 중입니다.");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_TEXT_EVENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String errorMsg = jObj.getString("error_msg");
                    Log.e("이벤트 등록 error",error+"");
                    Log.e("이벤트 등록 errorMsg",errorMsg);

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        Log.e("이벤트 등록","완료되었습니다.");
                        Toast.makeText(getApplicationContext(), "이벤트 등록 완료되었습니다!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(EventText.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        Log.e("이벤트 등록","실패했습니다..");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("이벤트 등록","완료되었습니다.");
                Toast.makeText(getApplicationContext(), "이벤트 등록 완료되었습니다!", Toast.LENGTH_LONG).show();
                // Launch login activity
                Intent intent = new Intent(EventText.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("number", _number);
                params.put("state", _state);
                params.put("value", _value);

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
}
