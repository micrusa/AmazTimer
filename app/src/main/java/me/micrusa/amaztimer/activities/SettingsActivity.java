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
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
    }

    private static final OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {
        @SuppressWarnings("UnnecessaryCallToStringValueOf")
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            file file = new file(defValues.SETTINGS_FILE, preference.getContext());
            file bodyFile = new file(defValues.BODY_FILE, preference.getContext());

            SwitchPreferenceCompat repsMode = preference.getPreferenceManager().findPreference(defValues.KEY_REPSMODE);
            SwitchPreferenceCompat workoutMode = preference.getPreferenceManager().findPreference(defValues.KEY_WORKOUT);
            SwitchPreferenceCompat chronoMode = preference.getPreferenceManager().findPreference(defValues.KEY_CHRONO);

            String key = preference.getKey();
            if (defValues.KEY_BATTERYSAVING.equals(key)) {
                file.set(defValues.SETTINGS_BATTERYSAVING, (Boolean) newValue);
            } else if (defValues.KEY_HRTOGGLE.equals(key)) {
                file.set(defValues.SETTINGS_HRSWITCH, (Boolean) newValue);
            } else if (defValues.KEY_LANG.equals(key)) {
                file.set(defValues.SETTINGS_LANG, newValue.toString());
            } else if (defValues.KEY_GENDER.equals(key)) {
                bodyFile.set(defValues.SETTINGS_MALE, Boolean.parseBoolean(newValue.toString()));
            } else if (defValues.KEY_AGE.equals(key)) {
                int age = defValues.CURRENT_YEAR - Integer.parseInt((String) newValue);
                bodyFile.set(defValues.SETTINGS_AGE, age);
                preference.setSummary(String.valueOf(age) + " " + preference.getContext().getResources().getString(R.string.ageyo));
            } else if (defValues.KEY_WEIGHT.equals(key)) {
                bodyFile.set(defValues.SETTINGS_WEIGHT, Integer.parseInt((String) newValue));
            } else if (defValues.KEY_LONGPREPARE.equals(key)) {
                file.set(defValues.SETTINGS_LONGPREPARE, (Boolean) newValue);
            } else if (defValues.KEY_REPSMODE.equals(key)) {
                file.set(defValues.SETTINGS_REPSMODE, (Boolean) newValue);
                if ((Boolean) newValue){
                    chronoMode.setEnabled(false);
                    workoutMode.setEnabled(false);
                } else {
                    chronoMode.setEnabled(true);
                    workoutMode.setEnabled(true);
                }
            } else if (defValues.KEY_WORKOUT.equals(key)) {
                file.set(defValues.SETTINGS_WORKOUTMODE, (Boolean) newValue);
                if ((Boolean) newValue){
                    chronoMode.setEnabled(false);
                    repsMode.setEnabled(false);
                } else {
                    chronoMode.setEnabled(true);
                    repsMode.setEnabled(true);
                }
            } else if (defValues.KEY_ENABLEPREPARE.equals(key)){
                file.set(defValues.SETTINGS_ENABLEPREPARE, (Boolean) newValue);
                if((Boolean) newValue)
                    preference.getPreferenceManager().findPreference(defValues.KEY_LONGPREPARE).setEnabled(true);
                else
                    preference.getPreferenceManager().findPreference(defValues.KEY_LONGPREPARE).setEnabled(false);
            } else if (defValues.KEY_CHRONO.equals(key)){
                file.set(defValues.SETTINGS_CHRONOMODE, (Boolean) newValue);
                if ((Boolean) newValue){
                    workoutMode.setEnabled(false);
                    repsMode.setEnabled(false);
                } else {
                    workoutMode.setEnabled(true);
                    repsMode.setEnabled(true);
                }
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
            batterySaving.setOnPreferenceChangeListener(onPreferenceChangeListener);
            hrSwitch.setOnPreferenceChangeListener(onPreferenceChangeListener);
            longPrepare.setOnPreferenceChangeListener(onPreferenceChangeListener);
            repsMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            workoutMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            chronoMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            enablePrepare.setOnPreferenceChangeListener(onPreferenceChangeListener);
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
            age.setSummary(String.valueOf(endYear - Integer.parseInt(age.getValue()) + " " + getResources().getString(R.string.ageyo)));
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
            file settingsFile = new file(defValues.SETTINGS_FILE, getContext());
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