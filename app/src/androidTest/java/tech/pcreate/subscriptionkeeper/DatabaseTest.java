package tech.pcreate.subscriptionkeeper;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import tech.pcreate.subscriptionkeeper.database.SubsDao;
import tech.pcreate.subscriptionkeeper.database.SubsDatabase;
import tech.pcreate.subscriptionkeeper.database.Subscription;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private SubsDatabase subsDatabase;
    private SubsDao subsDao;

    @Before
    public void createDb(){
        Context context= ApplicationProvider.getApplicationContext();
        subsDatabase = Room.inMemoryDatabaseBuilder(context, SubsDatabase.class).build();
        subsDao = subsDatabase.subsDao();
    }

    @After
    public void closeDb() throws IOException {
        subsDatabase.close();
    }

    @Test
    public void read_Write_Test(){
        subsDatabase.subsDao().deleteAll();
        List<Subscription> list  = subsDao.getSubscriptions();
        assertThat(list.size(), equalTo(0));

        Subscription subscription = new Subscription();
        subscription.setName("XYZ");
        subscription.setCurrency("YTS");
        subscription.setBeginDate(Calendar.getInstance().getTime());
        subscription.setAmount(650);
        subscription.setRecurring(2);

        subsDao.insertSubscription(subscription);
        List<Subscription> list1  = subsDao.getSubscriptions();
        assert(subscription.equals(list1.get(0)));

    }

}
