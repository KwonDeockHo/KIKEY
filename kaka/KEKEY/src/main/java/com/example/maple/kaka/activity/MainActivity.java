package com.example.maple.kaka.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.example.maple.kaka.ActionBarManager;
import com.example.maple.kaka.ManagementActivity;
import com.example.maple.kaka.R;
import com.example.maple.kaka.app.AppConfig;
import com.example.maple.kaka.helper.BottomNavigationViewHelper;
import com.example.maple.kaka.helper.SQLiteHandler;
import com.example.maple.kaka.helper.SessionManager;
import android.support.design.widget.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
	LocationListener locationListener;
	LocationManager locationManager;
	Double Latitude, Longitude;

	private final int START_TAB = 0;
	private final long BAKCKEY_END_DELAY_TIME = 2000;
    private static final int MAIN_ACTIVITY = 30000;
    public static final String USER_STATE = "Custom_State";
    SharedPreferences prefs;

	@Override
	protected void onResume() {
		super.onResume();
	}

	private long _backKeyPressedTime = 0;
	SQLiteHandler db_KIKI;
	private SessionManager session;


	String state, vendor_number;

	Button btn_search;
	EditText edt_search;


	//Bottom Navigation VIew
	private BottomNavigationView bottomNavigationView;
	private BottomNavigationMenuView menuView;

	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

        prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        state = prefs.getString(USER_STATE, "");

		HashMap<String, String> user = new HashMap<>();
		user.clear();
		GpsPermissionCheckForMashMallo(); // android 6.0 이상에서 호출해주어야 합니다.
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*지도 관련*/
		if(locationManager!=null){ // 위치정보 수집이 가능한 환경인지 검사.
			location_check();
		}


        //ActionBarManager.getInstance().CreateAdverActionBar(this, false);
        Log.e("Main_STATE", state.toString());
		if(state.equals("VENDOR")){
			ActionBarManager.getInstance().CreateAdverActionBar(this, false);
			db_KIKI = new SQLiteHandler(getApplicationContext());
			user = db_KIKI.getVendorDetails();
		}
		else if(state.equals("MEMBER")){
			ActionBarManager.getInstance().CreateMemAdverActionBar(this, false);
			db_KIKI = new SQLiteHandler(getApplicationContext());

			user = db_KIKI.getMemberDetails();
		}else{
			return;
		}
		// session manager
		session = new SessionManager(getApplicationContext());

		//여기 수정 - 세션
		if (!session.isLoggedIn()) {
			logout();
			Log.e("Session", "Login OUT 함수 호출");
		}else{
			//seesion 상태 아닐때 - 초기로 로그인 시 호출
			Log.e("Session", "Login In");
		}

		// SqLite database handler
		// Fetching user details from SQLite

		String name = user.get("name");
		String email = user.get("email");

		// Displaying the user details on the screen

		edt_search = (EditText)findViewById(R.id.edt_search);
		btn_search = (Button)findViewById(R.id.btn_ok);
		btn_search.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				pDialog.setMessage("리스트 로드 중입니다.");
				showDialog();

				new SearchTask().execute();
			}
		});


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
		/*
        //bottombar onClick Event
		navigation = (BottomNavigationView)findViewById(R.id.navigation);
        //bottombar Icon Color original
        navigation.setItemIconTintList(null);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()) {
					case R.id.bottom_bar_01:
						Toast.makeText(MainActivity.this, "현재 서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
						return true;
					case R.id.bottom_bar_02:
						Toast.makeText(MainActivity.this, "현재 서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
						return true;
					case R.id.bottom_bar_03:
						Toast.makeText(MainActivity.this, "현재 서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
						return true;
					case R.id.bottom_bar_04:
						Toast.makeText(MainActivity.this, "현재 서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
						return true;
				}

				return false;
			}
		});*/
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logout() {
		session.setLogin(false);
		if(state.equals("VENDOR")){
			Log.e("LOGOT", state);
			db_KIKI.deleteUsers();
		}
		else if(state.equals("MEMBER")){
			Log.e("LOGOT", state);
			db_KIKI.deleteUsers();
		}else{
			Log.e("LOGOT", "else");
			return;
		}
		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	private class SearchTask extends AsyncTask<Void, Void, String> {

		String targetURL;
		@Override
		protected void onPreExecute() {

			String index = edt_search.getText().toString();

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

			Log.e("onProgressUpdate", "on");
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			hideDialog();
			Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
			intent.putExtra("userList", result);
			Log.e("RESULT", result);
			MainActivity.this.startActivity(intent);
		}
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}
	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
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

					if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
						if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
							ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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
