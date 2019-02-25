package tech.pcreate.subscriptionkeeper.subscriptions;

import java.util.List;

import androidx.lifecycle.Observer;
import tech.pcreate.subscriptionkeeper.database.SubsDao;
import tech.pcreate.subscriptionkeeper.database.Subscription;

public class SubsPresenter implements SubsContract.Presenter {


    private final SubsDao subsDao;
    private final View mView;

    public SubsPresenter(SubsDao subsDao, View mView) {
        this.subsDao = subsDao;
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void addNewSub() {
        mView.showAddSub();
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void populateSubs() {
        subsDao.getAllSubscriptions().observeForever(new Observer<List<Subscription>>() {
            @Override
            public void onChanged(List<Subscription> subscriptionList) {
                mView.setSubs(subscriptionList);
                if (subscriptionList.size() < 1){
                    mView.showEmptyMessage();
                }
            }
        });
    }

    @Override
    public void openEditScreen(Subscription subscription) {
        mView.showEditScreen(subscription.id);
    }

    @Override
    public void openConfirmDeleteDialog(Subscription subscription) {
        mView.showDeleteConfirmDialog(subscription);
    }

    @Override
    public void delete(long subId) {
        Subscription subscription = subsDao.findSubscription(subId);
        subsDao.deleteSubscription(subscription);
    }

    @Override
    public void start() {

    }


}
