package com.example.maple.kaka;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.maple.kaka.activity.LoginActivity;
import com.example.maple.kaka.activity.RegisterActivity;
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

public class Advertisement extends AppCompatActivity  implements View.OnClickListener{
    private static final int AD_IMG_REQUEST_01 = 1;
    private static final int AD_IMG_REQUEST_02 = 2;
    private static final int AD_IMG_REQUEST_03 = 3;
    private static final int AD_IMG_REQUEST_04 = 4;
    private static final int AD_IMG_REQUEST_05 = 5;
    private static final int AD_IMG_REQUEST_06 = 6;
    private static final int RECORD_REQUEST_CODE = 101;
    private ProgressDialog pDialog;

    ImageView ad_img[] = new ImageView[6];
    EditText ad_point,  ad_hour, ad_count;
    Button ad_button;
    String ad_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        ActionBarManager.getInstance().CreateLogoActionBar(this, "페이지 광고 등록");

        SQLiteHandler db_KIKI;
        db_KIKI = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = new HashMap<>();
        user = db_KIKI.getVendorDetails();
        ad_number = user.get("number");
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permission!= PackageManager.PERMISSION_GRANTED){
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

        ad_point = (EditText)findViewById(R.id.ad_point);
        ad_hour = (EditText)findViewById(R.id.ad_hour);
        ad_count = (EditText)findViewById(R.id.ad_count);


        ad_img[0] = (ImageView)findViewById(R.id.ad_img01);
        ad_img[1] = (ImageView)findViewById(R.id.ad_img02);
        ad_img[2] = (ImageView)findViewById(R.id.ad_img03);
        ad_img[3] = (ImageView)findViewById(R.id.ad_img04);
        ad_img[4] = (ImageView)findViewById(R.id.ad_img05);
        ad_img[5] = (ImageView)findViewById(R.id.ad_img06);


        ad_button = (Button)findViewById(R.id.ad_Button);


        ad_img[0].setOnClickListener(this);
        ad_img[1].setOnClickListener(this);
        ad_img[2].setOnClickListener(this);
        ad_img[3].setOnClickListener(this);
        ad_img[4].setOnClickListener(this);
        ad_img[5].setOnClickListener(this);

        ad_button.setOnClickListener(this);

    }

    protected void makeRequest(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RECORD_REQUEST_CODE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.ad_img01 :
                Select_adImage(AD_IMG_REQUEST_01);
                break;


            case R.id.ad_img02 :
                Select_adImage(AD_IMG_REQUEST_02);
                break;


            case R.id.ad_img03 :
                Select_adImage(AD_IMG_REQUEST_03);
                break;


            case R.id.ad_img04 :
                Select_adImage(AD_IMG_REQUEST_04);
                break;


            case R.id.ad_img05 :
                Select_adImage(AD_IMG_REQUEST_05);
                break;


            case R.id.ad_img06 :
                Select_adImage(AD_IMG_REQUEST_06);
                break;

            case R.id.ad_Button:
                if(!ad_point.getText().toString().equals("") && !ad_hour.getText().toString().equals("") && !ad_count.getText().toString().equals("")){
                    checkUploadImage();
                }
                else{
                    Toast.makeText(getApplicationContext(), "모든 정보를 입력해주십시오", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }//end onClick


    private void Select_adImage(int i){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(galleryIntent, i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AD_IMG_REQUEST_01 && resultCode == RESULT_OK && data != null){

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ad_img[0].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ad_img[0]);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(requestCode == AD_IMG_REQUEST_02 && resultCode == RESULT_OK && data != null){

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ad_img[1].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ad_img[1]);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(requestCode == AD_IMG_REQUEST_03 && resultCode == RESULT_OK && data != null){

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ad_img[2].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ad_img[2]);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(requestCode == AD_IMG_REQUEST_04 && resultCode == RESULT_OK && data != null){

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ad_img[3].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ad_img[3]);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(requestCode == AD_IMG_REQUEST_05 && resultCode == RESULT_OK && data != null){

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ad_img[4].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ad_img[4]);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else if(requestCode == AD_IMG_REQUEST_06 && resultCode == RESULT_OK && data != null){

            try{
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ad_img[5].setImageBitmap(image_bitmap);
                Glide.with(this).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ad_img[5]);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }//end onActivityResult


    private void checkUploadImage(){

        String I_ad_point = ad_point.getText().toString();
        Image_nameUp(ad_number, I_ad_point);

    }//end checkUploadImage
    private void Image_nameUp(final String I_ad_ven_number, final String I_ad_point)
    {
        // Tag used to cancel the request
        String tag_string_req = "Image_Request";
        pDialog.setMessage("이미지 업로드 중입니다.");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_ADVER_INFO, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("success");
                    if (error) {
                        // Inserting row in users table
                        Bitmap bitmap_up;

                        for(int i=0; i<6; i++){
                            bitmap_up = ((GlideBitmapDrawable)ad_img[i].getDrawable()).getBitmap();
                            Bitmap resize_Bit = Bitmap.createScaledBitmap(bitmap_up, 128, 96, true);

                            UploadImage[] uli;
                            uli = new UploadImage[6];

                            uli[i] = new UploadImage(resize_Bit, I_ad_ven_number+"AD_image"+(i+1));
                            uli[i].execute();
                        }

                        Toast.makeText(getApplicationContext(), "이미지 업로드 완료!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(Advertisement.this, setting.class);
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
                params.put("_number", I_ad_ven_number);
                params.put("ad_point", I_ad_point);
                params.put("ad_image1", I_ad_ven_number+"AD_image"+1);
                params.put("ad_image2", I_ad_ven_number+"AD_image"+2);
                params.put("ad_image3", I_ad_ven_number+"AD_image"+3);
                params.put("ad_image4", I_ad_ven_number+"AD_image"+4);
                params.put("ad_image5", I_ad_ven_number+"AD_image"+5);
                params.put("ad_image6", I_ad_ven_number+"AD_image"+6);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public class UploadImage extends AsyncTask<Void, Void, Void>{

        Bitmap image;
        String name;

        public UploadImage(Bitmap image, String name){
            this.image = image;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodeedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataTosend = new ArrayList<>();
            dataTosend.add(new BasicNameValuePair("image", encodeedImage));
            dataTosend.add(new BasicNameValuePair("name", name));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(AppConfig.URL_BASIC + "SavePicture.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataTosend));
                client.execute(post);

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
