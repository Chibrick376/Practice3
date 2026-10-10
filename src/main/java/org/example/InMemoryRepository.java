package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Репозиторий в памяти на базе HashMap.
 * При повторном save() с тем же ID старое значение заменяется.
 */
public class InMemoryRepository<ID, T extends Identifiable<ID>>
        implements Repository<ID, T>
{

    private final Map<ID, T> values = new HashMap<>();

    @Override
    public void save(T value)
    {
        if (value == null)
            throw new IllegalArgumentException("Value must not be null");

        values.put(value.getId(), value);
    }

    @Override
    public T findById(ID id)
    {
        return values.get(id);
    }

    @Override
    public boolean existsById(ID id)
    {
        return values.containsKey(id);
    }

    @Override
    public boolean deleteById(ID id)
    {
        return values.remove(id) != null;
    }

    @Override
    public List<T> findAll()
    {
        return new ArrayList<>(values.values());
    }

    @Override
    public int size()
    {
        return values.size();
    }
}