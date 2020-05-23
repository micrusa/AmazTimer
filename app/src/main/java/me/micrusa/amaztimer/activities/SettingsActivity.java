package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;

public class SettingsActivity extends AppCompatActivity {

    private static final me.micrusa.amaztimer.defValues defValues = new defValues();
    private final me.micrusa.amaztimer.utils.utils utils = new utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        //Set language before creating preferences
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
    }

    private static final OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            file file = new file(defValues.settingsFile, preference.getContext());
            file bodyFile = new file(defValues.bodyFile, preference.getContext());
            String key = preference.getKey();
            if (defValues.KEY_BATTERYSAVING.equals(key)) {
                file.set(defValues.sBatterySaving, (Boolean) newValue);
            } else if (defValues.KEY_HRTOGGLE.equals(key)) {
                file.set(defValues.sHrSwitch, (Boolean) newValue);
            } else if (defValues.KEY_LANG.equals(key)) {
                file.set(defValues.sLang, newValue.toString());
            } else if (defValues.KEY_GENDER.equals(key)) {
                bodyFile.set(defValues.sMale, Boolean.parseBoolean(newValue.toString()));
            } else if (defValues.KEY_AGE.equals(key)) {
                bodyFile.set(defValues.sAge, Integer.parseInt((String) newValue));
            } else if (defValues.KEY_WEIGHT.equals(key)) {
                bodyFile.set(defValues.sWeight, Integer.parseInt((String) newValue));
            } else if (defValues.KEY_LONGPREPARE.equals(key)) {
                file.set(defValues.sLongPrepare, (Boolean) newValue);
            } else if (defValues.KEY_REPSMODE.equals(key)) {
                file.set(defValues.sRepsMode, (Boolean) newValue);
            }
            return true;
        }
    };

    private static final OnPreferenceClickListener OnPreferenceClickListener = new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (defValues.KEY_SAVED.equals(key)) {
                Intent intent = new Intent(preference.getContext(), PresetsActivity.class);
                preference.getContext().startActivity(intent);
            } else if (defValues.KEY_LATESTTRAIN.equals(key)) {
                Intent intent = new Intent(preference.getContext(), LatestTrainActivity.class);
                preference.getContext().startActivity(intent);
            } else if (defValues.KEY_APPINFO.equals(key)) {
                Intent intent = new Intent(preference.getContext(), AppInfo.class);
                preference.getContext().startActivity(intent);
            }
            return true;
        }
    };

    @SuppressWarnings("WeakerAccess")
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @SuppressWarnings("ConstantConditions")
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat batterySaving = findPreference(defValues.KEY_BATTERYSAVING);
            SwitchPreferenceCompat hrSwitch = findPreference(defValues.KEY_HRTOGGLE);
            SwitchPreferenceCompat longPrepare = findPreference(defValues.KEY_LONGPREPARE);
            SwitchPreferenceCompat repsMode = findPreference(defValues.KEY_REPSMODE);
            batterySaving.setOnPreferenceChangeListener(onPreferenceChangeListener);
            hrSwitch.setOnPreferenceChangeListener(onPreferenceChangeListener);
            longPrepare.setOnPreferenceChangeListener(onPreferenceChangeListener);
            repsMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            ListPreference lang = findPreference(defValues.KEY_LANG);
            ListPreference gender = findPreference(defValues.KEY_GENDER);
            lang.setOnPreferenceChangeListener(onPreferenceChangeListener);
            gender.setOnPreferenceChangeListener(onPreferenceChangeListener);
            Preference presets = findPreference(defValues.KEY_SAVED);
            Preference latestTrain = findPreference(defValues.KEY_LATESTTRAIN);
            Preference appInfo = findPreference(defValues.KEY_APPINFO);
            presets.setOnPreferenceClickListener(OnPreferenceClickListener);
            latestTrain.setOnPreferenceClickListener(OnPreferenceClickListener);
            appInfo.setOnPreferenceClickListener(OnPreferenceClickListener);
            ListPreference age = findPreference(defValues.KEY_AGE);
            ListPreference weight = findPreference(defValues.KEY_WEIGHT);
            String[] ages = new String[100];
            for(int i=0; i<100; i++){
                ages[i] = String.valueOf(i + 1);
            }
            age.setEntries(ages);
            age.setEntryValues(ages);
            String[] weightsEntry = new String[120];
            String[] weightsValue = new String[120];
            for(int i=30; i<150; i++){
                weightsEntry[i - 30] = String.valueOf(i + 1) + "Kg";
                weightsValue[i - 30] = String.valueOf(i + 1);
            }
            weight.setEntries(weightsEntry);
            weight.setEntryValues(weightsValue);
            age.setOnPreferenceChangeListener(onPreferenceChangeListener);
            weight.setOnPreferenceChangeListener(onPreferenceChangeListener);
        }
    }
}