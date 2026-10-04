package com.maruf.autohotspot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmScheduler {

    public static void schedule(Context context) {
        Calendar c = Calendar.getInstance();

        int hour = context
                .getSharedPreferences("AutoHotspot", Context.MODE_PRIVATE)
                .getInt("hour", c.get(Calendar.HOUR_OF_DAY));

        int minute = context
                .getSharedPreferences("AutoHotspot", Context.MODE_PRIVATE)
                .getInt("minute", c.get(Calendar.MINUTE));

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if (c.getTimeInMillis() <= System.currentTimeMillis()) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent i = new Intent(context, AlarmReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                1001,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (am != null) {
            am.setExact(
                    AlarmManager.RTC_WAKEUP,
                    c.getTimeInMillis(),
                    pi
            );
        }
    }
}