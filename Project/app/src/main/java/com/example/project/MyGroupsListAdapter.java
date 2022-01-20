package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MyGroupsListAdapter extends ArrayAdapter<MyGroupsRowData>{
    public MyGroupsListAdapter(Context context, ArrayList<MyGroupsRowData> videos) {
        super(context, 0, videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGroupsRowData video = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_groups_row, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.VideoRowTitle);
        textView.setText(video.name);
        TextView textView2 = (TextView) convertView.findViewById(R.id.VideoRowDuration);
        textView2.setText(video.Duration);
        return convertView;
    }

}