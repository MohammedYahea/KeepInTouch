package com.mmu.familyorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mmu.familyorganizer.Model.Group;
import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.Uitlity.Prefs;
import com.mmu.familyorganizer.adapter.GroupListAdapter;

import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity {


    ListView listView;
    DatabaseReference db;
    public ArrayList<Group> groups ;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        getSupportActionBar().setTitle("Groups");

        listView = (ListView) findViewById(R.id.listview);

        prefs = new Prefs(this);

        db = FirebaseDatabase.getInstance().getReference();

        Query query = db.child("Groups");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groups = new ArrayList<>();

                if(dataSnapshot!=null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Group group = snapshot.getValue(Group.class);
                        group.key = snapshot.getKey();
                        ArrayList<User> users = group.users;
                        Log.d("Group an",snapshot.toString());
                        for (int i = 0; i < users.size(); i++) {
                            User user = users.get(i);
                            if (prefs.getString(Prefs.KEY).equals(user.key)) {
                                groups.add(group);
                                break;
                            }
                        }
                    }

                    GroupListAdapter adapter = new GroupListAdapter(GroupListActivity.this, groups);
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(GroupListActivity.this,GroupMessageActivity.class).putExtra("group",groups.get(i)));
            }
        });
    }

    public void createGroup(View view){
        startActivity(new Intent(this,CreateGroupActivity.class));
    }
}
