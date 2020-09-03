package me.micrusa.amaztimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.activities.PrepareActivity;
import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.button.buttonEvent;
import me.micrusa.amaztimer.button.buttonListener;
import me.micrusa.amaztimer.utils.SystemProperties;
import me.micrusa.amaztimer.utils.heartrate.hrSensor;
import me.micrusa.amaztimer.utils.utils;

public class AmazTimer extends Activity {
    private Button plus, plus2, plus3, minus, minus2, minus3, start;
    private TextView sets, rest, work;
    
    private buttonListener buttonListener = new buttonListener();
    
    private boolean hasLaunchedIntent = false;

    private final View.OnClickListener plusMinusBtnListener = view -> plusMinusUpdates(view.getId(), false);
    private final View.OnLongClickListener plusMinusBtnLongListener = view -> plusMinusUpdates(view.getId(), true);

    private boolean plusMinusUpdates(int id, boolean longClick){
        int sets = Prefs.getInt(Constants.KEY_SETS, Constants.DEF_SETS);
        int workTime = Prefs.getInt(Constants.KEY_WORK, Constants.DEF_WORKTIME);
        int restTime = Prefs.getInt(Constants.KEY_REST, Constants.DEF_RESTTIME);
        int[] data = utils.getValues(new int[]{sets, workTime, restTime, id}, longClick, this);
        Prefs.putInt(Constants.KEY_SETS, data[0]);
        Prefs.putInt(Constants.KEY_WORK, data[1]);
        Prefs.putInt(Constants.KEY_REST, data[2]);
        setTexts();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupLang(this);
        setContentView(SystemProperties.isStratos3() || SystemProperties.isVerge() ? R.layout.round_amaztimer : R.layout.amaztimer);
        this.init();
        //Start listening for hr to avoid some time without HR values when started
        hrSensor.initialize(listener -> {});
        hrSensor.getInstance().onMainActCreate(this);

        setupBtnListener();

        plus.setOnClickListener(plusMinusBtnListener);
        plus2.setOnClickListener(plusMinusBtnListener);
        plus3.setOnClickListener(plusMinusBtnListener);
        minus.setOnClickListener(plusMinusBtnListener);
        minus2.setOnClickListener(plusMinusBtnListener);
        minus3.setOnClickListener(plusMinusBtnListener);
        plus.setOnLongClickListener(plusMinusBtnLongListener);
        plus2.setOnLongClickListener(plusMinusBtnLongListener);
        plus3.setOnLongClickListener(plusMinusBtnLongListener);
        minus.setOnLongClickListener(plusMinusBtnLongListener);
        minus2.setOnLongClickListener(plusMinusBtnLongListener);
        minus3.setOnLongClickListener(plusMinusBtnLongListener);

        start.setOnClickListener(view -> launchIntent(new Intent(view.getContext(),
                Prefs.getBoolean(Constants.KEY_ENABLEPREPARE, false)
                        ? PrepareActivity.class : TimerActivity.class)));

        start.setOnLongClickListener(view -> launchIntent(new Intent(view.getContext(), SettingsActivity.class)));
    }

    private void setTexts(){
        sets.setText(String.valueOf(Prefs.getInt(Constants.KEY_SETS, Constants.DEF_SETS)));
        work.setText(utils.formatTime(Prefs.getInt(Constants.KEY_WORK, Constants.DEF_WORKTIME)));
        rest.setText(utils.formatTime(Prefs.getInt(Constants.KEY_REST, Constants.DEF_RESTTIME)));
    }

    private void init() {
        plus = findViewById(R.id.plus);
        plus2 = findViewById(R.id.plus2);
        plus3 = findViewById(R.id.plus3);
        minus = findViewById(R.id.minus2);
        minus2 = findViewById(R.id.minus);
        minus3 = findViewById(R.id.minus3);
        start = findViewById(R.id.start);
        sets = findViewById(R.id.sets);
        rest = findViewById(R.id.rest);
        work = findViewById(R.id.work);
    }

    public void onStart() {
        super.onStart();
        hasLaunchedIntent = false;
        setTexts();
    }

    public void onStop() {
        super.onStop();
        buttonListener.stop();
    }

    public void onPause() {
        buttonListener.stop();
        super.onPause();
    }

     public void onDestroy(){
        super.onDestroy();
        hrSensor.getInstance().onMainActDestroy(this);
     }

    public void onResume() {
        hasLaunchedIntent = false;
        hrSensor.getInstance().onMainActCreate(this);
        setupBtnListener();
        super.onResume();
    }

    private boolean launchIntent(Intent intent){
        if(hasLaunchedIntent) return true; //Avoid multiple activities launched
        utils.vibrate(Constants.HAPTIC_VIBRATION, this);
        hasLaunchedIntent = true;
        buttonListener.stop();
        startActivity(intent);
        return true;
    }

    private void setupBtnListener(){
        Handler handler = new Handler();
        buttonListener.start(this, ButtonEvent -> {
            if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                handler.post(() -> start.performClick());
            else if(ButtonEvent.getKey() == buttonEvent.KEY_CENTER)
                handler.post(() -> start.performLongClick());
        });
    }
}
