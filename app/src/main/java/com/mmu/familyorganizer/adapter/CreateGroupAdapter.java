package com.mmu.familyorganizer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.mmu.familyorganizer.CreateGroupActivity;
import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.R;

import java.util.ArrayList;


public class CreateGroupAdapter extends BaseAdapter {

    Context context;
    ArrayList<User> users;
    public ArrayList<Boolean> select = new ArrayList<>();
    LayoutInflater inflater;

    public CreateGroupAdapter(Context context,ArrayList<User> users) {
        for(int i=0;i<users.size();i++){
            select.add(false);
        }
        this.context = context;
        this.users = users;
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return users.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.group_user_row,null);
            holder.textView = (TextView) view.findViewById(R.id.name);
            holder.numberview = (TextView) view.findViewById(R.id.number);
            holder.check = (CheckBox) view.findViewById(R.id.check);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final User user = users.get(i);
        holder.textView.setText(user.name);
        holder.numberview.setText(user.phone);

        if(select.get(i) == true){
            holder.check.setChecked(true);
        }else{
            holder.check.setChecked(false);
        }

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                select.set(i,b);
                if(b) {
                    CreateGroupActivity.selectedUsers.add(user);
                }else{
                    CreateGroupActivity.selectedUsers.remove(i);
                }
            }
        });

        return view;
    }

    private class ViewHolder{
        public TextView textView,numberview;
        public CheckBox check;
    }
}
