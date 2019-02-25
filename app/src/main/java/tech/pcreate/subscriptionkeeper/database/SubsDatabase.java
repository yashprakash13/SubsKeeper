package tech.pcreate.subscriptionkeeper.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Subscription.class}, version = 1, exportSchema = false)
public abstract class SubsDatabase extends RoomDatabase {

    public static SubsDatabase INSTANCE;
    public abstract SubsDao subsDao();

    public static SubsDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SubsDatabase.class, "subs")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static SubsDatabase getMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), SubsDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
