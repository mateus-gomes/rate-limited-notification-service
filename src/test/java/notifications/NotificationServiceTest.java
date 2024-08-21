package notifications;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mateus.exceptions.NoSuchNotificationTypeException;
import org.mateus.exceptions.OutOfRateLimitException;
import org.mateus.notifications.NotificationServiceImpl;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotificationServiceTest {

    private NotificationServiceImpl service;

    @Test
    @DisplayName("Should throw NoSuchNotificationTypeException when trying to send unknown notification type")
    public void test_notification_type_exception(){
        service = new NotificationServiceImpl();

        service.createNotificationType("Status", 10, ChronoUnit.SECONDS, 2);

        NoSuchNotificationTypeException exception = assertThrows(NoSuchNotificationTypeException.class, () ->
                service.send("Status2", "usertest", "Status 3")
        );

        assertEquals("There is no such notification type as Status2", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw OutOfRateLimitException when trying to send messages above limit")
    public void test_queue_out_of_limit_exception(){
        service = new NotificationServiceImpl();

        service.createNotificationType("Status", 10, ChronoUnit.SECONDS, 2);
        service.send("Status", "usertest", "Status 1");
        service.send("Status", "usertest", "Status 2");

        OutOfRateLimitException exception = assertThrows(OutOfRateLimitException.class, () ->
                service.send("Status", "usertest", "Status 3")
        );

        assertEquals(
                "Can't send Status notification type to User usertest. Max notifications already reached.",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Should return true when message is sent successfully")
    public void test_send_message_success(){
        service = new NotificationServiceImpl();

        service.createNotificationType("Status", 10, ChronoUnit.SECONDS, 2);
        service.send("Status", "usertest", "Status 1");

        assertEquals(service.send("Status", "usertest", "Status 2"),true);
    }

    @Test
    @DisplayName("Should return true when message is sent successfully after the time limit exceeds")
    public void test_send_many_message_success(){
        service = new NotificationServiceImpl();

        service.createNotificationType("Status", 3, ChronoUnit.SECONDS, 2);
        service.send("Status", "usertest", "Status 1");
        service.send("Status", "usertest", "Status 2");

        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        assertEquals(service.send("Status", "usertest", "Status 3"),true);
    }
}
