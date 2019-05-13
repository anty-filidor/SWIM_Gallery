package com.example.gallery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageDetailsSlidePageFragmentAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    ArrayList<ArrayList<String>> data;

    public ImageDetailsSlidePageFragmentAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.detailTitle = (TextView) row.findViewById(R.id.detail_title);
            holder.detailValue = (TextView) row.findViewById(R.id.detail_value);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        String valueItem = (String) data.get(0).get(position);
        String titleItem = (String) data.get(1).get(position);
        holder.detailValue.setText(valueItem);
        holder.detailTitle.setText(titleItem);
        return row;
    }


    static class ViewHolder {
        TextView detailTitle;
        TextView detailValue;
    }

    @Override
    public int getCount(){
        return data.get(0).size();
    }
}