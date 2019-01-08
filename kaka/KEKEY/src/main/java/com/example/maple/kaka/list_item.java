package com.example.maple.kaka;

/**
 * Created by maple on 2017-08-30.
 */

public class list_item {

    private String _image;
    private String _corname;
    private String distance;
    private String shopstate;

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }


    public String get_corname() {
        return _corname;
    }

    public void set_corname(String _corname) {
        this._corname = _corname;
    }


    public void setshopstate(String shopstate) {
        this.shopstate = shopstate;
    }

    public String getshopstate() {
        return shopstate;
    }


    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }


    public list_item(String _corname, String _image, String shopstate, String distance) {
        this._corname = _corname;
        this._image = _image;
        this.shopstate=shopstate;
        this.distance = distance;
    }
}
