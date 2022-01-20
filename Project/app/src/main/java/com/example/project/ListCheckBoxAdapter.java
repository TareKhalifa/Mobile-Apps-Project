package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ListCheckBoxAdapter extends ArrayAdapter<DropDownData> implements CompoundButton.OnCheckedChangeListener
{
    static class MembersInvolvedChooser
    {
        ImageView usrImg;
        TextView memberID;
        CheckBox chkSelect;
    }
    SparseBooleanArray mCheckStates;
    Context context;
    int layoutResourceId;
    DropDownData[] data= null;

    public ListCheckBoxAdapter(Context context, int layoutResourceId, DropDownData[] data){
        super(context, layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        mCheckStates = new SparseBooleanArray(data.length);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View row = convertView;
        MembersInvolvedChooser holder= null;

        if (row == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MembersInvolvedChooser();

            holder.usrImg = (ImageView) row.findViewById(R.id.member_img);
            holder.memberID = (TextView) row.findViewById(R.id.member_name);
            holder.chkSelect = (CheckBox) row.findViewById(R.id.list_view_item_checkbox);
            row.setTag(holder);

        }
        else{
            holder = (MembersInvolvedChooser)row.getTag();
        }


        DropDownData currentUser = data[position];
        holder.memberID.setText(currentUser.getName());
        new AsyncTasks(currentUser.getUserImg(), holder.usrImg).execute();
        holder.chkSelect.setChecked(true);
        holder.chkSelect.setTag(position);
        holder.chkSelect.setChecked(mCheckStates.get(position, false));
        holder.chkSelect.setOnCheckedChangeListener(this);
        return row;

    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }
}