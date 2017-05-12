package com.mmu.familyorganizer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.mmu.familyorganizer.Model.Group;
import com.mmu.familyorganizer.R;

import java.util.ArrayList;


public class GroupListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Group> groups;
    LayoutInflater inflater;

    public GroupListAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.user_row,null);
            holder.textView = (TextView) view.findViewById(R.id.name);
            holder.numberview = (TextView) view.findViewById(R.id.number);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Group group = groups.get(i);
        holder.textView.setText(group.groupName);
        holder.numberview.setText("");

        return view;
    }

    private class ViewHolder{
        public TextView textView,numberview;
        public ImageButton callBtn;
    }
}
