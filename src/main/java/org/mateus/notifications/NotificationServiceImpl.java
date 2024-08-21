package org.mateus.notifications;

import org.mateus.entities.NotificationLimit;
import org.mateus.exceptions.NoSuchNotificationTypeException;
import org.mateus.exceptions.OutOfRateLimitException;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.LinkedList;

public class NotificationServiceImpl implements NotificationService{

    private final HashMap<String, NotificationLimit> notificationLimitHashMap;
    private final HashMap<String, LinkedList<LocalDateTime>> userNotificationHashMap;

    public NotificationServiceImpl() {
        this.notificationLimitHashMap = new HashMap<String, NotificationLimit>();
        this.userNotificationHashMap = new HashMap<String, LinkedList<LocalDateTime>>();
    }

    @Override
    public boolean send(String type, String userId, String message) {
        saveUserNotification(userId, type);
        System.out.printf("Sending %s email to user %s with the content %s%n", type, userId, message);

        return true;
    }

    public void createNotificationType(
            String notificationKey,
            long timeAmount,
            TemporalUnit temporalUnit,
            int maxNotificationAmount
    ){
        notificationLimitHashMap.put(notificationKey,
                new NotificationLimit(timeAmount, temporalUnit, maxNotificationAmount));
    }

    private void saveUserNotification(String userId, String notificationKey){
        String mapKey = String.format("%s_%s", userId, notificationKey);

        if(!queueExists(userId, notificationKey)) {
            userNotificationHashMap.put(mapKey, new LinkedList<LocalDateTime>());
        }

        LinkedList<LocalDateTime> notificationQueue = userNotificationHashMap.get(mapKey);

        if(isValid(userId, notificationKey)){
            notificationQueue.add(LocalDateTime.now());
            userNotificationHashMap.put(mapKey, notificationQueue);
            return;
        }

        throw new OutOfRateLimitException(String.format(
                "Can't send %s notification type to User %s. Max notifications already reached.",
                notificationKey,
                userId
        ));
    };

    private boolean isValid(String userId, String notificationKey){
        String mapKey = String.format("%s_%s", userId, notificationKey);

        if(!notificationHashMapExists(notificationKey)){
            throw new NoSuchNotificationTypeException("There is no such notification type as " + notificationKey);
        }

        NotificationLimit notificationLimit = notificationLimitHashMap.get(notificationKey);
        LinkedList<LocalDateTime> notificationQueue = userNotificationHashMap.get(mapKey);

        if(notificationQueue.isEmpty()){
            return true;
        }

        if(notificationQueue.size() >= notificationLimit.getMaxNotificationAmount()){
            LocalDateTime firstNotificationTime = notificationQueue.peek();
            LocalDateTime expirationTime = firstNotificationTime.plus(
                    notificationLimit.getTimeAmount(), notificationLimit.getTemporalUnit()
            );

            if(LocalDateTime.now().isAfter(expirationTime)){
                notificationQueue.pop();
                return true;
            }

            return false;
        }

        return true;
    }

    private boolean queueExists(String userId, String notificationKey){
        return userNotificationHashMap.containsKey(
                String.format("%s_%s", userId, notificationKey)
        );
    }

    private boolean notificationHashMapExists(String notificationKey){
        return notificationLimitHashMap.containsKey(notificationKey);
    }
}
