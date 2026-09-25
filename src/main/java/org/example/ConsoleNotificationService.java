package org.example;

/**
 * Простейшая реализация NotificationService —
 * выводит сообщение в консоль.
 */
public class ConsoleNotificationService implements NotificationService
{

    @Override
    public void notify(String message)
    {
        System.out.println(message);
    }
}
