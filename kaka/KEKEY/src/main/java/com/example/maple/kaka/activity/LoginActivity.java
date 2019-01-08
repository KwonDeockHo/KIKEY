/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.example.maple.kaka.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maple.kaka.ActionBarManager;
import com.example.maple.kaka.SelectRegister;
import com.example.maple.kaka.helper.SQLiteHandler;
import com.example.maple.kaka.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.maple.kaka.R;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.app.AppController;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db_KIKI;
    private static final int MAIN_ACTIVITY = 30000;
    public static final String USER_STATE = "Custom_State";
    private  String _USER;
    SharedPreferences prefs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBarManager.getInstance().CreateEmptyActionBar(this, "Login");

        inputEmail = (EditText) findViewById(R.id.userID);
        inputPassword = (EditText) findViewById(R.id.userPass);

        btnLogin = (Button) findViewById(R.id.Login);
        btnLinkToRegister = (Button) findViewById(R.id.register);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler - 로그인 시 구분
        Log.e("LoginActivity", "CreateSQLiteHandler");
        db_KIKI = new SQLiteHandler(getApplicationContext());
        // Session manager
        Log.e("LoginActivity", "CreateSession");
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        //여기가 문제
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivityForResult(intent, MAIN_ACTIVITY);
            startActivity(intent);
        }else{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Log.e("Login","Session_else");
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요!", Toast.LENGTH_LONG).show();
                }
            }
        });
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SelectRegister.class);
                startActivity(i);
                finish();
            }
        });

    }
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    boolean state = jObj.getBoolean("state");
                    Log.e("Check_Respnse", "Try" + USER_STATE);
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        if(state) {
                            // Now store the user in SQLite
                            String uid = jObj.getString("uid");
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
                            String created_at = user.getString("created_at");
                            Log.e("Check_Respnse", "VENDOR");
                            _USER = "VENDOR";

                            // Inserting row in users table
                            db_KIKI.addVendor(_phone, _email, _number, _corname, _name, _birth, _gendor, _address, _follow, uid, created_at);
                        }
                        else{
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
                            Log.e("Check_Respnse", "MEMBER");
                            _USER = "MEMBER";

                            // Inserting row in users table
                            db_KIKI.addMember(_phone, _email, _name, _birth, _gendor, _address, _follow, uid, created_at);
                        }
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        if(state){
                            intent.putExtra(USER_STATE, "VENDOR");
                            _USER = "VENDOR";
                        }else{
                            intent.putExtra(USER_STATE, "MEMBER");
                            _USER = "MEMBER";

                        }
                        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(USER_STATE, _USER.toString());
                        editor.commit();

                        startActivityForResult(intent, MAIN_ACTIVITY);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {

                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                Log.e("checkLogin_email", email);
                Log.e("checkLogin_password", password);

                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(strReq);

        // Adding request to request queue
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
