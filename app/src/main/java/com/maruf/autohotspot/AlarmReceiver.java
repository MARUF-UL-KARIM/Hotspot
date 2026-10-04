package com.maruf.autohotspot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        HotspotAccessibilityService.openHotspot(context);
        AlarmScheduler.schedule(context);
    }
}