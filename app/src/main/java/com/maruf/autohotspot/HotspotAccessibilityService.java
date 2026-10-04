package com.maruf.autohotspot;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class HotspotAccessibilityService extends AccessibilityService {

    private static HotspotAccessibilityService instance;
    private final Handler handler = new Handler();

    @Override
    protected void onServiceConnected() {
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    public static void openHotspot(Context context) {
        if (instance != null) {
            instance.enableHotspot();
        } else {
            Intent i = new Intent(Settings.ACTION_SETTINGS);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    private void enableHotspot() {
        performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS);

        handler.postDelayed(() -> {
            AccessibilityNodeInfo root = getRootInActiveWindow();

            if (root == null) {
                performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS);
                return;
            }

            clickHotspot(root);
        }, 800);
    }

    private boolean clickHotspot(AccessibilityNodeInfo node) {
        if (node == null) return false;

        CharSequence text = node.getText();
        CharSequence description = node.getContentDescription();

        String value = "";

        if (text != null) value += text.toString().toLowerCase() + " ";
        if (description != null) value += description.toString().toLowerCase();

        if (value.contains("hotspot")
                || value.contains("mobile hotspot")
                || value.contains("portable hotspot")
                || value.contains("tethering")) {

            if (node.isClickable() && node.isEnabled()) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }

            AccessibilityNodeInfo parent = node.getParent();

            if (parent != null) {
                if (parent.isClickable() && parent.isEnabled()) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);

            if (clickHotspot(child)) {
                return true;
            }
        }

        return false;
    }
}