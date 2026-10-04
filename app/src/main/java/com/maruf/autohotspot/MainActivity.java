package com.maruf.autohotspot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends Activity {

    Switch enabledSwitch;
    EditText delayInput;
    TimePicker timePicker;
    Button saveButton;
    Button accessibilityButton;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        enabledSwitch = findViewById(R.id.enabledSwitch);
        delayInput = findViewById(R.id.delayInput);
        timePicker = findViewById(R.id.timePicker);
        saveButton = findViewById(R.id.saveButton);
        accessibilityButton = findViewById(R.id.accessibilityButton);

        delayInput.setText(String.valueOf(getPreferences(0).getInt("delay", 30)));

        Calendar c = Calendar.getInstance();
        timePicker.setHour(getPreferences(0).getInt("hour", c.get(Calendar.HOUR_OF_DAY)));
        timePicker.setMinute(getPreferences(0).getInt("minute", c.get(Calendar.MINUTE)));

        enabledSwitch.setChecked(getPreferences(0).getBoolean("enabled", false));

        saveButton.setOnClickListener(v -> {
            int delay = 30;

            try {
                delay = Integer.parseInt(delayInput.getText().toString());
            } catch (Exception ignored) {
            }

            if (delay < 1) delay = 1;

            getPreferences(0)
                    .edit()
                    .putBoolean("enabled", enabledSwitch.isChecked())
                    .putInt("delay", delay)
                    .putInt("hour", timePicker.getHour())
                    .putInt("minute", timePicker.getMinute())
                    .apply();

            AlarmScheduler.schedule(this);
        });

        accessibilityButton.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        );
    }
}