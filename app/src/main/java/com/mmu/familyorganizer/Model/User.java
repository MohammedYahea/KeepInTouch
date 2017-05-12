package com.mmu.familyorganizer.Model;

import java.io.Serializable;

/**
 * Created by Mohd.Ali on 12/19/16.
 */

public class User implements Serializable{

    public String name;
    public String phone;
    public String key;
    public String token;
    public double lat,lon;

    public User() {
    }


    public User(String name,String phone) {
        this.name = name;
        this.phone = phone;
    }
}
