package com.mmu.familyorganizer.Uitlity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by Mohd.Ali on 12/1/16.
 */
public class MyAlerts<T> {

    Context context;
    Class<T> tclass;
    public Activity activity;

    public MyAlerts(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    public void showAlertBox(String msg, final Intent intent) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(intent);
                activity.finish();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.create().show();

    }

    public void showAlertBox(String msg, Class<T> tClass) {
        this.tclass = tClass;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, tclass));
                activity.finish();

            }
        });

        builder.create().show();

    }

    public void showAlertBox(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.create().show();

    }
}
