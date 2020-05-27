package me.micrusa.amaztimer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.util.Log;

public class UserInfo {
    int mGender;

    int mHeight;

    int mMonth;

    float mWeight;

    int mYear;

    public static UserInfo getUserInfo(Context paramContext) {
        ContentResolver contentResolver = paramContext.getContentResolver();
        Cursor cursor = null;
        paramContext = null;
        try {
            Cursor cursor1 = contentResolver.query(Uri.parse("content://com.huami.watch.setup.usersettings"), null, null, null, null);
            null = cursor1;
            cursor = cursor1;
            return new UserInfo(cursor1);
        } catch (Exception exception) {
            return null;
        } finally {
            if (exception != null)
                exception.close();
        }
    }

    public UserInfo(Cursor paramCursor) {
        if (paramCursor != null && paramCursor.moveToFirst()) {
            this.mGender = paramCursor.getInt(paramCursor.getColumnIndex("gender"));
            this.mYear = paramCursor.getInt(paramCursor.getColumnIndex("year"));
            this.mMonth = paramCursor.getInt(paramCursor.getColumnIndex("month"));
            this.mWeight = paramCursor.getFloat(paramCursor.getColumnIndex("weight"));
            this.mHeight = paramCursor.getInt(paramCursor.getColumnIndex("height"));
        }
    }

    public int getAage() {
        Log.d("HeartView", "mYear : " + this.mYear + "  Calendar.getInstance().get(Calendar.YEAR) :" + Calendar.getInstance().get(1));
        return Calendar.getInstance().get(1) - this.mYear;
    }
}

