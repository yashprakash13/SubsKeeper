package tech.pcreate.subscriptionkeeper.backgroundJobs;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tech.pcreate.subscriptionkeeper.database.SubsDatabase;
import tech.pcreate.subscriptionkeeper.database.Subscription;
import tech.pcreate.subscriptionkeeper.newSubscription.NewSubActivity;
import tech.pcreate.subscriptionkeeper.reminders.ScheduleReminderAsync;
import tech.pcreate.subscriptionkeeper.utils.DateFormatter;

public class CheckForDueDateJobService extends JobService {

    private SubsDatabase mRoomDatabase;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mRoomDatabase = SubsDatabase.getDatabase(this);
        new CheckBillDateAsync(jobParameters, this).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class CheckBillDateAsync extends AsyncTask<Void, Void, Void> {

        Context mContext;
        JobParameters mJobParameters;
        public CheckBillDateAsync(JobParameters jobParameters, Context context) {
            this.mJobParameters = jobParameters;
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Subscription> subscriptionsList = mRoomDatabase.subsDao().getSubscriptions();
            for (Subscription subscription : subscriptionsList) {
                Calendar calendar  = Calendar.getInstance();
                Date curDate = calendar.getTime();
                Date billDate = subscription.getBeginDate();
                if(billDate.before(curDate)){
                    subscription.setBeginDate(DateFormatter.rawDueDate(billDate, subscription.getRecurring()));
                    mRoomDatabase.subsDao().updateSubscription(subscription);
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    scheduleReminder(subscription);
                }
            }
            jobFinished(mJobParameters, false);
            return null;
        }

        private void scheduleReminder(Subscription subscription) {
            new ScheduleReminderAsync(subscription, mContext).execute();
        }

    }
}
