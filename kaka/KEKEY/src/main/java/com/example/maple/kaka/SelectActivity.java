package com.example.maple.kaka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectActivity extends AppCompatActivity {
    TextView Select_ven_index, event_state, event_value;

    ImageView shop_img[] = new ImageView[6];
    ImageView call_shop;

    String product_data1, product_data2, product_data3, product_data4, product_data5, product_data6, ven_index;
    String vendor_phone, vendor_address, text_event_value, text_event_state, vendor_ID;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ActionBarManager.getInstance().CreateLogoActionBar(this, "상세페이지");

        Select_ven_index = (TextView)findViewById(R.id.select_ven_index);

        shop_img[0] = (ImageView)findViewById(R.id.shop_img01);
        shop_img[1] = (ImageView)findViewById(R.id.shop_img02);
        shop_img[2] = (ImageView)findViewById(R.id.shop_img03);
        shop_img[3] = (ImageView)findViewById(R.id.shop_img04);
        shop_img[4] = (ImageView)findViewById(R.id.shop_img05);
        shop_img[5] = (ImageView)findViewById(R.id.shop_img06);

        call_shop = (ImageView)findViewById(R.id.call_by_shop);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pDialog.setMessage("이미지 로딩 중입니다.");
        showDialog();
        new SearchShop().execute();

        event_state= (TextView)findViewById(R.id.event_state);
        event_value = (TextView)findViewById(R.id.event_value);

        shop_img[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
        shop_img[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
        shop_img[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
        shop_img[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
        shop_img[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
        shop_img[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, ImageActivity.class);
                startActivity(intent);
            }
        });
        call_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전화걸기기
                startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:"+vendor_phone)));
            }
        });


    }

    private class SearchShop extends AsyncTask<Void, Void, String>{

        Intent click_intent = getIntent();
        String clickShopID = click_intent.getStringExtra("clickList").toString();
        String targetURL;

        @Override
        protected void onPreExecute() {
            targetURL = AppConfig.URL_BASIC+"kiki_/kiSelect_info.php?_corname="+clickShopID;
            Log.e("ClickShopID", clickShopID);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(targetURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return stringBuilder.toString().trim();

            }catch (Exception e){
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            findVendorInfo(clickShopID.toString());

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    product_data1 = object.getString("product_data1");
                    product_data2 = object.getString("product_data2");
                    product_data3 = object.getString("product_data3");
                    product_data4 = object.getString("product_data4");
                    product_data5 = object.getString("product_data5");
                    product_data6 = object.getString("product_data6");
                    ven_index = object.getString("_index");


                    String ven_product_info[] = new String[7];

                    ven_product_info[0] = product_data1;
                    ven_product_info[1] = product_data2;
                    ven_product_info[2] = product_data3;
                    ven_product_info[3] = product_data4;
                    ven_product_info[4] = product_data5;
                    ven_product_info[5] = product_data6;
                    ven_product_info[6] = ven_index;


                    for(int glide = 0; glide < 6; glide++)
                    { Glide.with(getApplicationContext()).load(AppConfig.URL_AD+ven_product_info[glide]+".PNG").override(80,60).into(shop_img[glide]); }

                    Select_ven_index.setText(ven_product_info[6]);

                    hideDialog();

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void findVendorInfo(final String _corname)
    {
        // Tag used to cancel the request
        String tag_string_req = "SelectActivity";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SELECT_INFO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONObject user = jObj.getJSONObject("select");

                        text_event_state = user.getString("event_state");
                        text_event_value = user.getString("event_value");
                        vendor_address  = user.getString("address");
                        vendor_phone = user.getString("phone");



                        if(text_event_state.toString().equals("TRUE")){
                            event_state.setText("EVENT");
                            event_value.setText(text_event_value);
                        }
                        else{
                            event_state.setText("");
                            event_value.setText("");
                        }
                        Select_ven_index.setText(vendor_address);


                        Log.e("Reponse", text_event_state);
                        Log.e("Reponse", text_event_value);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("corname", _corname);
                return params;
            }
        };
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