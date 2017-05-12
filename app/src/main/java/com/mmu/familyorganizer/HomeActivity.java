package com.mmu.familyorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.Uitlity.GPSTracker;
import com.mmu.familyorganizer.Uitlity.Prefs;
import com.mmu.familyorganizer.adapter.UserListAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference db;
    ArrayList<User> users ;
    Prefs prefs;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Contacts");

        listView = (ListView) findViewById(R.id.listview);
        prefs = new Prefs(this);
        gpsTracker = new GPSTracker(this);

        db = FirebaseDatabase.getInstance().getReference();

        db.orderByChild("users");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null) {
                    users = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("users").getChildren()) {
                        User user = snapshot.getValue(User.class);
                        user.key = snapshot.getKey();
                        Log.d("name", user.name);
                        if (!prefs.getString(Prefs.KEY).equals(user.key))
                            users.add(user);
                    }

                    UserListAdapter adapter = new UserListAdapter(HomeActivity.this, users);
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        if(gpsTracker.canGetLocation()){
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            db = database.getReference();
            Query query = db.child("users").child(prefs.getString(Prefs.KEY));

            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        User user = dataSnapshot.getValue(User.class);
                        user.lat = gpsTracker.getLatitude();
                        user.lon = gpsTracker.getLongitude();
                        //Toast.makeText(getApplicationContext(),prefs.getString(Prefs.KEY)+user.name+" location",Toast.LENGTH_SHORT).show();
                        db.child("users").child(prefs.getString(Prefs.KEY)).setValue(user);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    public void group(View view){
        startActivity(new Intent(this,GroupListActivity.class));
    }

}
