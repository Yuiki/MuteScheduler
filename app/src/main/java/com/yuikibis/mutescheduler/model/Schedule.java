package com.yuikibis.mutescheduler.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Schedules")
public class Schedule extends Model {
    @Column(name = "year")
    public int year;
    @Column(name = "month")
    public int month;
    @Column(name = "date")
    public int date;

    @Column(name = "startHour")
    public int startHour;
    @Column(name = "startMinute")
    public int startMinute;
    @Column(name = "endHour")
    public int endHour;
    @Column(name = "endMinute")
    public int endMinute;

    @Column(name = "isSun")
    public boolean isSun;
    @Column(name = "isMon")
    public boolean isMon;
    @Column(name = "isTue")
    public boolean isTue;
    @Column(name = "isWed")
    public boolean isWed;
    @Column(name = "isThu")
    public boolean isThu;
    @Column(name = "isFri")
    public boolean isFri;
    @Column(name = "isSat")
    public boolean isSat;

    @Column(name = "isMedia")
    public boolean isMedia;
    @Column(name = "isRing")
    public boolean isRing;
    @Column(name = "isAlarm")
    public boolean isAlarm;

    @Column(name = "isEnabled")
    public boolean isEnabled;

    @Column(name = "idx")
    public int idx;

    @Column(name = "mediaVolume")
    public int mediaVolume;
    @Column(name = "ringVolume")
    public int ringVolume;
    @Column(name = "alarmVolume")
    public int alarmVolume;
}
