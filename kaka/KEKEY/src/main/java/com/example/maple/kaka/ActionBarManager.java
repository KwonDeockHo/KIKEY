package com.example.maple.kaka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maple.kaka.activity.MainActivity;
import com.example.maple.kaka.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kwon on 2017-08-15.
 */

public class ActionBarManager {
    Context context;

    private static ActionBarManager ourInstance = new ActionBarManager();
    public static ActionBarManager getInstance(){
        return ourInstance;
    }

    private ActionBarManager(){

    }
    public void CreateSimpleActionBar(AppCompatActivity activity, @StringRes int resource)
    {
        CreateSimpleActionBar(activity, activity.getResources().getString(resource));
    }

    public void CreateSimpleActionBar(AppCompatActivity activity, String title)
    {
        View view = CreateActionBar(activity, R.layout.actionbar_simple);
        TextView titleText = (TextView)view.findViewById(R.id.Title);
        titleText.setText(title);
    }
    public void CreateEmptyActionBar(AppCompatActivity activity, @StringRes int resource)
    {
        CreateEmptyActionBar(activity, activity.getResources().getString(resource));
    }

    public void CreateEmptyActionBar(AppCompatActivity activity ,  String title)
    {
        View view = CreateActionBarEmpty(activity, R.layout.actionbar_simple);
    }

    public void CreateAdverActionBar(AppCompatActivity activity, @BoolRes int  resource)
    {
        CreateAdverActionBar(activity, activity.getResources().getBoolean(resource));
    }

    public void CreateAdverActionBar(AppCompatActivity activity, boolean resource)
    {
        View view = CreateActionBar(activity, R.layout.actionbar_adver);
        Switch bt_switch = (Switch)view.findViewById(R.id.bt_switch);

        view.findViewById(R.id.bt_option).setOnClickListener(new CloseListener(activity));
        bt_switch.setChecked(resource);
        ((Switch) view.findViewById(R.id.bt_switch)).setOnCheckedChangeListener(new SwitchListener(activity, resource));
        view.findViewById(R.id.bt_option).setOnClickListener(new OptionListener(activity));
    }

    public void CreateLogoActionBar(AppCompatActivity activity, @StringRes int resource)
    {
        CreateLogoActionBar(activity, activity.getResources().getString(resource));
    }

    public void CreateLogoActionBar(AppCompatActivity activity, String title)
    {
        View view = CreateActionBar(activity, R.layout.actionbar_logo);
        TextView titleText = (TextView)view.findViewById(R.id.Title);
        titleText.setText(title);
        view.findViewById(R.id.bt_option).setOnClickListener(new OptionListener(activity));

        Button logo_back = (Button)view.findViewById(R.id.action_logo_back);
        view.findViewById(R.id.action_logo_back).setOnClickListener(new logo_ButtonListener(activity));
    }

    public void CreateMemLogoActionBar(AppCompatActivity activity, @StringRes int resource)
    {
        CreateLogoActionBar(activity, activity.getResources().getString(resource));
    }

    public void CreateMemLogoActionBar(AppCompatActivity activity, String title)
    {
        View view = CreateActionBar(activity, R.layout.actionbar_logo);
        TextView titleText = (TextView)view.findViewById(R.id.Title);
        titleText.setText(title);
        view.findViewById(R.id.bt_option).setOnClickListener(new MemOptionListener(activity));
    }
    private View CreateActionBarEmpty(AppCompatActivity activity, @LayoutRes int resource)
    {
        ActionBar actionBar = activity.getSupportActionBar();
        View customView = LayoutInflater.from(activity).inflate(resource,null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(customView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));

        actionBar.hide();

        return customView;
    }
    public void CreateMemAdverActionBar(AppCompatActivity activity, @BoolRes int  resource)
    {
        CreateMemAdverActionBar(activity, activity.getResources().getBoolean(resource));
    }

    public void CreateMemAdverActionBar(AppCompatActivity activity, boolean resource)
    {
        View view = CreateActionBar(activity, R.layout.actionbar_adver);
        Switch bt_switch = (Switch)view.findViewById(R.id.bt_switch);

        view.findViewById(R.id.bt_option).setOnClickListener(new CloseListener(activity));
        bt_switch.setChecked(resource);
        ((Switch) view.findViewById(R.id.bt_switch)).setOnCheckedChangeListener(new SwitchListener(activity, resource));
        view.findViewById(R.id.bt_option).setOnClickListener(new MemOptionListener(activity));
    }

    private View CreateActionBar(AppCompatActivity activity, @LayoutRes int resource)
    {
        ActionBar actionBar = activity.getSupportActionBar();

        View customView = LayoutInflater.from(activity).inflate(resource, null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(customView, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));


        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        return customView;
    }
    class SwitchListener implements CompoundButton.OnCheckedChangeListener{
        private Activity _activity;
        private boolean _isChecked;

        private ProgressDialog pDialog;

        public SwitchListener(Activity activity, boolean isChecked)
        {
            _activity = activity;
            _isChecked = isChecked;

            pDialog = new ProgressDialog(_activity);
            pDialog.setCancelable(false);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if(isChecked){
                pDialog.setMessage("광고 준비 중입니다.");
                showDialog();

                new AD_arr().execute();
            }
            else {
                Intent i = new Intent(_activity, MainActivity.class);
                _activity.startActivity(i);
            }
        }




        private class AD_arr extends AsyncTask<Void, Void, String> {

            String targetURL;
            @Override
            protected void onPreExecute() {
                //GMC12 wifi = 192.168.43.123
                targetURL = AppConfig.URL_BASIC+"kiki_/count_AD_table.php";
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
                try {
                    JSONObject jsonObject =new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");

                    //광고 데이터베이스에 _number의 개수만큼 담음
                    int count = jsonArray.length();

                    //개수만큼의 배열을 생성
                    String[] ad_arr_num;
                    ad_arr_num = new String[count];

                    //광고가 담겨있는 배열의 번호 선정
                    int random_num = (int)(Math.random()*count);



                    for(int i=0; i<count; i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        ad_arr_num[i] = object.getString("_number_check"+i);

                    }
                    String play_adv_number = ad_arr_num[random_num];

                    Log.i("Random",play_adv_number);


                    hideDialog();
                    Intent intent = new Intent(_activity, Main2.class);
                    intent.putExtra("AD_arr", play_adv_number);
                    _activity.startActivity(intent);

                    /*
                    Intent intent = new Intent(MainActivity.this, Main2.class);
                    intent.putExtra("AD_arr", play_adv_number);
                    MainActivity.this.startActivity(intent);*/

                }catch (Exception e){
                    e.printStackTrace();
                }


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
    }
    class CloseListener implements View.OnClickListener
    {
        private Activity _activity;

        public CloseListener(Activity activity)
        {
            _activity = activity;
        }

        @Override
        public void onClick(View v)
        {
            _activity.finish();
        }
    }
    class OptionListener implements View.OnClickListener
    {
        private Activity _activity;

        public OptionListener(Activity activity)
        {
            _activity = activity;
        }

        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(_activity, setting.class);
            _activity.startActivity(i);
        }
    }

    class logo_ButtonListener implements View.OnClickListener{
        private  Activity _activity;

        public logo_ButtonListener(Activity activity){ _activity = activity;}

        @Override
        public void onClick(View view) {

            _activity.finish();

        }
    }

    class MemOptionListener implements View.OnClickListener
    {
        private Activity _activity;

        public MemOptionListener(Activity activity)
        {
            _activity = activity;
        }

        @Override
        public void onClick(View v)
        {
            Intent i = new Intent(_activity, member_setting.class);
            _activity.startActivity(i);
        }
    }

}
