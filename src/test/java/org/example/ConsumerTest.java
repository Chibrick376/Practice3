package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerTest
{

    @Test
    void forEachAppliesActionToEveryElement()
    {
        List<String> names = List.of("A", "B", "C");
        List<String> collected = new ArrayList<>();

        CollectionFunctions.forEach(names, collected::add);

        assertEquals(List.of("A", "B", "C"), collected);
    }

    @Test
    void forEachProcessesElementsInOrder()
    {
        List<Integer> numbers = List.of(3, 1, 2);
        List<Integer> order = new ArrayList<>();

        CollectionFunctions.forEach(numbers, order::add);

        assertEquals(List.of(3, 1, 2), order);
    }

    @Test
    void forEachOnEmptyListDoesNothing()
    {
        List<String> empty = List.of();
        List<String> collected = new ArrayList<>();

        CollectionFunctions.forEach(empty, collected::add);

        assertTrue(collected.isEmpty());
    }

    @Test
    void consumerOfSupertypeWorksWithWildcard()
    {
        List<AccountSummary> summaries = List.of(
                new AccountSummary(new AccountNumber("0000000001"), "DebitAccount", 100),
                new AccountSummary(new AccountNumber("0000000002"), "CreditAccount", 200)
        );
        List<Object> collected = new ArrayList<>();

        Consumer<Object> collector = collected::add;
        CollectionFunctions.forEach(summaries, collector);

        assertEquals(2, collected.size());
        assertEquals("DebitAccount", ((AccountSummary) collected.get(0)).type());
        assertEquals(200, ((AccountSummary) collected.get(1)).balance());
    }
}
