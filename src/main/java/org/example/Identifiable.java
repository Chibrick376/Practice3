package org.example;

/**
 * Контракт для сущностей, у которых есть идентификатор.
 */
public interface Identifiable<ID> {
    ID getId();
}