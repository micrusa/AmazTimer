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
import me.micrusa.amaztimer.utils.SystemProperties;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        //Set language before creating preferences
        utils.setLang(this, new file(defValues.SETTINGS_FILE).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
    }

    private static final OnPreferenceChangeListener onPreferenceChangeListener = (preference, newValue) -> {
        file file = new file(defValues.SETTINGS_FILE);
        file bodyFile = new file(defValues.BODY_FILE);

        SwitchPreferenceCompat repsMode = preference.getPreferenceManager().findPreference(defValues.KEY_REPSMODE);
        SwitchPreferenceCompat workoutMode = preference.getPreferenceManager().findPreference(defValues.KEY_WORKOUT);
        SwitchPreferenceCompat chronoMode = preference.getPreferenceManager().findPreference(defValues.KEY_CHRONO);

        String key = preference.getKey();
        switch (key) {
            case defValues.KEY_BATTERYSAVING:
                file.set(defValues.SETTINGS_BATTERYSAVING, (Boolean) newValue);
                break;
            case defValues.KEY_HRTOGGLE:
                file.set(defValues.SETTINGS_HRSWITCH, (Boolean) newValue);
                break;
            case defValues.KEY_LANG:
                file.set(defValues.SETTINGS_LANG, newValue.toString());
                break;
            case defValues.KEY_GENDER:
                bodyFile.set(defValues.SETTINGS_MALE, Boolean.parseBoolean(newValue.toString()));
                break;
            case defValues.KEY_AGE:
                int age = defValues.CURRENT_YEAR - Integer.parseInt((String) newValue);
                bodyFile.set(defValues.SETTINGS_AGE, age);
                preference.setSummary(String.valueOf(age) + " " + preference.getContext().getResources().getString(R.string.ageyo));
                break;
            case defValues.KEY_WEIGHT:
                bodyFile.set(defValues.SETTINGS_WEIGHT, Integer.parseInt((String) newValue));
                preference.setSummary(newValue + "Kg");
                break;
            case defValues.KEY_SOUND:
                file.set(defValues.SETTINGS_SOUND, (Boolean) newValue);
                break;
            case defValues.KEY_LONGPREPARE:
                file.set(defValues.SETTINGS_LONGPREPARE, (Boolean) newValue);
                break;
            case defValues.KEY_REPSMODE:
                file.set(defValues.SETTINGS_REPSMODE, (Boolean) newValue);
                if ((Boolean) newValue) {
                    chronoMode.setEnabled(false);
                    workoutMode.setEnabled(false);
                } else {
                    chronoMode.setEnabled(true);
                    workoutMode.setEnabled(true);
                }
                break;
            case defValues.KEY_WORKOUT:
                file.set(defValues.SETTINGS_WORKOUTMODE, (Boolean) newValue);
                if ((Boolean) newValue) {
                    chronoMode.setEnabled(false);
                    repsMode.setEnabled(false);
                } else {
                    chronoMode.setEnabled(true);
                    repsMode.setEnabled(true);
                }
                break;
            case defValues.KEY_ENABLEPREPARE:
                file.set(defValues.SETTINGS_ENABLEPREPARE, (Boolean) newValue);
                if ((Boolean) newValue)
                    preference.getPreferenceManager().findPreference(defValues.KEY_LONGPREPARE).setEnabled(true);
                else
                    preference.getPreferenceManager().findPreference(defValues.KEY_LONGPREPARE).setEnabled(false);
                break;
            case defValues.KEY_CHRONO:
                file.set(defValues.SETTINGS_CHRONOMODE, (Boolean) newValue);
                if ((Boolean) newValue) {
                    workoutMode.setEnabled(false);
                    repsMode.setEnabled(false);
                } else {
                    workoutMode.setEnabled(true);
                    repsMode.setEnabled(true);
                }
                break;
            case defValues.KEY_TCX:
                file.set(defValues.SETTINGS_TCX, (Boolean) newValue);
                break;
        }
        return true;
    };

    private static final OnPreferenceClickListener OnPreferenceClickListener = preference -> {
        String key = preference.getKey();
        switch (key) {
            case defValues.KEY_SAVED: {
                Intent intent = new Intent(preference.getContext(), PresetsActivity.class);
                preference.getContext().startActivity(intent);
                break;
            }
            case defValues.KEY_LATESTTRAIN: {
                Intent intent = new Intent(preference.getContext(), LatestTrainActivity.class);
                preference.getContext().startActivity(intent);
                break;
            }
            case defValues.KEY_APPINFO: {
                Intent intent = new Intent(preference.getContext(), AppInfo.class);
                preference.getContext().startActivity(intent);
                break;
            }
        }
        return true;
    };

    @SuppressWarnings({"UnnecessaryCallToStringValueOf"})
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @SuppressWarnings({"ConstantConditions", "UnnecessaryCallToStringValueOf"})
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat batterySaving = findPreference(defValues.KEY_BATTERYSAVING);
            SwitchPreferenceCompat hrSwitch = findPreference(defValues.KEY_HRTOGGLE);
            SwitchPreferenceCompat longPrepare = findPreference(defValues.KEY_LONGPREPARE);
            SwitchPreferenceCompat repsMode = findPreference(defValues.KEY_REPSMODE);
            SwitchPreferenceCompat workoutMode = findPreference(defValues.KEY_WORKOUT);
            SwitchPreferenceCompat chronoMode = findPreference(defValues.KEY_CHRONO);
            SwitchPreferenceCompat enablePrepare = findPreference(defValues.KEY_ENABLEPREPARE);
            SwitchPreferenceCompat enableTcx = findPreference(defValues.KEY_TCX);
            SwitchPreferenceCompat enableSound = findPreference(defValues.KEY_SOUND);
            batterySaving.setOnPreferenceChangeListener(onPreferenceChangeListener);
            hrSwitch.setOnPreferenceChangeListener(onPreferenceChangeListener);
            longPrepare.setOnPreferenceChangeListener(onPreferenceChangeListener);
            repsMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            workoutMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            chronoMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            enablePrepare.setOnPreferenceChangeListener(onPreferenceChangeListener);
            enableTcx.setOnPreferenceChangeListener(onPreferenceChangeListener);
            //enableSound only visible for verge
            enableSound.setOnPreferenceChangeListener(onPreferenceChangeListener);
            enableSound.setVisible(SystemProperties.isVerge());
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
            int startYear = defValues.CURRENT_YEAR - 100;
            int endYear = defValues.CURRENT_YEAR;
            for(int i=startYear; i<endYear; i++){
                ages[i - startYear] = String.valueOf(i);
            }
            age.setEntries(ages);
            age.setEntryValues(ages);
            if (age.getValue() != null)
                age.setSummary(String.valueOf(endYear - Integer.parseInt(age.getValue()) + " " + getResources().getString(R.string.ageyo)));
            else
                age.setSummary(String.valueOf(defValues.DEFAULT_AGE + " " + getResources().getString(R.string.ageyo)));
            String[] weightsEntry = new String[120];
            String[] weightsValue = new String[120];
            for(int i=30; i<150; i++){
                weightsEntry[i - 30] = String.valueOf(i + 1) + "Kg";
                weightsValue[i - 30] = String.valueOf(i + 1);
            }
            weight.setEntries(weightsEntry);
            weight.setEntryValues(weightsValue);
            if (weight.getValue() != null)
                weight.setSummary(weight.getValue() + weight.getSummary().toString());
            else
                weight.setSummary(defValues.DEFAULT_WEIGHT + weight.getSummary().toString());
            age.setOnPreferenceChangeListener(onPreferenceChangeListener);
            weight.setOnPreferenceChangeListener(onPreferenceChangeListener);
            file settingsFile = new file(defValues.SETTINGS_FILE);
            //Disable longPrepare if prepare timer is off
            if (settingsFile.get(defValues.SETTINGS_ENABLEPREPARE, defValues.DEFAULT_ENABLEPREPARE))
                longPrepare.setEnabled(true);
            else
                longPrepare.setEnabled(false);

            if (settingsFile.get(defValues.SETTINGS_WORKOUTMODE, defValues.DEFAULT_WORKOUTMODE)){
                chronoMode.setEnabled(false);
                repsMode.setEnabled(false);
            } else if (settingsFile.get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
                chronoMode.setEnabled(false);
                workoutMode.setEnabled(false);
            } else if (settingsFile.get(defValues.SETTINGS_CHRONOMODE, defValues.DEFAULT_CHRONOMODE)){
                workoutMode.setEnabled(false);
                repsMode.setEnabled(false);
            }
        }
    }
}