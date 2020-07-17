package me.micrusa.amaztimer.trainings.database;

import com.dbflow5.config.DBFlowDatabase;
import com.dbflow5.config.FlowManager;
import com.dbflow5.database.DatabaseWrapper;

import org.jetbrains.annotations.NotNull;

@com.dbflow5.annotation.Database(name = Database.NAME, version = Database.VERSION)
public class Database extends DBFlowDatabase {

    public static final String NAME = "AmazTimer";

    public static final int VERSION = 1;

    public static DatabaseWrapper getDb(){
        return FlowManager.getDatabase(Database.class);
    }

    @NotNull
    @Override
    public Class<?> getAssociatedDatabaseClassFile() {
        return null;
    }

    @Override
    public int getDatabaseVersion() {
        return VERSION;
    }

    @Override
    public boolean isForeignKeysSupported() {
        return false;
    }

    @Override
    public boolean areConsistencyChecksEnabled() {
        return false;
    }

    @Override
    public boolean backupEnabled() {
        return false;
    }
}
