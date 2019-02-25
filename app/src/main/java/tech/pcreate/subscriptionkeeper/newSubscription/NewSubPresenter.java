package tech.pcreate.subscriptionkeeper.newSubscription;

import android.util.Log;

import tech.pcreate.subscriptionkeeper.database.SubsDao;
import tech.pcreate.subscriptionkeeper.database.Subscription;
import tech.pcreate.subscriptionkeeper.reminders.ScheduleReminderAsync;
import tech.pcreate.subscriptionkeeper.utils.AppConstants;

public class NewSubPresenter implements NewSubContract.Presenter {

    private final NewSubContract.View mView;
    private final SubsDao subsDao;

    public NewSubPresenter(NewSubContract.View mView, SubsDao subsDao) {
        this.mView = mView;
        this.mView.setPresenter(this);
        this.subsDao = subsDao;
    }

    @Override
    public void save(Subscription subscription) {

        long id  = this.subsDao.insertSubscription(subscription);
        mView.close();
    }

    @Override
    public boolean validate(Subscription subscription) {
        mView.clearPreErrors();
        if(subscription.name.isEmpty()){
            mView.showErrorMessage(AppConstants.NAME);
            return false;
        }

        if(subscription.amount == 0) {
            mView.showErrorMessage(AppConstants.AMOUNT);
            return false;
        }

        if(subscription.currency.isEmpty()){
            mView.showErrorMessage(AppConstants.CURRENCY);
            return false;
        }

        if(subscription.beginDate == null){
            mView.showErrorMessage(AppConstants.BILLING_DATE);
            return false;
        }

        if(subscription.recurring == 0){
            mView.showErrorMessage(AppConstants.RECURRING_MONTHS);
            return false;
        }

        return true;
    }

    @Override
    public void update(Subscription subscription) {

        int id  = this.subsDao.updateSubscription(subscription);
        mView.close();
    }

    @Override
    public void showDateDialog() {

        mView.openDateDialog();

    }

    @Override
    public void getSubAndPopulate(long id) {

        Subscription subscription = subsDao.findSubscription(id);
        if(subscription != null )  mView.populate(subscription);
    }

    @Override
    public void start() {

    }
}
