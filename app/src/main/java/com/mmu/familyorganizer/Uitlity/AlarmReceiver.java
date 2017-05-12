package com.mmu.familyorganizer.Uitlity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.mmu.familyorganizer.MeetingActivity;
import com.mmu.familyorganizer.R;



public class AlarmReceiver extends BroadcastReceiver {


    public static final int ID_SMALL_NOTIFICATION = 235;

    Context context;

    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub
        context = k1;


        Intent intent = new Intent(context,MeetingActivity.class);

        showSmallNotification("Event alert",k2.getStringExtra("event").toString(),intent);
    }

    public void showSmallNotification(String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.family_o).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.family_o)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.family_o))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }

}