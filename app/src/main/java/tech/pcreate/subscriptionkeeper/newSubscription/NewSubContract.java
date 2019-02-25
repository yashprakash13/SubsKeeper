package tech.pcreate.subscriptionkeeper.newSubscription;

import java.util.Date;

import tech.pcreate.subscriptionkeeper.BaseViewPresenter.BasePresenter;
import tech.pcreate.subscriptionkeeper.BaseViewPresenter.BaseView;
import tech.pcreate.subscriptionkeeper.database.Subscription;

public interface NewSubContract {

    interface Presenter extends BasePresenter{

        void save(Subscription subscription);

        boolean validate(Subscription subscription);

        void update(Subscription subscription);

        void showDateDialog();

        void getSubAndPopulate(long id);

    }

    interface View extends BaseView<Presenter>{

        void populate(Subscription subscription);

        void openDateDialog();

        void showErrorMessage(int messageId);

        void clearPreErrors();

        void close();

    }

    interface DateListener {

        void setSelectedDate(Date date);

    }


}
