package tech.pcreate.subscriptionkeeper.subscriptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import tech.pcreate.subscriptionkeeper.R;
import tech.pcreate.subscriptionkeeper.backgroundJobs.CheckForDueDateJobService;
import tech.pcreate.subscriptionkeeper.database.SubsDatabase;
import tech.pcreate.subscriptionkeeper.database.Subscription;
import tech.pcreate.subscriptionkeeper.expenses.MyExpensesActivity;
import tech.pcreate.subscriptionkeeper.newSubscription.NewSubActivity;
import tech.pcreate.subscriptionkeeper.settings.SettingsActivity;
import tech.pcreate.subscriptionkeeper.utils.AppConstants;
import tech.pcreate.subscriptionkeeper.utils.DeleteConfirmFragment;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import static tech.pcreate.subscriptionkeeper.utils.ActivityUtils.setColors;

public class SubsListActivity extends AppCompatActivity implements
        SubsContract.Presenter.View,
        SubsContract.Presenter.OnItemClickListener,
        SubsContract.Presenter.DeleteListener{

    private SubsContract.Presenter presenter;
    private SubsAdapter adapter;

    private TextView mEmptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subslist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.your_subscriptions));

        setColors(this, false);

        SubsDatabase subsDatabase = SubsDatabase.getDatabase(getApplicationContext());
        presenter = new SubsPresenter(subsDatabase.subsDao(), this);

        initializeViews();

        scheduleDueDateCheckingJob();

    }

    private void scheduleDueDateCheckingJob() {
        JobInfo.Builder builder = new JobInfo.Builder(AppConstants.DUE_DATE_CHECK_JOB_ID,
                new ComponentName(this, CheckForDueDateJobService.class));
        builder.setPersisted(true);
        builder.setRequiresCharging(false);
        builder.setPeriodic(AppConstants.SIX_HOURS_IN_MILLISECONDS);

        JobInfo jobInfo = builder.build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobInfo);

    }

    private void initializeViews() {
        RecyclerView recyclerView = findViewById(R.id.subslist);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL));
        adapter = new SubsAdapter(this);
        recyclerView.setAdapter(adapter);

        mEmptyTextView = findViewById(R.id.emptyView);

        SubsDatabase subsDatabase = SubsDatabase.getDatabase(getApplicationContext());
        presenter = new SubsPresenter(subsDatabase.subsDao(), this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subs_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.new_note){
            newSubscription();
        }else if (id == R.id.settings_btn){
            startActivity(new Intent(this, SettingsActivity.class));
        }else if(id == R.id.total_exp_btn){
            startActivity(new Intent(this, MyExpensesActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.populateSubs();
    }

    private void newSubscription() {
        presenter.addNewSub();
    }

    @Override
    public void showAddSub() {
        startActivity(new Intent(this, NewSubActivity.class));
    }

    @Override
    public void setSubs(List<Subscription> subscriptionList) {
        adapter.setSubscriptionsList(subscriptionList);
        mEmptyTextView.setVisibility(View.GONE);
    }

    @Override
    public void showEditScreen(long id) {
        Intent intent = new Intent(this, NewSubActivity.class);
        intent.putExtra(AppConstants.SUB_ID, id);
        startActivity(intent);
    }

    @Override
    public void showDeleteConfirmDialog(Subscription subscription) {
        DeleteConfirmFragment fragment = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.SUB_ID, subscription.getId());
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "deleteConfirmation");
    }

    @Override
    public void showEmptyMessage() {
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void clickItem(Subscription subscription) {
        presenter.openEditScreen(subscription);
    }

    @Override
    public void clickLongItem(Subscription subscription) {
        presenter.openConfirmDeleteDialog(subscription);
    }

    @Override
    public void setConfirm(boolean confirm, long subId) {
        if (confirm){
            presenter.delete(subId);
        }
    }

    @Override
    public void setPresenter(SubsContract.Presenter presenter) {
        this.presenter = presenter;
    }

}