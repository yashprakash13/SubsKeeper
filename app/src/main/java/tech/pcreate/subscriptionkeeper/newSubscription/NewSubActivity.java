package tech.pcreate.subscriptionkeeper.newSubscription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import tech.pcreate.subscriptionkeeper.R;
import tech.pcreate.subscriptionkeeper.database.SubsDatabase;
import tech.pcreate.subscriptionkeeper.database.Subscription;
import tech.pcreate.subscriptionkeeper.reminders.ScheduleReminderAsync;
import tech.pcreate.subscriptionkeeper.utils.ActivityUtils;
import tech.pcreate.subscriptionkeeper.utils.AppConstants;
import tech.pcreate.subscriptionkeeper.utils.DateDialogFragment;
import tech.pcreate.subscriptionkeeper.utils.DateFormatter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class NewSubActivity extends AppCompatActivity implements
        NewSubContract.View,
        NewSubContract.DateListener{

    public static final String BILLING_DATE_PICKER = "BILLING_DATE_PICKER";
    private TextInputLayout mName, mCurr, mAmount;
    private SeekBar mRecurrMonths;
    private TextView mDispMonths, mBillingDate;

    private Subscription mSubscription;
    private boolean isEditing =  false;

    private NewSubContract.Presenter mPresenter;
    private int mSeekBarValue = 0;
    private Toolbar mToolbar;

    private boolean twoTimesBackPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sub);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivityUtils.setColors(this, true);

        mSubscription =  new Subscription();
        checkIfNewSubscription();
        setActionBarTitles();

        SubsDatabase subsDatabase = SubsDatabase.getDatabase(getApplicationContext());
        mPresenter = new NewSubPresenter(this, subsDatabase.subsDao());

        initializeFields();

    }

    private void scheduleReminders() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean reminderYes = sharedPreferences.getBoolean(getString(R.string.scheduler_key), true);
        if (reminderYes){
            new ScheduleReminderAsync(mSubscription, this).execute();
        }

    }

    private void setActionBarTitles() {
        if(!isEditing) getSupportActionBar().setTitle(getString(R.string.new_subscription));
        else getSupportActionBar().setTitle(getString(R.string.edit_subscription));
    }

    private void initializeFields() {
        mName = findViewById(R.id.subscriptionname);
        mCurr = findViewById(R.id.currencyofbilling);
        mAmount = findViewById(R.id.amountofbilling);
        mRecurrMonths = findViewById(R.id.recurringmonths);
        mDispMonths =  findViewById(R.id.recurringmonthsview);

        mBillingDate =  findViewById(R.id.dateofbilling);
        mBillingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.showDateDialog();
            }
        });

        mRecurrMonths.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSeekBarValue = i;
                mDispMonths.setText(String.valueOf(mSeekBarValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDispMonths.setText(String.valueOf(mSeekBarValue));
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            mSubscription.setName(mName.getEditText().getText().toString());
            mSubscription.setCurrency(mCurr.getEditText().getText().toString());
            mSubscription.setAmount(Float.parseFloat(mAmount.getEditText().getText().toString()));
            mSubscription.setRecurring(Integer.parseInt(mDispMonths.getText().toString()));

            boolean isValid = mPresenter.validate(mSubscription);
            if (isValid && twoTimesBackPressedOnce) {
                if (!isEditing) mPresenter.save(mSubscription);
                else mPresenter.update(mSubscription);
                scheduleReminders();
                twoTimesBackPressedOnce = false;
                finish();
            }
        }catch (Exception ignored){
        }

    }

    @Override
    public void onBackPressed() {
        if(twoTimesBackPressedOnce){
            super.onBackPressed();
            Toast.makeText(this, R.string.empty_sub_discarded, Toast.LENGTH_SHORT).show();
        }
        twoTimesBackPressedOnce = true;
        onPause();
        Snackbar.make(mName, getString(R.string.fill_all_fileds_to_save), Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                twoTimesBackPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isEditing){
            mPresenter.getSubAndPopulate(mSubscription.getId());
        }
    }

    private void checkIfNewSubscription() {
        if (getIntent().getExtras() != null) {
            mSubscription.setId((int) getIntent().getLongExtra(AppConstants.SUB_ID, 5));
            isEditing = true;
        }
    }

    @Override
    public void populate(Subscription subscription) {
        this.mSubscription = subscription;

        mName.getEditText().setText(mSubscription.getName());
        mCurr.getEditText().setText(mSubscription.getCurrency());
        mAmount.getEditText().setText(String.valueOf(mSubscription.getAmount()));
        mBillingDate.setText(String.format(getString(R.string.billing_date), DateFormatter.format(mSubscription.getBeginDate())));
        mDispMonths.setText(String.valueOf(mSubscription.getRecurring()));

        mRecurrMonths.setProgress(mSubscription.getRecurring());

    }

    @Override
    public void openDateDialog() {
        DateDialogFragment fragment = new DateDialogFragment();
        fragment.show(getSupportFragmentManager(), BILLING_DATE_PICKER);
    }

    @Override
    public void showErrorMessage(int field) {
        if (field == AppConstants.NAME) {
            mName.setError(getString(R.string.invalid_subs_name));
        } else if (field == AppConstants.CURRENCY) {
            mCurr.setError(getString(R.string.invalid_currency));
        } else if (field == AppConstants.AMOUNT) {
            mAmount.setError(getString(R.string.invalid_amount));
        } else if (field == AppConstants.BILLING_DATE) {
            mBillingDate.setError(getString(R.string.invalid_billing_date));
        } else if (field == AppConstants.RECURRING_MONTHS) {
            Snackbar.make(mRecurrMonths, getString(R.string.invalid_recurring_months), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void clearPreErrors() {
        mName.setErrorEnabled(false);
        mCurr.setErrorEnabled(false);
        mAmount.setErrorEnabled(false);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void setSelectedDate(Date date) {
        mSubscription.setBeginDate(date);
        mBillingDate.setText(DateFormatter.format(date));
    }

    @Override
    public void setPresenter(NewSubContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}