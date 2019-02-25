package tech.pcreate.subscriptionkeeper.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tech.pcreate.subscriptionkeeper.utils.AppConstants;

public class ReminderNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(AppConstants.REMINDER_NOT_NOT);
        int id = intent.getIntExtra(AppConstants.REMINDER_NOT_ID, 101);

        notificationManager.notify(id, notification);
    }
}
