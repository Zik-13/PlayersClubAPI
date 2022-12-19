package com.example.playersclubapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptStaff extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<Staff> staffs;
    AdaptStaff(Context context, ArrayList<Staff> staffs){
        this.context = context;
        this.staffs = staffs;
        lInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return staffs.size();
    }

    @Override
    public Object getItem(int i) {
        return staffs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View vieww, ViewGroup viewGroup) {

        View view = vieww;
        if (view == null) {
            view = lInflater.inflate(R.layout.gridviewlayout, viewGroup, false);
        }

        if (view == null) {
            view = lInflater.inflate(R.layout.gridviewlayout, viewGroup,false);
        }

        Staff staff = getStaff(i);

        ((TextView) view.findViewById(R.id.name)).setText(staff.name);
        ((TextView) view.findViewById(R.id.phone)).setText(staff.phone);
        ((TextView) view.findViewById(R.id.email)).setText(staff.email);
        if(staff.imageB != null){
            ((ImageView) view.findViewById(R.id.image)).setImageBitmap(staff.imageB);
        }
        else {
            ((ImageView) view.findViewById(R.id.image)).setImageResource(R.drawable.ic_baseline_person_24);
        }
        return view;
    }
    Staff getStaff(int pos){
        return ((Staff) getItem(pos));
    }
}
