package com.example.maple.kaka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PopupAcitivity extends Activity{

    private WebView browser;

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            String result_address = addressID(data);
            extra.putString("data", result_address);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    protected String addressID(String address){
            int index_number = address.indexOf(",");
        String add = address.substring(index_number+2);
        Log.e("Address_CUT", add);
        return add;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_acitivity);

        browser = (WebView) findViewById(R.id.webView);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                browser.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        browser.loadUrl("http://cdn.rawgit.com/jolly73-df/DaumPostcodeExample/master/DaumPostcodeExample/app/src/main/assets/daum.html");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}
