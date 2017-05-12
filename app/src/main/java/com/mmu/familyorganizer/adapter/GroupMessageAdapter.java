package com.mmu.familyorganizer.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mmu.familyorganizer.GroupMessageActivity;
import com.mmu.familyorganizer.Model.GroupMessage;
import com.mmu.familyorganizer.R;
import com.mmu.familyorganizer.Uitlity.Prefs;

import java.util.ArrayList;


public class GroupMessageAdapter extends BaseAdapter {

    Context context;
    ArrayList<GroupMessage> messages;
    LayoutInflater inflater;
    Prefs prefs;

    public GroupMessageAdapter(Context context, ArrayList<GroupMessage> messages) {
        this.context = context;
        this.messages = messages;
        prefs = new Prefs(context);
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
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
            view = inflater.inflate(R.layout.message_row,null);
            holder.textView = (TextView) view.findViewById(R.id.text);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.ll = (LinearLayout) view.findViewById(R.id.view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        GroupMessage message = messages.get(i);
        holder.textView.setText(message.msg);
        holder.time.setText(message.time);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
        params.weight = 1.0f;


        if(GroupMessageActivity.ME.key.equals(message.sender.key)){
            holder.textView.setGravity(Gravity.RIGHT);
            view.setPadding(90,0,0,0);
            params.gravity = Gravity.RIGHT;
            holder.ll.setBackgroundResource(R.drawable.border_bg);
            holder.textView.setTextColor(context.getResources().getColor(R.color.black));
            holder.name.setText(prefs.getString(Prefs.NAME));
        }else{
            holder.textView.setGravity(Gravity.LEFT);
            view.setPadding(0,0,90,0);
            params.gravity = Gravity.LEFT;
            holder.ll.setBackgroundResource(R.drawable.msg_sender_border);
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
            holder.name.setText(message.sender.name);
        }
        holder.textView.setLayoutParams(params);


        return view;
    }

    private class ViewHolder{
        public TextView textView,time,name;
        public LinearLayout ll;
    }
}