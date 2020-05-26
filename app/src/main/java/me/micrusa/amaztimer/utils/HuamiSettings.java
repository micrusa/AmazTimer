package me.micrusa.amaztimer.utils;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;

public class HuamiSettings {
    private static final String TAG = "UserSettings-Watch";

    public static String get(ContentResolver paramContentResolver, String paramString) {
        Cursor cursor3 = null;
        ContentResolver contentResolver = null;
        Cursor cursor2 = null;
        Cursor cursor1 = null;
        try {
            String str1 = null;
            Cursor cursor = paramContentResolver.query(SettingsEntry.CONTENT_URI, SettingsEntry.COLUMNS_VALUE, "key=?", new String[] { paramString }, null);
            paramContentResolver = contentResolver;
            if (cursor != null) {
                paramContentResolver = contentResolver;
                cursor1 = cursor;
                cursor2 = cursor;
                if (cursor.moveToFirst()) {
                    cursor1 = cursor;
                    cursor2 = cursor;
                    str1 = cursor.getString(0);
                }
            }
            String str2 = str1;
            if (cursor != null) {
                cursor.close();
                str2 = str1;
            }
            return str2;
        } catch (Exception exception) {
            cursor2 = cursor1;
            exception.printStackTrace();
            cursor2 = cursor3;
            if (cursor1 != null) {
                cursor1.close();
                cursor2 = cursor3;
            }
            return (String) cursor2;
        } finally {
            if (cursor2 != null)
                cursor2.close();
        }
    }

    public static class SettingsEntry {
        public static final String[] COLUMNS_ALL;

        public static final String[] COLUMNS_EMPTY = new String[0];

        public static final String[] COLUMNS_KEY_VALUE;

        public static final String[] COLUMNS_VALUE = new String[] { "value" };

        public static final String COLUMN_CLOUD_SYNC_STATE = "cloud_sync_state";

        public static final String COLUMN_KEY = "key";

        public static final String COLUMN_VALUE = "value";

        public static final String COLUMN_WATCH_SYNC_STATE = "watch_sync_state";

        public static final Uri CONTENT_URI = Uri.parse("content://com.huami.watch.companion.settings");

        public static final int SYNC_STATE_NEED_SYNC = 0;

        public static final int SYNC_STATE_SYNCED = 1;

        static {
            COLUMNS_KEY_VALUE = new String[] { "key", "value" };
            COLUMNS_ALL = new String[] { "key", "value", "cloud_sync_state", "watch_sync_state" };
        }
    }
}