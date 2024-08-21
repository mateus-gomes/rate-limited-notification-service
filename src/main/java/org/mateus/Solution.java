package org.mateus;

import org.mateus.exceptions.NoSuchNotificationTypeException;
import org.mateus.exceptions.OutOfRateLimitException;
import org.mateus.notifications.NotificationServiceImpl;

import java.time.temporal.ChronoUnit;

public class Solution {
    public static void main(String[] args) {
        NotificationServiceImpl service = new NotificationServiceImpl();

        service.createNotificationType("Status", 10, ChronoUnit.SECONDS, 2);
        service.createNotificationType("News", 1, ChronoUnit.DAYS, 1);

       try {
           service.send("Status", "usertest", "Status 1");
           service.send("Status", "usertest", "Status 2");

           Thread.sleep(5 * 1000);

           service.send("Status", "usertest2", "Status 3");
           service.send("Status2", "usertest", "Status 3");
       }catch (NoSuchNotificationTypeException | OutOfRateLimitException exception){
           System.out.println(exception.getMessage());
       } catch (InterruptedException ie) {
           Thread.currentThread().interrupt();
       }
    }
}