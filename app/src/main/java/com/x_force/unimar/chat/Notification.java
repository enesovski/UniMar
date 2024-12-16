package com.x_force.unimar.chat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.x_force.unimar.MainActivity;
import com.x_force.unimar.R;

public class Notification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        getMessageNotification(getApplicationContext(),message.getNotification().getTitle(),message.getNotification().getBody(),intent);

    }
    private void getMessageNotification(Context context, String title, String message, Intent intent){
        NotificationManager NotificationManeger=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId=1;
        String channelId="Channel1";
        String channelName="My Channel";

        int important=NotificationManeger.IMPORTANCE_HIGH;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel myChannel=new NotificationChannel(
                    channelId,channelName,important
            );
            NotificationManeger.createNotificationChannel(myChannel);
        }
        NotificationCompat.Builder myBuilder=new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message);

        PendingIntent intent1=PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_MUTABLE);
        myBuilder.setContentIntent(intent1);
        NotificationManeger.notify(notificationId,myBuilder.build());
    }
}
