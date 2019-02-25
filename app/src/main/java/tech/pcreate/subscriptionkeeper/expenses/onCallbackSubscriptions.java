package tech.pcreate.subscriptionkeeper.expenses;

import java.util.List;

import tech.pcreate.subscriptionkeeper.database.Subscription;

public interface onCallbackSubscriptions{

    void getAllSubscriptions(List<Subscription> subscriptionList);
}