package com.yuikibis.mutescheduler.viewController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CustomBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().endsWith(Intent.ACTION_PACKAGE_REPLACED)) {
            if (!intent.getDataString().equals("package:" + context.getPackageName())) {
                return;
            }
        }

        MuteService.reRegisterAlarm(context);
    }
}
