package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreamTest
{

    private List<BankAccount> accounts;

    @BeforeEach
    void setUp()
    {
        accounts = List.of(
                new DebitAccount(new AccountNumber("0000000001"), "A", 150_000),
                new DebitAccount(new AccountNumber("0000000002"), "B", 50_000),
                new CreditAccount(new AccountNumber("0000000003"), "C", 200_000, 1000)
        );
    }

    @Test
    void filterRichWithLoop()
    {
        List<BankAccount> result = new ArrayList<>();
        for (BankAccount account : accounts)
        {
            if (account.getBalance() >= 100_000)
            {
                result.add(account);
            }
        }

        assertEquals(2, result.size());
        assertTrue(result.contains(accounts.get(0)));
        assertTrue(result.contains(accounts.get(2)));
    }

    @Test
    void filterRichWithStream()
    {
        List<BankAccount> result = accounts.stream()
                .filter(a -> a.getBalance() >= 100_000)
                .toList();

        assertEquals(2, result.size());
        assertTrue(result.contains(accounts.get(0)));
        assertTrue(result.contains(accounts.get(2)));
    }

    @Test
    void loopAndStreamGiveSameResult()
    {
        List<BankAccount> byLoop = new ArrayList<>();
        for (BankAccount account : accounts)
        {
            if (account.getBalance() >= 100_000)
            {
                byLoop.add(account);
            }
        }

        List<BankAccount> byStream = accounts.stream()
                .filter(a -> a.getBalance() >= 100_000)
                .toList();

        assertEquals(byLoop, byStream);
    }

    @Test
    void streamFilterDoesNotChangeSourceList()
    {
        accounts.stream()
                .filter(a -> a.getBalance() >= 100_000)
                .toList();

        assertEquals(3, accounts.size());
    }
}
