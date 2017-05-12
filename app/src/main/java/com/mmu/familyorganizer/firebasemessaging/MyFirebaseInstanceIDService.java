package com.mmu.familyorganizer.firebasemessaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mmu.familyorganizer.Uitlity.Prefs;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);


//        Toast.makeText(this, refreshedToken, Toast.LENGTH_LONG).show();
        //calling the method store token and passing token
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {

        Prefs pref;
        pref = new Prefs(this);
        pref.setshareprefdatastring(TAG_TOKEN,token);
        //we will save the token in sharedpreferences later
//        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(TAG_TOKEN, token);
//        editor.apply();

    }
}