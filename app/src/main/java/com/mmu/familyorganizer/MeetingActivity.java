package com.mmu.familyorganizer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmu.familyorganizer.Model.Group;
import com.mmu.familyorganizer.Model.Meeting;
import com.mmu.familyorganizer.Model.Products;
import com.mmu.familyorganizer.Uitlity.AlarmReceiver;
import com.mmu.familyorganizer.Uitlity.MyAlerts;
import com.mmu.familyorganizer.adapter.EventListAdapter;
import com.mmu.familyorganizer.gson.GsonRequest;
import com.mmu.familyorganizer.gson.RequestTag;
import com.mmu.familyorganizer.gson.VolleyHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MeetingActivity extends AppCompatActivity {

    ListView listView;
    Button addBtn;
    public DatabaseReference db;
    ArrayList<Meeting> alarmlist;
    EventListAdapter adapter;
    Group projects;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        listView = (ListView) findViewById(R.id.list);
        addBtn = (Button) findViewById(R.id.addMeeting);

        alarmlist = new ArrayList<>();
        projects = (Group) getIntent().getSerializableExtra("group");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePicker();
            }
        });

        adapter = new EventListAdapter(this, alarmlist);
        listView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        db = database.getReference();
        getMeetings(projects.key);
    }

    public void dateTimePicker() {

        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                EditText event = (EditText) dialogView.findViewById(R.id.eventName);
                CheckBox remind = (CheckBox) dialogView.findViewById(R.id.remind);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                if(remind.isChecked())
                    calendar.add(Calendar.MINUTE,-5);

                long time = calendar.getTimeInMillis();
                date = calendar.getTime();
                setAlarm(calendar,event.getText().toString());
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void setAlarm(Calendar targetCal,String eventName) {

        Meeting event = new Meeting();
        event.eventName = eventName;
        event.time = targetCal.getTime()+"";

        alarmlist.add( event);
        adapter.notifyDataSetChanged();

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("event",eventName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

        saveMeetingTime(targetCal.getTimeInMillis(),eventName);
    }

    public void saveMeetingTime(final long timeInMillis,String eventName){
        Meeting meeting = new Meeting();
        meeting.time = timeInMillis+"";
        meeting.groupKey = projects.key;
        meeting.eventName = eventName;

        db.child("Events").child(db.child("Events").push().getKey()).setValue(meeting);

        db.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {


                    for(int i=0;i<projects.users.size();i++)
                    sendNotification(projects.users.get(i).token,dataSnapshot.getKey(),timeInMillis);
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    public void getMeetings(String key) {

        db.child("Events").child("groupKey").equalTo(key);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = false;
                if (dataSnapshot != null) {

                    Calendar calendar = Calendar.getInstance();
                    alarmlist = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("Events").getChildren()) {
                        Meeting meeting = snapshot.getValue(Meeting.class);

                        if(meeting.groupKey.equals(projects.key)) {
                            calendar.setTimeInMillis(Long.parseLong(meeting.time));
                            meeting.time = String.valueOf(calendar.getTime());
                            alarmlist.add(meeting);
                        }
                    }
                    adapter = new EventListAdapter(MeetingActivity.this, alarmlist);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendNotification(String token, String key,long time) {

        HashMap<String, String> params = new HashMap<>();
        params.put("device_token", token);
        params.put("service_key", time+"");
        params.put("title", "Meeting");
        params.put("msg", date+"");
        GsonRequest gsonRequest = new GsonRequest(RequestTag.URL, Products.class, params, null, new Response.Listener<Products>() {
            @Override
            public void onResponse(Products response) {
                MyAlerts myAlerts = new MyAlerts(MeetingActivity.this);
                myAlerts.showAlertBox("Event announced.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleyHelper.getInstance(this).addToRequestQueue(gsonRequest);
    }




}
