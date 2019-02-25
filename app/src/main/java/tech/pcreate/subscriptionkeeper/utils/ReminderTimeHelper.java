package tech.pcreate.subscriptionkeeper.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class ReminderTimeHelper {

    public static long getReminderTIme (Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR, 9);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 3);

        return calendar.getTimeInMillis();
    }
}