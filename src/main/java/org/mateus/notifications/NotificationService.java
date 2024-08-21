package org.mateus.notifications;

import java.time.temporal.TemporalUnit;

public interface NotificationService {
    void send(String type, String userId, String message);
    void createNotificationType(
            String notificationKey,
            long timeAmount,
            TemporalUnit temporalUnit,
            int maxNotificationAmount
    );
}
