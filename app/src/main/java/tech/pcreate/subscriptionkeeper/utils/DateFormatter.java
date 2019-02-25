package tech.pcreate.subscriptionkeeper.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    public static String format(Date date) {
        if (date == null) {
            return " ";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static String dueDate(Date date, int recurring){
        if(date == null) {
            return " ";
        }
        else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, recurring);
            Date dueDate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            return sdf.format(dueDate);
        }
    }

    public static Date rawDueDate(Date date, int recurring){
        if(date == null) {
            return null;
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, recurring);
            return calendar.getTime();
        }
    }

}
