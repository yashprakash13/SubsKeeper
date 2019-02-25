package tech.pcreate.subscriptionkeeper.subscriptions;

import java.util.List;

import tech.pcreate.subscriptionkeeper.BaseViewPresenter.BasePresenter;
import tech.pcreate.subscriptionkeeper.BaseViewPresenter.BaseView;
import tech.pcreate.subscriptionkeeper.database.Subscription;

public interface SubsContract {

    interface Presenter extends BasePresenter {

        void addNewSub();

        void result(int requestCode, int resultCode);

        void populateSubs();

        void openEditScreen(Subscription subscription);

        void openConfirmDeleteDialog(Subscription subscription);

        void delete(long subId);


        interface View extends BaseView<Presenter> {

            void showAddSub();

            void setSubs(List<Subscription> subscriptionList);

            void showEditScreen(long id);

            void showDeleteConfirmDialog(Subscription subscription);

            void showEmptyMessage();
        }

        interface OnItemClickListener {

            void clickItem(Subscription subscription);

            void clickLongItem(Subscription subscription);
        }

        interface DeleteListener {

            void setConfirm(boolean confirm, long personId);

        }
    }
}
