package com.npes87184.enviromenttw.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.npes87184.enviromenttw.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npes87184 on 2015/5/3.
 */
public class UVAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<DataContainer> uv;
    private int selectItem = -1;
    private boolean isStar = false;
    private ArrayList<Boolean> star;
    Context c;

    public UVAdapter(Context context, List<DataContainer> uv){
        c = context;
        myInflater = LayoutInflater.from(context);
        this.uv = uv;
    }

    @Override
    public int getCount() {
        return uv.size();
    }

    @Override
    public Object getItem(int arg0) {
        return uv.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return uv.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView value;
        ImageView color;
        ImageView star;
        public ViewHolder(TextView value, ImageView color, ImageView star) {
            this.value = value;
            this.color = color;
            this.star = star;
        }
    }

    public void setSelectItem(int selectItem, boolean isStar) {
        this.selectItem = selectItem;
        star.set(selectItem, isStar);
    }

    public void init(ArrayList<Boolean> star) {
        this.star = star;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        convertView = myInflater.inflate(R.layout.list_item, null);
        holder = new ViewHolder(
                (TextView) convertView.findViewById(R.id.textView),
                (ImageView) convertView.findViewById(R.id.imageView),
                (ImageView) convertView.findViewById(R.id.imageView2)
        );

        DataContainer line = (DataContainer)getItem(position);
        try {
            holder.value.setText(line.getLocation() + ":" + line.getValue());
            if(Float.parseFloat(line.getValue())<3) {
                holder.color.setImageResource(R.drawable.good);
            } else if(Float.parseFloat(line.getValue())<6) {
                holder.color.setImageResource(R.drawable.normal);
            } else {
                holder.color.setImageResource(R.drawable.bad);
            }
        } catch (Exception e) {
            holder.value.setText(line.getLocation() + ":" + c.getString(R.string.nodata));
        }


        if (!star.get(position)) {
            holder.star.setImageResource(R.drawable.btn_star_on_disabled_focused_holo_dark);
        }
        if(star.get(position)) {
            holder.star.setImageResource(R.drawable.btn_star_on_pressed_holo_dark);
        }

        return convertView;
    }

    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
