package me.micrusa.amaztimer.trainings.database;

import com.dbflow5.config.DBFlowDatabase;
import com.dbflow5.config.FlowManager;
import com.dbflow5.database.DatabaseWrapper;

@com.dbflow5.annotation.Database(name = Database.NAME, version = Database.VERSION)
public class Database {

    public static final String NAME = "AmazTimer";

    public static final int VERSION = 1;

    public static DatabaseWrapper getDb(){
        return FlowManager.getDatabase(NAME);
    }

}
