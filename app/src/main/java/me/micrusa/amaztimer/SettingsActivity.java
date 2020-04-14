package me.micrusa.amaztimer;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import me.micrusa.app.amazwidgets.R;

public class SettingsActivity extends AppCompatActivity {

    private static defValues defValues = new defValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static OnPreferenceChangeListener batterySavingListener = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.i("AmazTimer", "Changing batterySaving preference");
            file file = new file(defValues.settingsFile, preference.getContext());
            file.set(defValues.sBattSvg, (Boolean) newValue);
            return true;
        }
    };

    private static OnPreferenceChangeListener hrSwitchListener = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.i("AmazTimer", "Changing hrSwitch preference");
            file file = new file(defValues.settingsFile, preference.getContext());
            file.set(defValues.hrSwitch, (Boolean) newValue);
            return true;
        }
    };

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat batterySaving = findPreference("batterySaving");
            SwitchPreferenceCompat hrSwitch = findPreference("hrOn");
            batterySaving.setOnPreferenceChangeListener(batterySavingListener);
            hrSwitch.setOnPreferenceChangeListener(hrSwitchListener);
        }
    }
}