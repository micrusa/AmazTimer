/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.micrusa.amaztimer.database;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import me.micrusa.amaztimer.AmazTimerApplication;

public class DBUtils {
    public static AmazTimerDB createInstance(){
        return Room.databaseBuilder(AmazTimerApplication.getContext(),
                AmazTimerDB.class, DBConstants.DB_NAME)
                .addMigrations(new SQLMigration(1, 2, new String[]{"CREATE TABLE IF NOT EXISTS `Timer` (`timeAdded` INTEGER NOT NULL, `name` TEXT, `sets` INTEGER NOT NULL, `work` INTEGER NOT NULL, `rest` INTEGER NOT NULL, `mode` INTEGER NOT NULL, `heartrate` INTEGER NOT NULL, `gps` INTEGER NOT NULL, PRIMARY KEY(`timeAdded`))"}))
                .build();
    }

    public static class SQLMigration extends Migration {
        private String[] SQL;
        public SQLMigration(int start, int end, String[] SQL){
            super(start, end);
            this.SQL = SQL;
        }
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            for(String str : SQL) database.execSQL(str);
        }
    }
}
