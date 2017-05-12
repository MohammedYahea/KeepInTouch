package com.mmu.familyorganizer.Uitlity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mohd.Ali on 11/26/16.
 */
public class Prefs {

    public static String IS_LOGGED_ID = "IS_LOGGED_IN";
    public static String USER_ID = "USER_ID";
    public static String BALANCE = "BALANCE";
    public static String M_BALANCE = "M_BALANCE";
    public static String CHECKED = "CHECKED";
    public static String MOBILE = "MOBILE";
    public static String PASSWORD = "PASSWORD";
    public static String NAME = "NAME";
    public static String IMAGE = "IMAGE";
    public static String TYPE = "TYPE";
    public static String ACTIVE = "ACTIVE";
    public static String KEY = "KEY";
    public static final String TAG_TOKEN = "tagtoken";

    SharedPreferences prefs;

    public Prefs(Context context) {
        prefs = context.getSharedPreferences("MyPrefs",context.MODE_PRIVATE);
    }

    public void setBoolean(String KEY, boolean value){
        prefs.edit().putBoolean(KEY,value).commit();
    }

    public void setString(String KEY, String value){
        prefs.edit().putString(KEY,value).commit();
    }

    public void setInteger(String KEY, int value){
        prefs.edit().putInt(KEY,value).commit();
    }

    public boolean getBoolean(String KEY){
        return prefs.getBoolean(KEY,false);
    }

    public String getString(String KEY){
        return prefs.getString(KEY,"");
    }

    public int getInteger(String KEY){
        return prefs.getInt(KEY,0);
    }

    public void setshareprefdatastring(String KEY, String values){

        prefs.edit().putString(KEY,values).commit();
    }
}
