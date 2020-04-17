package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;
import me.micrusa.app.amazwidgets.R;

public class SettingsActivity extends AppCompatActivity {

    private static me.micrusa.amaztimer.defValues defValues = new defValues();
    private me.micrusa.amaztimer.utils.utils utils = new utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            file file = new file(defValues.settingsFile, preference.getContext());
            String prefkey = preference.getKey();
            //Log.i("AmazTimer", "Changing '" + prefname + "' preference");
            if(prefkey.equals("batterySaving")){
                file.set(defValues.sBatterySaving, (Boolean) newValue);
            }else if(prefkey.equals("hrOn")){
                file.set(defValues.sHrSwitch, (Boolean) newValue);
            }else if(prefkey.equals("lang")){
                //Log.i("AmazTimer", "New lang is " + newValue.toString());
                file.set(defValues.sLang, newValue.toString());
            }
            return true;
        }
    };

    private static OnPreferenceClickListener OnPreferenceClickListener = new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String prefkey = preference.getKey();
            if(prefkey.equals("saved")){
                Intent intent = new Intent(preference.getContext(), PresetsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                preference.getContext().startActivity(intent);
            }
            return true;
        }
    };

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat batterySaving = findPreference("batterySaving");
            SwitchPreferenceCompat hrSwitch = findPreference("hrOn");
            batterySaving.setOnPreferenceChangeListener(onPreferenceChangeListener);
            hrSwitch.setOnPreferenceChangeListener(onPreferenceChangeListener);
            ListPreference lang = findPreference("lang");
            lang.setOnPreferenceChangeListener(onPreferenceChangeListener);
            Preference presets = findPreference("saved");
            presets.setVisible(false); //Presets invisible for now
            //presets.setOnPreferenceClickListener(OnPreferenceClickListener);
        }
    }
}