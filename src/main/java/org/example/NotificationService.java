package org.example;

/**
 * Сервис уведомлений.
 * Может быть реализован по-разному: вывод в консоль,
 * отправка email, запись в лог и т.д.
 */
public interface NotificationService
{

    /**
     * Отправить уведомление.
     */
    void notify(String message);
}