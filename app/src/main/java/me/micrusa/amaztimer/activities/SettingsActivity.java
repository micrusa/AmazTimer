package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.SystemProperties;
import me.micrusa.amaztimer.utils.utils;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set language before creating preferences
        utils.setupPrefs(this);
        utils.setLang(this, Prefs.getString(defValues.KEY_LANG, "en"));
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    @SuppressWarnings({"UnnecessaryCallToStringValueOf"})
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @SuppressWarnings({"ConstantConditions", "UnnecessaryCallToStringValueOf"})
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat repsMode = findPreference(defValues.KEY_REPSMODE);
            SwitchPreferenceCompat workoutMode = findPreference(defValues.KEY_WORKOUT);
            SwitchPreferenceCompat enableSound = findPreference(defValues.KEY_SOUND);
            repsMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            workoutMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            //enableSound only visible for verge
            enableSound.setVisible(SystemProperties.isVerge());
            Preference presets = findPreference(defValues.KEY_SAVED);
            Preference latestTrain = findPreference(defValues.KEY_LATESTTRAIN);
            Preference appInfo = findPreference(defValues.KEY_APPINFO);
            presets.setOnPreferenceClickListener(OnPreferenceClickListener);
            latestTrain.setOnPreferenceClickListener(OnPreferenceClickListener);
            appInfo.setOnPreferenceClickListener(OnPreferenceClickListener);

            setupListPreferences(true);
            setModesVisibility();
        }

        private final OnPreferenceChangeListener onPreferenceChangeListener = (preference, newValue) -> {
            Resources res = preference.getContext().getResources();
            setModesVisibility();
            setupListPreferences(false);
            return true;
        };

        private final OnPreferenceClickListener OnPreferenceClickListener = preference -> {
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

        private void setModesVisibility(){
            SwitchPreferenceCompat repsMode = findPreference(defValues.KEY_REPSMODE);
            SwitchPreferenceCompat workoutMode = findPreference(defValues.KEY_WORKOUT);
            if (Prefs.getBoolean(defValues.KEY_WORKOUT, false))
                repsMode.setEnabled(false);
            else if (Prefs.getBoolean(defValues.KEY_REPSMODE, false))
                workoutMode.setEnabled(false);
        }

        private void setupListPreferences(boolean isFirstRun){
            ListPreference age = findPreference(defValues.KEY_AGE);
            ListPreference weight = findPreference(defValues.KEY_WEIGHT);
            int currYear = defValues.CURRENT_YEAR;
            age.setSummary(String.valueOf(currYear - Integer.parseInt(Prefs.getString(defValues.KEY_AGE, "2000")))
                    + " " + getResources().getString(R.string.ageyo));
            weight.setSummary(String.valueOf(Prefs.getString(defValues.KEY_WEIGHT, "70")) + "Kg");
            
            if(isFirstRun){
            age.setOnPreferenceChangeListener(onPreferenceChangeListener);
            weight.setOnPreferenceChangeListener(onPreferenceChangeListener);
            
            String[] weightsEntry = new String[120];
            String[] weightsValue = new String[120];
            for (int i = 30; i < 150; i++) {
                weightsEntry[i - 30] = String.valueOf(i + 1) + "Kg";
                weightsValue[i - 30] = String.valueOf(i + 1);
            }
            weight.setEntries(weightsEntry);
            weight.setEntryValues(weightsValue);
            
            String[] ages = new String[100];
            int startYear = defValues.CURRENT_YEAR - 100;
            int endYear = defValues.CURRENT_YEAR;
            for (int i = startYear; i < endYear; i++) ages[i - startYear] = String.valueOf(i);
            age.setEntries(ages);
            age.setEntryValues(ages);
            }
        }
    }
}