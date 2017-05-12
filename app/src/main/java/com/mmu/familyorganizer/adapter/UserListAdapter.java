package com.mmu.familyorganizer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mmu.familyorganizer.MessageActivity;
import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.R;

import java.util.ArrayList;


public class UserListAdapter extends BaseAdapter {

    Context context;
    ArrayList<User> users;
    LayoutInflater inflater;

    public UserListAdapter(Context context,ArrayList<User> users) {
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
        final User user = users.get(i);
        holder.textView.setText(user.name);
        holder.numberview.setText(user.phone);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context,MessageActivity.class).putExtra("user",user));
            }
        });

        return view;
    }

    private class ViewHolder{
        public TextView textView,numberview;
        public ImageButton callBtn;
    }
}
