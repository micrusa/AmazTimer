package me.micrusa.amaztimer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.util.Log;

import me.micrusa.amaztimer.defValues;

public class UserInfo {

    private me.micrusa.amaztimer.defValues defValues = new defValues();
    private Context context;

    int mGender;
    int mHeight;
    int mMonth;
    float mWeight;
    int mYear;

    public UserInfo(Context paramContext) {
        context = paramContext;
        ContentResolver contentResolver = paramContext.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(Uri.parse("content://com.huami.watch.setup.usersettings"), null, null, null, null);
        } catch (Exception exception) {
            Log.e("AmazTimer: UserInfo", "Exception while getting UserInfo: " + exception.getMessage());
        }
        if (cursor != null && cursor.moveToFirst()) {
            this.mGender = cursor.getInt(cursor.getColumnIndex("gender"));
            this.mYear = cursor.getInt(cursor.getColumnIndex("year"));
            this.mMonth = cursor.getInt(cursor.getColumnIndex("month"));
            this.mWeight = cursor.getFloat(cursor.getColumnIndex("weight"));
            this.mHeight = cursor.getInt(cursor.getColumnIndex("height"));
        }
    }

    public int getAge() {
        Log.d("HeartView", "mYear : " + this.mYear + "  defValues.CURRENT_YEAR :" + defValues.CURRENT_YEAR);
        return defValues.CURRENT_YEAR - this.mYear;
    }

    public int getGender(){
        return this.mGender;
    }

    public float getWeight(){
        return this.mWeight;
    }

    public int getHeight(){
        return this.mHeight;
    }

    public void logEverything(){
        Log.d("AmazTimer", "logEverything: GENDER: " + this.mGender
                + " YEAR: " + this.mYear
                + " MONTH: " + this.mMonth
                + " WEIGHT: " + this.mWeight
                + " HEIGHT: " + this.mHeight);
    }
}

