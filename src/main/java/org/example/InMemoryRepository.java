package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий в памяти на базе ArrayList.
 * При повторном save() с тем же ID старый объект заменяется.
 */
public class InMemoryRepository<ID, T extends Identifiable<ID>>
        implements Repository<ID, T> {

    private final List<T> values = new ArrayList<>();

    @Override
    public void save(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }
        ID id = value.getId();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getId().equals(id)) {
                values.set(i, value);
                return;
            }
        }
        values.add(value);
    }

    @Override
    public T findById(ID id) {
        for (T value : values) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        throw new EntityNotFoundException("Entity not found: " + id);
    }

    @Override
    public boolean existsById(ID id) {
        for (T value : values) {
            if (value.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return values.size();
    }
}