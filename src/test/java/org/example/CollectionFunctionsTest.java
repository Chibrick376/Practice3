package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class CollectionFunctionsTest
{

    @Test
    void filterPositiveBalance()
    {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new DebitAccount(new AccountNumber("0000000001"), "A", 100));
        accounts.add(new DebitAccount(new AccountNumber("0000000002"), "B", -50));
        accounts.add(new DebitAccount(new AccountNumber("0000000003"), "C", 0));

        Predicate<BankAccount> positiveBalance = account -> account.getBalance() > 0;
        List<BankAccount> result = CollectionFunctions.filter(accounts, positiveBalance);

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getOwner());
    }

    @Test
    void filterRich()
    {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new DebitAccount(new AccountNumber("0000000004"), "A", 100_000));
        accounts.add(new DebitAccount(new AccountNumber("0000000005"), "B", 99_999));

        Predicate<BankAccount> rich = account -> account.getBalance() >= 100_000;
        List<BankAccount> result = CollectionFunctions.filter(accounts, rich);

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getOwner());
    }

    @Test
    void filterDebitOnly()
    {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new DebitAccount(new AccountNumber("0000000006"), "A", 100));
        accounts.add(new SavingsAccount(new AccountNumber("0000000007"), "B", 200, 50));
        accounts.add(new CreditAccount(new AccountNumber("0000000008"), "C", 300, 100));

        Predicate<BankAccount> debitOnly = DebitAccount.class::isInstance;
        List<BankAccount> result = CollectionFunctions.filter(accounts, debitOnly);

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof DebitAccount);
    }
}