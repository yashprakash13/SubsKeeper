package tech.pcreate.subscriptionkeeper.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao
public interface SubsDao {

    @Insert(onConflict = IGNORE)
    long insertSubscription(Subscription subscription);

    @Delete
    void deleteSubscription(Subscription subscription);

    @Update
    int updateSubscription (Subscription subscription);

    @Update
    int updateSubscription(List<Subscription> subscriptionList);

    @Query("Delete from subs")
    void deleteAll();

    @Query("Select * from subs order by name asc")
    LiveData<List<Subscription>> getAllSubscriptions();

    @Query("Select * from subs where id = :id")
    Subscription findSubscription(long id);

    @Query("Select * from subs")
    List<Subscription> getSubscriptions();

    @Query("SELECT count(id) FROM subs")
    int getDataCount();
}
