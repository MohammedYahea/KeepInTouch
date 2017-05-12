package com.mmu.familyorganizer;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mmu.familyorganizer.Model.Message;
import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.Uitlity.Prefs;
import com.mmu.familyorganizer.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class MessageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ListView listView;
    EditText msgEt;
    ImageButton sendBtn;
    Button btn;
    public DatabaseReference db;
    Prefs prefs;
    ArrayList<Message> messages;
    public String token;
    User user;
    LocationManager lm;
    Marker marker;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        db = database.getReference();
        prefs = new Prefs(this);


        listView = (ListView) findViewById(R.id.list);
        sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        btn = (Button) findViewById(R.id.btn);
        msgEt = (EditText) findViewById(R.id.text);

        user = (User) getIntent().getSerializableExtra("user");
        getSupportActionBar().setTitle(user.name);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg(msgEt.getText().toString());
            }
        });

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                db = database.getReference();
                User user = new User();
                user.name = prefs.getString(Prefs.NAME);
                user.key = prefs.getString(Prefs.KEY);
                user.phone = prefs.getString(Prefs.MOBILE);
                user.lat = location.getLatitude();
                user.lon = location.getLongitude();
                db.child("users").child(user.key).setValue(user);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapFragment.isVisible()){

                }
            }
        });

        getMessages();
        setLocation();
    }

    public void setLocation(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        db = database.getReference();
        Query query = db.child("users").child(user.key);

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    moveLocation(new LatLng(user.lat,user.lon));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMsg(String msg) {

        Message message = new Message();
        message.msg = msg;
        Calendar calendar = Calendar.getInstance();
        message.time = calendar.get(Calendar.MONTH) +"-"+ calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE);

        message.senderKey = prefs.getString(Prefs.KEY);
        message.receiverKey = user.key;


        db.child("Messages").child(db.child("Messages").push().getKey()).setValue(message);

        db.child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    msgEt.setText("");
                    listView.setSelection(messages.size()-1);
//                    sendNotification(token, projects.key);

                } else {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getMessages() {

        Query query = db.orderByChild("Messages");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = false;
                if (dataSnapshot != null) {

                    messages = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.child("Messages").getChildren()) {
                        Message message = snapshot.getValue(Message.class);

                        if ((message.receiverKey.equals(user.key) && message.senderKey.equals(prefs.getString(Prefs.KEY))) || (message.receiverKey.equals(prefs.getString(Prefs.KEY)) && message.senderKey.equals(user.key))) {
                            message.name = user.name;
                            messages.add(message);
                        }
                    }

                    MessageAdapter adapter = new MessageAdapter(MessageActivity.this, messages);
                    listView.setAdapter(adapter);

                    listView.setSelection(messages.size() - 1);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        user = (User) getIntent().getSerializableExtra("user");
        setMarker(user);
    }

    public void setMarker(User user){

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(user.lat, user.lon);
        marker = mMap.addMarker(new MarkerOptions().position(sydney).title(user.name));
        marker.setPosition(sydney);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f));
    }

    public void moveLocation(LatLng latLng){
        marker.setPosition(latLng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handlers = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handlers.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handlers.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

}
