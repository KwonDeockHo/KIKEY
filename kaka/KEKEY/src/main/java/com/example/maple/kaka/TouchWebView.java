package com.example.maple.kaka;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by kwon on 2017-08-16.
 */

public class TouchWebView extends WebView {
    public TouchWebView(Context context){ super(context); }

    public TouchWebView(Context context, AttributeSet attrs){ super(context, attrs); }

    public TouchWebView(Context context, AttributeSet attrs, int defStyle){ super(context, attrs, defStyle); }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
