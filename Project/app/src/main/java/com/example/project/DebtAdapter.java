package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.AsyncTasks;
import com.example.project.DebtData;
import com.example.project.R;
import com.example.project.TransactionsRowData;

import java.util.ArrayList;

class DebtAdapter extends ArrayAdapter<DebtData> {
    public DebtAdapter(Context context, ArrayList<DebtData> videos) {
        super(context, 0, videos);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DebtData video = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.debts_item, parent, false);
        }
        TextView textView1 = (TextView) convertView.findViewById(R.id.should_pay_name);
        //take only the first name of the person
        String name = video.getName1();
        String[] names = name.split(" ");
        String name2 = video.getName2();
        String[] names2 = name2.split(" ");
        textView1.setText(names[0]);
        TextView textView2 = (TextView) convertView.findViewById(R.id.paid_to_name);
        textView2.setText(names2[0]);
        ImageView imgv1 = (ImageView) convertView.findViewById(R.id.member_img1);
        new AsyncTasks(video.getImg1(), imgv1).execute();
        ImageView imgv2 = (ImageView) convertView.findViewById(R.id.member_img2);
        new AsyncTasks(video.getImg2(), imgv2).execute();
        TextView textView3 = (TextView) convertView.findViewById(R.id.amount_id);
        String amount = video.getValue();
        if(amount.contains(".")){
            amount = amount.substring(0, amount.indexOf("."));
        }
        textView3.setText(amount);
        return convertView;
    }

}