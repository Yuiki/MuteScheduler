package com.yuikibis.mutescheduler.viewController;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import com.activeandroid.query.Select;
import com.yuikibis.mutescheduler.model.Schedule;

import java.util.Calendar;
import java.util.List;

public class MuteService extends IntentService {
    public MuteService() {
        super("MuteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int id = intent.getIntExtra("id", 0);
        Schedule schedule = new Select().from(Schedule.class).where("idx = ?", Math.abs(id)).executeSingle();
        if (schedule == null) {
            return;
        }

        if (id > 0) {
            if (!checkDayWeek(schedule)) {
                return;
            }

            if (schedule.isAlarm) {
                schedule.alarmVolume = am.getStreamVolume(AudioManager.STREAM_ALARM);
                am.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
            }
            if (schedule.isRing) {
                schedule.ringVolume = am.getStreamVolume(AudioManager.STREAM_RING);
                am.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
            if (schedule.isMedia) {
                schedule.mediaVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            }
            schedule.save();
        } else if (id < 0) {
            // 次の日に設定
            Calendar calendar = Calendar.getInstance();
            calendar.set(schedule.year, schedule.month, schedule.date);
            calendar.add(Calendar.DATE, 1);
            schedule.year = calendar.get(Calendar.YEAR);
            schedule.month = calendar.get(Calendar.MONTH);
            schedule.date = calendar.get(Calendar.DATE);
            schedule.save();
            registerAlarm(getApplicationContext(), schedule);

            if (!checkDayWeek(schedule)) {
                return;
            }

            if (schedule.isRing) {
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                am.setStreamVolume(AudioManager.STREAM_RING, schedule.ringVolume, 0);
            }
            if (schedule.isAlarm) {
                am.setStreamVolume(AudioManager.STREAM_ALARM, schedule.alarmVolume, 0);
            }
            if (schedule.isMedia) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, schedule.mediaVolume, 0);
            }
        }
    }

    private boolean checkDayWeek(Schedule schedule) {
        Calendar calendar = Calendar.getInstance();
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return schedule.isSun;
            case Calendar.MONDAY:
                return schedule.isMon;
            case Calendar.TUESDAY:
                return schedule.isTue;
            case Calendar.WEDNESDAY:
                return schedule.isWed;
            case Calendar.THURSDAY:
                return schedule.isThu;
            case Calendar.FRIDAY:
                return schedule.isFri;
            case Calendar.SATURDAY:
                return schedule.isSat;
            default:
                return false;
        }
    }

    public static void registerAlarm(Context context, Schedule schedule) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(schedule.year, schedule.month, schedule.date, schedule.startHour, schedule.startMinute);
        end.set(schedule.year, schedule.month, schedule.date, schedule.endHour, schedule.endMinute);
        Intent startIntent = new Intent(context, MuteService.class);
        Intent endIntent = new Intent(context, MuteService.class);
        startIntent.putExtra("id", schedule.idx);
        // 値を戻すIntentのidは-1を掛ける
        endIntent.putExtra("id", schedule.idx * -1);
        PendingIntent pendingIntentStart = PendingIntent.getService(context, schedule.idx, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentEnd = PendingIntent.getService(context, schedule.idx * -1, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT < 19) {
            am.set(AlarmManager.RTC, start.getTimeInMillis(), pendingIntentStart);
            am.set(AlarmManager.RTC, end.getTimeInMillis(), pendingIntentEnd);
        } else {
            am.setExact(AlarmManager.RTC, start.getTimeInMillis(), pendingIntentStart);
            am.setExact(AlarmManager.RTC, end.getTimeInMillis(), pendingIntentEnd);
        }
    }

    public static void reRegisterAlarm(Context context) {
        List<Schedule> schedules = new Select().from(Schedule.class).execute();
        for (Schedule schedule : schedules) {
            if (schedule.isEnabled) {
                registerAlarm(context, schedule);
            }
        }
    }

    public static void unregisterAlarm(Context context, Schedule schedule) {
        PendingIntent pendingIntentStart = PendingIntent.getService(context, schedule.idx, new Intent(context, MuteService.class), 0);
        PendingIntent pendingIntentEnd = PendingIntent.getService(context, schedule.idx * -1, new Intent(context, MuteService.class), 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntentStart);
        am.cancel(pendingIntentEnd);
    }
}
