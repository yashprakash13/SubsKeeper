package tech.pcreate.subscriptionkeeper.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

import androidx.core.content.ContextCompat;
import tech.pcreate.subscriptionkeeper.R;

public class ActivityUtils {

    public static void openKeyboard(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 500);
    }

    public static void hideKeyboard(Activity activity) {

        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();

        if (currentFocusedView != null) {
            Objects.requireNonNull(inputManager).hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void setColors(Activity activity, boolean light) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (!light){
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            window.setNavigationBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }else{
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPinker));
            window.setNavigationBarColor(ContextCompat.getColor(activity, R.color.colorPinker));
        }
    }

}
