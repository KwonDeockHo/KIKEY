package com.example.maple.kaka;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.app.AppController;
import com.example.maple.kaka.helper.SQLiteHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class insert extends AppCompatActivity implements View.OnClickListener {

    private static final int MENU_REQUEST = 0;
    private static final int IMG_REQUEST_01 = 1;
    private static final int IMG_REQUEST_02 = 2;
    private static final int IMG_REQUEST_03 = 3;
    private static final int IMG_REQUEST_04 = 4;
    private static final int IMG_REQUEST_05 = 5;
    private static final int IMG_REQUEST_06 = 6;
    private static final int RECORD_REQUEST_CODE = 101;
    private ProgressDialog pDialog;

    Button button_insert;

    ImageView inst_img[] = new ImageView[6];
    ImageView inst_Menu_img;
    EditText inst_edt[] = new EditText[6];
    EditText inst_ven_number, inst_ven_index;

    String insert_number, insert_index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ActionBarManager.getInstance().CreateLogoActionBar(this, "검색 광고 등록");

        SQLiteHandler db_KIKI;
        db_KIKI = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = new HashMap<>();
        user = db_KIKI.getVendorDetails();
        insert_number = user.get("number");
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permission!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("이미지 권한을 설정하셔야 이용가능합니다.").setTitle("이미지 가져오기 권한");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        makeRequest();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                makeRequest();
            }
        }

        inst_Menu_img = (ImageView)findViewById(R.id.inst_title_img);
        inst_img[0] = (ImageView)findViewById(R.id.inst_img01);
        inst_img[1] = (ImageView)findViewById(R.id.inst_img02);
        inst_img[2] = (ImageView)findViewById(R.id.inst_img03);
        inst_img[3] = (ImageView)findViewById(R.id.inst_img04);
        inst_img[4] = (ImageView)findViewById(R.id.inst_img05);
        inst_img[5] = (ImageView)findViewById(R.id.inst_img06);

        inst_edt[0] = (EditText)findViewById(R.id.inst_name01);
        inst_edt[1] = (EditText)findViewById(R.id.inst_name02);
        inst_edt[2] = (EditText)findViewById(R.id.inst_name03);
        inst_edt[3] = (EditText)findViewById(R.id.inst_name04);
        inst_edt[4] = (EditText)findViewById(R.id.inst_name05);
        inst_edt[5] = (EditText)findViewById(R.id.inst_name06);

        inst_ven_number = (EditText)findViewById(R.id.inst_ven_number);
        inst_ven_index = (EditText)findViewById(R.id.inst_ven_index);

        button_insert = (Button)findViewById(R.id.button_insert);

        inst_Menu_img.setOnClickListener(this);
        inst_img[0].setOnClickListener(this);
        inst_img[1].setOnClickListener(this);
        inst_img[2].setOnClickListener(this);
        inst_img[3].setOnClickListener(this);
        inst_img[4].setOnClickListener(this);
        inst_img[5].setOnClickListener(this);

        button_insert.setOnClickListener(this);
    }
    protected void makeRequest(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.inst_title_img :
                InsertImage_Select(MENU_REQUEST);
                break;

            case R.id.inst_img01 :
                InsertImage_Select(IMG_REQUEST_01);
                break;

            case R.id.inst_img02 :
                InsertImage_Select(IMG_REQUEST_02);
                break;

            case R.id.inst_img03 :
                InsertImage_Select(IMG_REQUEST_03);
                break;

            case R.id.inst_img04 :
                InsertImage_Select(IMG_REQUEST_04);
                break;

            case R.id.inst_img05 :
                InsertImage_Select(IMG_REQUEST_05);
                break;

            case R.id.inst_img06 :
                InsertImage_Select(IMG_REQUEST_06);
                break;

            case R.id.button_insert :
                if(!inst_edt[0].getText().toString().equals("") && !inst_edt[1].getText().toString().equals("") &&
                        !inst_edt[2].getText().toString().equals("") && !inst_edt[3].getText().toString().equals("") &&
                        !inst_edt[4].getText().toString().equals("") && !inst_edt[5].getText().toString().equals("")){

                    completeUploadImage();
                }
                else {
                    Toast.makeText(getApplicationContext(), "이름가격을 입력해주십시오", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private void InsertImage_Select(int i){
        Intent insertGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        insertGallery.setType(MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(insertGallery, i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MENU_REQUEST && resultCode == RESULT_OK && data != null){

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                inst_Menu_img.setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(inst_Menu_img);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == IMG_REQUEST_01 && resultCode == RESULT_OK && data != null){
            //Uri insertImage = data.getData();
            //inst_img[0].setImageURI(insertImage);

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                inst_img[0].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(inst_img[0]);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == IMG_REQUEST_02 && resultCode == RESULT_OK && data != null){
            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                inst_img[1].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(inst_img[1]);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == IMG_REQUEST_03 && resultCode == RESULT_OK && data != null){
            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                inst_img[2].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(inst_img[2]);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == IMG_REQUEST_04 && resultCode == RESULT_OK && data != null){
            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                inst_img[3].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(inst_img[3]);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == IMG_REQUEST_05 && resultCode == RESULT_OK && data != null){
            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                inst_img[4].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(inst_img[4]);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == IMG_REQUEST_06 && resultCode == RESULT_OK && data != null){
            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                inst_img[5].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(inst_img[5]);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private void completeUploadImage() {

        insert_index = inst_ven_index.getText().toString();
        Image_nameUp(insert_number, insert_index);
    }

    //////////////////////////////////////////////////////
    private void Image_nameUp(final String inst_number, final String inst_index)
    {
        // Tag used to cancel the request
        String tag_string_req = "Image_Request";
        pDialog.setMessage("이미지 업로드 중입니다.");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_VENDOR_PRODUCT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        // Inserting row in users table
                        Bitmap bitmap_up;

                        String Insert_Title_Name = inst_number+"MenuImage";

                        //각 이미지의 이름 설정
                        String product_data_num[] = new String[6];
                        for(int i=0; i<6; i++){
                            product_data_num[i] = inst_number + inst_edt[i].getText().toString();
                        }


                        Insert_Upload_Image[] Insert_ui;
                        Insert_ui = new Insert_Upload_Image[7];

                        for(int i=0; i<7; i++){
                            if(i < 6){
                                //0 1 2 3 4 5번째 이미지 뷰
                                Bitmap bit =((GlideBitmapDrawable)inst_img[i].getDrawable()).getBitmap();

                                Insert_ui[i] = new Insert_Upload_Image(bit, product_data_num[i]);
                                Insert_ui[i].execute();
                            }
                            else {
                                //타이틀 이미지 업로드
                                Bitmap bit_menu = ((GlideBitmapDrawable)inst_Menu_img.getDrawable()).getBitmap();
                                Bitmap resize_Bit = Bitmap.createScaledBitmap(bit_menu, 128, 96, true);

                                Insert_ui[i] = new Insert_Upload_Image(resize_Bit, Insert_Title_Name);
                                Insert_ui[i].execute();

                                //Toast.makeText(getApplicationContext(), "이미지가 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Toast.makeText(getApplicationContext(), "이미지 업로드 완료!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(insert.this, setting.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        Toast.makeText(getApplicationContext(), "이미지 업로드 실패!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
            protected Map<String, String> getParams()
            {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("_number", inst_number);
                params.put("title_image", inst_number+"MenuImage");
                params.put("product_data1", inst_number + inst_edt[0].getText().toString());
                params.put("product_data2", inst_number + inst_edt[1].getText().toString());
                params.put("product_data3", inst_number + inst_edt[2].getText().toString());
                params.put("product_data4", inst_number + inst_edt[3].getText().toString());
                params.put("product_data5", inst_number + inst_edt[4].getText().toString());
                params.put("product_data6", inst_number + inst_edt[5].getText().toString());
                params.put("_index", inst_index);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public class Insert_Upload_Image extends AsyncTask<Void, Void, Void>{

        Bitmap image;
        String name;

        public Insert_Upload_Image(Bitmap image, String name){
            this.image = image;
            this.name = name;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            data_to_send.add(new BasicNameValuePair("image", encodedImage));
            data_to_send.add(new BasicNameValuePair("name", name));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(AppConfig.URL_BASIC+"SavePicture.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                client.execute(post);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    private HttpParams getHttpRequestParams(){

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);

        return httpRequestParams;
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