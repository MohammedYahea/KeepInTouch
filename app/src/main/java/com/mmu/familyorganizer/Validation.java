package com.mmu.familyorganizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.Uitlity.MyAlerts;
import com.mmu.familyorganizer.Uitlity.Prefs;

import java.util.Random;

public class Validation extends AppCompatActivity {

    EditText code;
    Button submit;
    Prefs prefs;
    String key;
    int codeValue;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        prefs = new Prefs(this);

        code = (EditText) findViewById(R.id.code);
        submit = (Button) findViewById(R.id.submit);

        sendSMS();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codes = code.getText().toString();
                if (codes.equals(codeValue + "")) {
                    loading();
                    uploadData((User) getIntent().getSerializableExtra("user"));
                }
            }
        });
    }

    public void sendSMS() {

        codeValue = randInt(1000, 9999);

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "Your validation code : " + codeValue);
        sendIntent.setType("vnd.android-dir/mms-sms");
     //   startActivity(sendIntent);
        Toast.makeText(getApplicationContext(), codeValue + "", Toast.LENGTH_SHORT).show();
    }

    public void uploadData(final User user) {


        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        key = db.child("users").push().getKey();
        user.key = key;
        user.token = prefs.getString(Prefs.TAG_TOKEN);
        db.child("users").child(key).setValue(user);

        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    prefs.setString(Prefs.NAME, user.name);
                    prefs.setString(Prefs.MOBILE, user.phone);
                    prefs.setString(Prefs.KEY, key);
                    prefs.setBoolean(Prefs.IS_LOGGED_ID, true);


                    MyAlerts myAlerts = new MyAlerts(Validation.this);
                    myAlerts.showAlertBox("Successfully registered. ", HomeActivity.class);

                } else {

                }
                stopLoading();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static int randInt(int min, int max) {

        Random rand = new Random();


        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void loading() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
    }

    public void stopLoading() {
        dialog.dismiss();
    }


}
