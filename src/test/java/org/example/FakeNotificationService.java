package org.example;

/**
 * Тестовая реализация NotificationService.
 * Не печатает в консоль, а запоминает количество
 * вызовов и последнее сообщение.
 * Используется в тестах, чтобы проверить,
 * что TransferService действительно вызывает notify(...).
 */
public class FakeNotificationService implements NotificationService
{

    private String lastMessage;
    private int notificationCount;

    @Override
    public void notify(String message)
    {
        lastMessage = message;
        notificationCount++;
    }

    public String getLastMessage()
    {
        return lastMessage;
    }

    public int getNotificationCount()
    {
        return notificationCount;
    }
}