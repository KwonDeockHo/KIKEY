package com.example.maple.kaka;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.maple.kaka.app.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maple on 2017-08-27.
 */

public class AdvertisementRequest extends StringRequest{
    final static private String URL = AppConfig.URL_BASIC+"kiki_/ki_Advertisement.php";
    private Map<String, String> ad_parameters;

    public AdvertisementRequest(String I_ad_number, String I_ad_point, String ad_image1, String ad_image2, String ad_image3,
                         String ad_image4, String ad_image5, String ad_image6, Response.Listener<String> listener){

        //super(Request.Method.POST, URL, listener, null);
        super(Method.POST, URL, listener, null);

        ad_parameters = new HashMap<>();
        ad_parameters.put("_number", I_ad_number);
        ad_parameters.put("ad_point", I_ad_point);
        ad_parameters.put("ad_image1", ad_image1);
        ad_parameters.put("ad_image2", ad_image2);
        ad_parameters.put("ad_image3", ad_image3);
        ad_parameters.put("ad_image4", ad_image4);
        ad_parameters.put("ad_image5", ad_image5);
        ad_parameters.put("ad_image6", ad_image6);

    }

    @Override
    public Map<String, String> getParams() {
        return ad_parameters;
    }
}
