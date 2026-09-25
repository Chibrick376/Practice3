package org.example;

/**
 * Тестовая реализация NotificationService.
 * Не печатает в консоль, а запоминает количество
 * вызовов и последнее сообщение.
 * Нужна, чтобы проверять в тестах, что TransferService
 * действительно вызывает notify(...).
 */
public class FakeNotificationService implements NotificationService
{

    private int count = 0;
    private String lastMessage;

    @Override
    public void notify(String message)
    {
        count++;
        lastMessage = message;
    }

    public int getCount()
    {
        return count;
    }

    public String getLastMessage()
    {
        return lastMessage;
    }
}