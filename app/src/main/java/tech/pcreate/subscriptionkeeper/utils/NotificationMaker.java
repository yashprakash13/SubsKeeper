package tech.pcreate.subscriptionkeeper.utils;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import tech.pcreate.subscriptionkeeper.R;
import tech.pcreate.subscriptionkeeper.database.Subscription;
import tech.pcreate.subscriptionkeeper.newSubscription.NewSubActivity;

import static android.content.Context.NOTIFICATION_SERVICE;
import static tech.pcreate.subscriptionkeeper.utils.AppConstants.PRIMARY_CHANNEL_ID;

public class NotificationMaker  {

    private static NotificationManager mNotifyManager;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static Notification getNotification(String title, String text, Context context, Subscription subscription){
        mContext = context;
        createNotificationChannel();

        Intent intent = new Intent(mContext, NewSubActivity.class);
        intent.putExtra(AppConstants.SUB_ID, (long) subscription.getId());
        intent.setAction(Long.toString(System.currentTimeMillis())); //Default Action

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                AppConstants.OPEN_SUBSCRIPTION_FROM_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (mContext, PRIMARY_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setColor(mContext.getResources().getColor(R.color.colorPinker))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        return builder.build();

    }

    private static void createNotificationChannel() {

        mNotifyManager =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                            PRIMARY_CHANNEL_ID,
                            "Reminder notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription("Notifications from Alarm Manager");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }


}
