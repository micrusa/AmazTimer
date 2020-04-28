package me.micrusa.amaztimer.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Vibrator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.micrusa.amaztimer.defValues;

public class utils {

    private me.micrusa.amaztimer.defValues defValues = new defValues();

    public void vibrate(int time, Context Context) {
        Vibrator v = (Vibrator) Context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    public String formatTime(int seconds) {
        //Format seconds to mm:ss
        SimpleDateFormat df = new SimpleDateFormat(defValues.timeFormat, Locale.getDefault());
        return df.format(new Date(seconds * 1000));
    }

    public void setLang(Context context, String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public void pushToFile(file f, int sets, int work, int rest) {
        f.set(defValues.sSets, sets);
        f.set(defValues.sWork, work);
        f.set(defValues.sRest, rest);
    }

    public static boolean isRooted() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public static void getSecureSettingsPerm(Context context) {
        if(checkSecureSettingsPerm(context)){
            return;
        }
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        try {
            os.writeBytes("pm grant "+context.getPackageName()+" android.permission.WRITE_SECURE_SETTINGS \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.writeBytes("exit\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkSecureSettingsPerm(Context context)
    {
        int res = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


}
