package tech.pcreate.subscriptionkeeper.reminders;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import tech.pcreate.subscriptionkeeper.database.Subscription;
import tech.pcreate.subscriptionkeeper.utils.AppConstants;
import tech.pcreate.subscriptionkeeper.utils.NotificationMaker;
import tech.pcreate.subscriptionkeeper.utils.ReminderTimeHelper;

public class ScheduleReminderAsync extends AsyncTask<Void, Void, Void> {

    private Subscription mSubscription;
    private Context mContext;

    public ScheduleReminderAsync(Subscription subscription, Context context) {
        this.mSubscription = subscription;
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String notTitle = mSubscription.getName() + " Reminder";
        String text = "Your " + mSubscription.getName() + " Subscription is due tomorrow. Tap to view the details.";

        Notification notification = NotificationMaker.getNotification(notTitle, text, mContext, mSubscription);
        Intent intent = new Intent(mContext, ReminderNotification.class);
        intent.putExtra(AppConstants.REMINDER_NOT_ID, 101);
        intent.putExtra(AppConstants.REMINDER_NOT_NOT, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        long reminderTime = ReminderTimeHelper.getReminderTIme(mSubscription.getBeginDate());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);

        return null;
    }
}