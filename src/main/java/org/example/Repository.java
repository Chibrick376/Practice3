package org.example;

/**
 * Универсальный контракт репозитория.
 * T — сущность, ID — тип её идентификатора.
 */
public interface Repository<ID, T extends Identifiable<ID>> {
    void save(T value);
    T findById(ID id);
    boolean existsById(ID id);
    int size();
}