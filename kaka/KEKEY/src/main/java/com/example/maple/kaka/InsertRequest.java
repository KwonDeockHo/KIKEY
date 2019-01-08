package com.example.maple.kaka;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.maple.kaka.app.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maple on 2017-08-24.
 */

public class InsertRequest extends StringRequest {

    private Map<String, String> parameters;

    public InsertRequest(String _number, String title_image, String product_data1, String product_data2, String product_data3,
                         String product_data4, String product_data5, String product_data6, String _index, Response.Listener<String> listener){

        super(Method.POST, AppConfig.URL_VENDOR_PRODUCT, listener, null);

        parameters = new HashMap<>();
        parameters.put("_number", _number);
        parameters.put("title_image", title_image);
        parameters.put("product_data1", product_data1);
        parameters.put("product_data2", product_data2);
        parameters.put("product_data3", product_data3);
        parameters.put("product_data4", product_data4);
        parameters.put("product_data5", product_data5);
        parameters.put("product_data6", product_data6);
        parameters.put("_index", _index);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
