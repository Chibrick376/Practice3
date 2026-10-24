package org.example;

/**
 * Сервис уведомлений.
 */
@FunctionalInterface
public interface NotificationService
{
    void notify(String message);
}