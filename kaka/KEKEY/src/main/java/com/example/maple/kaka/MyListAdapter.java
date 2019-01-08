package com.example.maple.kaka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by maple on 2017-08-31.
 */

public class MyListAdapter extends BaseAdapter{
    Context context;
    ArrayList<list_item> list_itemArrayList;
    ViewHolder viewholder;


    public MyListAdapter(Context context, ArrayList<list_item> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {
        return this.list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);

            viewholder = new ViewHolder();
            viewholder._corname = (TextView)convertView.findViewById(R.id.list_corname);

            viewholder._image = (ImageView)convertView.findViewById(R.id._image);


            viewholder.EVENT_tv = (TextView)convertView.findViewById(R.id.EVENT_tv);


            viewholder.tv_distance = (TextView)convertView.findViewById(R.id.tv_distance);



            convertView.setTag(viewholder);

        }else{
            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder._corname.setText(list_itemArrayList.get(position).get_corname());

        String event_state = list_itemArrayList.get(position).getshopstate();
        Double distance_value = 0.0;
        int value = 0;
        try{
            distance_value = Double.parseDouble(list_itemArrayList.get(position).getDistance().toString());
            value = distance_value.intValue();
            if(value >= 5000){
                value = 5000;
            }else{
                value = value;
            }

        }catch (NumberFormatException e){
            value = 0;
        }

        viewholder.tv_distance.setText( value+"m");

        if(event_state.toString().equals("TRUE"))
            viewholder.EVENT_tv.setText("EVENT");
        else{
            viewholder.EVENT_tv.setText("");

        }


        Glide.with(context).load(list_itemArrayList.get(position).get_image()).into(viewholder._image);


        return convertView;
    }

    class ViewHolder{

        TextView _corname;
        ImageView _image;
        TextView EVENT_tv, tv_distance;


    }
}
