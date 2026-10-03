package org.example;

/**
 * Бросается, когда сущность с указанным ID не найдена.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}