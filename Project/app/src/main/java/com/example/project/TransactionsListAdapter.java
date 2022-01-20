package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class TransactionsListAdapter extends ArrayAdapter<TransactionsRowData>{
    public TransactionsListAdapter(Context context, ArrayList<TransactionsRowData> videos) {
        super(context, 0, videos);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TransactionsRowData video = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_transactions_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.guyWhoPaid);
        textView.setText(video.expense);
        ImageView textView2 = (ImageView) convertView.findViewById(R.id.user_paid_img);
        new AsyncTasks(video.img, textView2).execute();
        return convertView;
    }

}