package org.mateus.entities;

import java.time.temporal.TemporalUnit;

public class NotificationLimit {

    private long timeAmount;
    private TemporalUnit temporalUnit;
    private int maxNotificationAmount;

    public NotificationLimit(long timeAmount, TemporalUnit temporalUnit, int maxNotificationAmount) {
        this.timeAmount = timeAmount;
        this.temporalUnit = temporalUnit;
        this.maxNotificationAmount = maxNotificationAmount;
    }

    public long getTimeAmount() {
        return timeAmount;
    }

    public void setTimeAmount(long timeAmount) {
        this.timeAmount = timeAmount;
    }

    public TemporalUnit getTemporalUnit() {
        return temporalUnit;
    }

    public void setTemporalUnit(TemporalUnit temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public int getMaxNotificationAmount() {
        return maxNotificationAmount;
    }

    public void setMaxNotificationAmount(int maxNotificationAmount) {
        this.maxNotificationAmount = maxNotificationAmount;
    }
}
