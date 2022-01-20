package com.example.project;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class DropDownAdapter extends ArrayAdapter<DropDownData> {
    LayoutInflater inflater;
    ArrayList<DropDownData> objects;
    static class ViewHolder {
        TextView name;
        ImageView img;
    }
    ViewHolder viewHolder = null;

    public DropDownAdapter(Context context, int textViewResourceId, ArrayList<DropDownData> objects) {
        super(context, textViewResourceId, objects);
        inflater = ((Activity) context).getLayoutInflater();
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        DropDownData listItemAddProg = objects.get(position);
        View row = convertView;
        if (null == row) {
            viewHolder = new ViewHolder();
            row = inflater.inflate(R.layout.paid_dropdown, parent, false);
            viewHolder.name = (TextView) row.findViewById(R.id.spinner_name);
            viewHolder.img = (ImageView) row.findViewById(R.id.spinner_img);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.name.setText(listItemAddProg.getName());
        new AsyncTasks(listItemAddProg.getUserImg(), viewHolder.img).execute();
        return row;
    }

}