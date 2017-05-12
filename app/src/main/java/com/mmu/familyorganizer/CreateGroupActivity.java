package com.mmu.familyorganizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmu.familyorganizer.Model.Group;
import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.Uitlity.GPSTracker;
import com.mmu.familyorganizer.Uitlity.MyAlerts;
import com.mmu.familyorganizer.Uitlity.Prefs;
import com.mmu.familyorganizer.adapter.CreateGroupAdapter;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference db;
    ArrayList<User> users ;
    public static ArrayList<User> selectedUsers = new ArrayList<>();
    Prefs prefs;
    GPSTracker gpsTracker;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        listView = (ListView) findViewById(R.id.listview);
        prefs = new Prefs(this);



        db = FirebaseDatabase.getInstance().getReference();

        db.orderByChild("users");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("users").getChildren()) {
                    User user = snapshot.getValue(User.class);
                    user.key = snapshot.getKey();

                    if (!prefs.getString(Prefs.KEY).equals(user.key))
                        users.add(user);
                    else{
                        selectedUsers.add(0,user);
                    }
                }

                CreateGroupAdapter adapter = new CreateGroupAdapter(CreateGroupActivity.this, users);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createGroup(View view){
        Group group = new Group();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        key = db.child("Groups").push().getKey();
        group.key = key;
        group.users = selectedUsers;
        group.groupName= groupName();

        db.child("Groups").child(key).setValue(group);

        db.child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    MyAlerts myAlerts = new MyAlerts(CreateGroupActivity.this);
                    myAlerts.showAlertBox("Group Created. ", GroupListActivity.class);

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                MyAlerts myAlerts = new MyAlerts(CreateGroupActivity.this);
                myAlerts.showAlertBox("Something went wrong.");
            }
        });
    }

    public String groupName(){
        String name = "";

        for(int i=0;i<selectedUsers.size();i++){
            name +=  selectedUsers.get(i).name+", ";
        }

        return name;
    }
}
