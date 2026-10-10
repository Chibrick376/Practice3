package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErasureDemoTest
{

    @Test
    void differentGenericTypesShareSameRuntimeClass()
    {
        List<String> strings = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        assertEquals(strings.getClass(), numbers.getClass());
    }

    @Test
    void runtimeClassIsJustArrayList()
    {
        List<String> strings = new ArrayList<>();
        assertEquals(ArrayList.class, strings.getClass());
    }

    @Test
    void genericsAreErasedAtRuntime()
    {
        List<String> strings = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        assertSame(strings.getClass(), numbers.getClass());
    }
}