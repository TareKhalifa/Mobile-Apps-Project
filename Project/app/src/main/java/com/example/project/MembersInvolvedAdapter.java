package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class MembersInvolvedAdapter extends ArrayAdapter<DropDownData>{
    public MembersInvolvedAdapter(Context context, ArrayList<DropDownData> genres) {
        super(context, 0, genres);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DropDownData genre = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.members_involved_row, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.member_img);
        new AsyncTasks(genre.userImg, imageView).execute();

        TextView textView = (TextView) convertView.findViewById(R.id.member_name);
        textView.setText(genre.name);

        return convertView;
    }
}