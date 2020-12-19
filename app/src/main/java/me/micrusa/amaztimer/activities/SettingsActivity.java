/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.saveworkout.ui.SavedWorkoutsActivity;
import me.micrusa.amaztimer.utils.Utils;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setupLang(this);
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
            SwitchPreferenceCompat repsMode = findPreference(Constants.KEY_REPSMODE);
            SwitchPreferenceCompat workoutMode = findPreference(Constants.KEY_WORKOUT);
            SwitchPreferenceCompat repsCounterMode = findPreference(Constants.KEY_REPSCOUNT);
            //SwitchPreferenceCompat enableSound = findPreference(Constants.KEY_SOUND);
            repsMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            workoutMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            repsCounterMode.setOnPreferenceChangeListener(onPreferenceChangeListener);
            //enableSound only visible for verge
            //enableSound.setVisible(SystemProperties.isVerge());
            Preference presets = findPreference(Constants.KEY_SAVED);
            Preference latestTrain = findPreference(Constants.KEY_WORKOUTVIEWER);
            Preference appInfo = findPreference(Constants.KEY_APPINFO);
            presets.setOnPreferenceClickListener(OnPreferenceClickListener);
            latestTrain.setOnPreferenceClickListener(OnPreferenceClickListener);
            appInfo.setOnPreferenceClickListener(OnPreferenceClickListener);

            setupListPreferences(true);
            setModesVisibility();
        }

        private final OnPreferenceChangeListener onPreferenceChangeListener = (preference, newValue) -> {
            setModesVisibility();
            setupListPreferences(false);
            return true;
        };

        private final OnPreferenceClickListener OnPreferenceClickListener = preference -> {
            String key = preference.getKey();
            switch (key) {
                case Constants.KEY_WORKOUTVIEWER: {
                    Intent intent = new Intent(preference.getContext(), SavedWorkoutsActivity.class);
                    preference.getContext().startActivity(intent);
                    break;
                }
                case Constants.KEY_APPINFO: {
                    Intent intent = new Intent(preference.getContext(), AppInfo.class);
                    preference.getContext().startActivity(intent);
                    break;
                }
            }
            return true;
        };

        private void setModesVisibility(){
            SwitchPreferenceCompat repsMode = findPreference(Constants.KEY_REPSMODE);
            SwitchPreferenceCompat workoutMode = findPreference(Constants.KEY_WORKOUT);
            SwitchPreferenceCompat repsCounterMode = findPreference(Constants.KEY_REPSCOUNT);
            boolean workout = workoutMode.isChecked();
            boolean reps = repsMode.isChecked();
            boolean repsCounter = repsCounterMode.isChecked();

            repsMode.setEnabled(!(workout || repsCounter));
            workoutMode.setEnabled(!(reps || repsCounter));
            repsCounterMode.setEnabled(!(workout || reps));

            //HR modes
            SwitchPreferenceCompat hrEnabled = findPreference(Constants.KEY_HRTOGGLE);
            SwitchPreferenceCompat hrOnStart = findPreference(Constants.KEY_HRONSTART);
            SwitchPreferenceCompat experimentalHr = findPreference(Constants.KEY_HREXPERIMENT);

            hrOnStart.setEnabled(hrEnabled.isChecked());
            experimentalHr.setEnabled(hrEnabled.isChecked());
        }

        private void setupListPreferences(boolean isFirstRun){
            ListPreference age = findPreference(Constants.KEY_AGE);
            ListPreference weight = findPreference(Constants.KEY_WEIGHT);
            int currYear = Constants.CURRENT_YEAR;
            age.setSummary(String.valueOf(currYear - Integer.parseInt(Prefs.getString(Constants.KEY_AGE, "2000")))
                    + " " + getResources().getString(R.string.ageyo));
            weight.setSummary(String.valueOf(Prefs.getString(Constants.KEY_WEIGHT, "70")) + "Kg");
            
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
            int startYear = Constants.CURRENT_YEAR - 100;
            int endYear = Constants.CURRENT_YEAR;
            for (int i = startYear; i < endYear; i++) ages[i - startYear] = String.valueOf(i);
            age.setEntries(ages);
            age.setEntryValues(ages);
            }
        }
    }
}