package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

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
            switch (preference.getKey()) {
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
                    bodyFile.set(defValues.sMale, Boolean.parseBoolean(newValue.toString()));
                    break;
                case "age":
                    bodyFile.set(defValues.sAge, (int) newValue);
                    preference.setSummary(newValue.toString());
                    break;
                case "weight":
                    bodyFile.set(defValues.sWeight, (int) newValue);
                    preference.setSummary(newValue.toString() + "Kg");
                    break;
                case "huamiactivity":
                    file.set(defValues.sLongPrepare, (Boolean) newValue);
                    break;
            }
            return true;
        }
    };

    private static OnPreferenceClickListener OnPreferenceClickListener = new OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "saved": {
                    Intent intent = new Intent(preference.getContext(), PresetsActivity.class);
                    preference.getContext().startActivity(intent);
                    break;
                }
                case "latesttrain": {
                    Intent intent = new Intent(preference.getContext(), LatestTrainActivity.class);
                    preference.getContext().startActivity(intent);
                    break;
                }
                case "openwithbtn":
                    if(me.micrusa.amaztimer.utils.utils.isRooted()){
                        me.micrusa.amaztimer.utils.utils.getSecureSettingsPerm(preference.getContext());
                    }
                    if(!me.micrusa.amaztimer.utils.utils.checkSecureSettingsPerm(preference.getContext())){
                        Toast.makeText(preference.getContext(), "No permission", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Settings.Secure.putString(preference.getContext().getContentResolver(), "long_key_settings", "'#Intent;launchFlags=0x10000000;component=me.micrusa.amaztimer/me.micrusa.amaztimer.AmazTimer;end'");
                    break;
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
            SwitchPreferenceCompat longPrepare = findPreference("huamiactivity");
            assert batterySaving != null;
            batterySaving.setOnPreferenceChangeListener(onPreferenceChangeListener);
            assert hrSwitch != null;
            hrSwitch.setOnPreferenceChangeListener(onPreferenceChangeListener);
            assert longPrepare != null;
            longPrepare.setOnPreferenceChangeListener(onPreferenceChangeListener);
            ListPreference lang = findPreference("lang");
            ListPreference gender = findPreference("gender");
            assert lang != null;
            lang.setOnPreferenceChangeListener(onPreferenceChangeListener);
            assert gender != null;
            gender.setOnPreferenceChangeListener(onPreferenceChangeListener);
            Preference presets = findPreference("saved");
            Preference latestTrain = findPreference("latesttrain");
            Preference openWithBtn = findPreference("openwithbtn");
            assert presets != null;
            presets.setOnPreferenceClickListener(OnPreferenceClickListener);
            assert latestTrain != null;
            latestTrain.setOnPreferenceClickListener(OnPreferenceClickListener);
            assert openWithBtn != null;
            openWithBtn.setOnPreferenceClickListener(OnPreferenceClickListener);
            SeekBarPreference ageBar = (SeekBarPreference) findPreference("age");
            SeekBarPreference weightBar = (SeekBarPreference) findPreference("weight");
            assert ageBar != null;
            ageBar.setOnPreferenceChangeListener(onPreferenceChangeListener);
            assert weightBar != null;
            weightBar.setOnPreferenceChangeListener(onPreferenceChangeListener);
        }
    }
}