package com.example.maple.kaka;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maple.kaka.activity.MainActivity;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.helper.BottomNavigationViewHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ManagementActivity extends AppCompatActivity implements View.OnClickListener{
    LocationListener locationListener;
    LocationManager locationManager;
    Double Latitude, Longitude;
    ListView listView;
    MyListAdapter myListAdapter;
    ArrayList<list_item> list_itemArrayList;

    //다시 검색 했을때
    Button btn_search;
    EditText edt_search;

    //Bottom Navigation VIew
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationMenuView menuView;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        ActionBarManager.getInstance().CreateLogoActionBar(this, "검색 결과");
        GpsPermissionCheckForMashMallo(); // android 6.0 이상에서 호출해주어야 합니다.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Intent intent = getIntent();

        listView = (ListView)findViewById(R.id.listView);

        list_itemArrayList = new ArrayList<list_item>();


        //Bottom Navigation View
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        bottomNavigationView.setItemIconTintList(null);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_key :
                        Toast.makeText(getApplicationContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_talk :
                        Toast.makeText(getApplicationContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_issue :
                        Toast.makeText(getApplicationContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_share :
                        Toast.makeText(getApplicationContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });


        myListAdapter = new MyListAdapter(ManagementActivity.this,list_itemArrayList);
        listView.setAdapter(myListAdapter);

        try {
            Log.e("TRY", intent.getStringExtra("userList"));

            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");

            int count = 0;
            String shopID, shopTel, shopImg, shopstate;
            String distance;

            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);
                shopID = object.getString("_corname");
                shopTel = object.getString("_phone");
                shopImg = object.getString("_number");
                shopstate = object.getString("shopState");
                distance = object.getString("distance");

                Log.e("_cornameLog", shopID);
                Log.e("ShopState_distance", distance+"");

                Log.e("ShopState", shopstate);
                list_item list_items = new list_item(shopID, "http://kikitest.000webhostapp.com/pictures/"+shopImg+"MenuImage.PNG", shopstate, distance);

                list_itemArrayList.add(list_items);

                count++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //////////////////////////////////////////////////////////////////////////////////
        // 리스트뷰 클릭할때

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                        TextView clickListText = (TextView) view.findViewById(R.id.list_corname);
                        String listviewText = clickListText.getText().toString();

                        Intent searchintent = new Intent(ManagementActivity.this, SelectActivity.class);
                        searchintent.putExtra("clickList", listviewText);
                        ManagementActivity.this.startActivity(searchintent);

                        //Toast.makeText(ManagementActivity.this, listviewText, Toast.LENGTH_SHORT).show();
                    }
                }
        );
////////////////////////////
        /*지도 관련*/
        if(locationManager!=null){ // 위치정보 수집이 가능한 환경인지 검사.
            location_check();
        }
        //////////////////////////////////////////////////////////////////////////////////
        // 재검색
        edt_search = (EditText) findViewById(R.id.edt_search);
        btn_search = (Button) findViewById(R.id.btn_ok);

        btn_search.setOnClickListener(this);
    }

    protected void location_check(){
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled || isNetworkEnabled) {
            Log.e("GPS Enable", "true");

            final List<String> m_lstProviders = locationManager.getProviders(false);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.e("onLocationChanged", "onLocationChanged");
                    Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
                    Log.e("location", location.getLatitude()+"");

                    Latitude= location.getLatitude();
                    Longitude = location.getLongitude();

                    if (ActivityCompat.checkSelfPermission(ManagementActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ManagementActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.removeUpdates(locationListener);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("onStatusChanged", "onStatusChanged");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("onProviderEnabled", "onProviderEnabled");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("onProviderDisabled", "onProviderDisabled");
                }
            };

            // QQQ: 시간, 거리를 0 으로 설정하면 가급적 자주 위치 정보가 갱신되지만 베터리 소모가 많을 수 있다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String name : m_lstProviders) {
                        if (ActivityCompat.checkSelfPermission(ManagementActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ManagementActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        locationManager.requestLocationUpdates(name, 1000, 0, locationListener);
                    }
                }
            });

        } else {
            Log.e("GPS Enable", "false");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        Log.e("onResume", "");
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_ok) {
            pDialog.setMessage("광고 준비 중입니다.");
            showDialog();

            new SearchTask().execute();
        }

    }

    private class SearchTask extends AsyncTask<Void, Void, String> {
        String targetURL;
        @Override
        protected void onPreExecute() {

            String index = edt_search.getText().toString();
            Log.e("_Latitude", Latitude+"");
            Log.e("_Longitude", Longitude+"");

            //GMC12 wifi = 192.168.43.123
            targetURL = AppConfig.URL_BASIC + "kiki_/kiList.php?_corname="+index+"&_Latitude="+Latitude+"&_Longitude="+Longitude;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
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

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            hideDialog();
            Intent intent = new Intent(ManagementActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ManagementActivity.this.startActivity(intent);
        }
    }//end searchTask
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //GPS 퍼미션 문제
    public void GpsPermissionCheckForMashMallo() {

        //마시멜로우 버전 이하면 if문에 걸리지 않습니다.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS 사용 허가 요청");
            alertDialog.setMessage("앰버요청 발견을 알리기위해서는 사용자의 GPS 허가가 필요합니다.\n('허가'를 누르면 GPS 허가 요청창이 뜹니다.)");
            // OK 를 누르게 되면 설정창으로 이동합니다.
            alertDialog.setPositiveButton("허가",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ManagementActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        }
                    });
            // Cancle 하면 종료 합니다.
            alertDialog.setNegativeButton("거절",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }
}