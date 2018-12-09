package com.bamboospear.daily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private Context ctx;
    private List<MyData> data;

    public MyAdapter(Context ctx, List<MyData> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            view = inflater.inflate(R.layout.fruit_list, viewGroup, false);
        }

        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setImageBitmap(data.get(i).image);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(data.get(i).title);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(data.get(i).content);
        TextView weather = (TextView) view.findViewById(R.id.weather);
        weather.setText(data.get(i).weather);
        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(data.get(i).date);
        return view;
    }
}
