package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
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
            file bodyFile = new file(defValues.bodyFile, preference.getContext());
            String prefkey = preference.getKey();
            switch (prefkey) {
                case "batterySaving":
                    file.set(defValues.sBatterySaving, (Boolean) newValue);
                    break;
                case "hrOn":
                    file.set(defValues.sHrSwitch, (Boolean) newValue);
                    break;
                case "lang":
                    file.set(defValues.sLang, newValue.toString());
                    break;
                case "gender":
                    bodyFile.set(defValues.sMale, (Boolean) newValue);
                    break;
                case "age":
                    bodyFile.set(defValues.sAge, (int) newValue);
                    break;
                case "weight":
                    bodyFile.set(defValues.sWeight, (int) newValue);
                    break;
            }
            return true;
        }
    };

    private static OnPreferenceClickListener OnPreferenceClickListener = new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String prefkey = preference.getKey();
            if (prefkey.equals("saved")) {
                Intent intent = new Intent(preference.getContext(), PresetsActivity.class);
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
            assert batterySaving != null;
            batterySaving.setOnPreferenceChangeListener(onPreferenceChangeListener);
            assert hrSwitch != null;
            hrSwitch.setOnPreferenceChangeListener(onPreferenceChangeListener);
            ListPreference lang = findPreference("lang");
            ListPreference gender = findPreference("gender");
            assert lang != null;
            lang.setOnPreferenceChangeListener(onPreferenceChangeListener);
            assert gender != null;
            gender.setOnPreferenceChangeListener(onPreferenceChangeListener);
            Preference presets = findPreference("saved");
            assert presets != null;
            presets.setOnPreferenceClickListener(OnPreferenceClickListener);
            SeekBarPreference ageBar = (SeekBarPreference) findPreference("age");
            SeekBarPreference weightBar = (SeekBarPreference) findPreference("weight");
            assert ageBar != null;
            ageBar.setOnPreferenceChangeListener(onPreferenceChangeListener);
            assert weightBar != null;
            weightBar.setOnPreferenceChangeListener(onPreferenceChangeListener);
        }
    }
}