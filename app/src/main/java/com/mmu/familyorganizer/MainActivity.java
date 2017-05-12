package com.mmu.familyorganizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mmu.familyorganizer.Model.User;
import com.mmu.familyorganizer.Uitlity.GPSTracker;
import com.mmu.familyorganizer.Uitlity.Prefs;


public class MainActivity extends AppCompatActivity {

    EditText codeEt,numberEt,nameEt;
    ProgressDialog dialog;
    GPSTracker gpsTracker;
    Prefs prefs;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(this);
        gpsTracker =  new GPSTracker(this);

        if(prefs.getBoolean(Prefs.IS_LOGGED_ID)){
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }

        codeEt = (EditText) findViewById(R.id.code);
        numberEt = (EditText) findViewById(R.id.number);
        nameEt = (EditText) findViewById(R.id.name);

    }

    public void signup(View view){
       // loading();
        User user = new User();

        user.name = nameEt.getText().toString();
        user.phone = codeEt.getText().toString()+numberEt.getText().toString();

        if(gpsTracker.canGetLocation()) {
            user.lat = gpsTracker.getLatitude();
            user.lon = gpsTracker.getLongitude();
            startActivity(new Intent(this,Validation.class).putExtra("user",user));
            finish();
        }else{
            gpsTracker.showSettingsAlert();
           // stopLoading();
        }

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
