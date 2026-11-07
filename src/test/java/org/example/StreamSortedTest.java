package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreamSortedTest
{

    private BankService bank;
    private InMemoryRepository<AccountNumber, BankAccount> repository;

    private DebitAccount debitA;
    private DebitAccount debitB;
    private CreditAccount creditA;

    @BeforeEach
    void setUp()
    {
        repository = new InMemoryRepository<>();
        bank = new BankService(
                repository,
                new TransferService(
                        new NoCommission(),
                        new FakeNotificationService(),
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        // намеренно не в алфавитном порядке владельцев и не по номерам
        debitB = new DebitAccount(new AccountNumber("0000000002"), "B", 50_000);
        creditA = new CreditAccount(new AccountNumber("0000000003"), "A", 200_000, 1000);
        debitA = new DebitAccount(new AccountNumber("0000000001"), "A", 150_000);

        bank.openAccount(debitB);
        bank.openAccount(creditA);
        bank.openAccount(debitA);
    }

    @Test
    void richestFirstOrdersByBalanceDescending()
    {
        List<BankAccount> result = bank.richestFirst();

        assertEquals(3, result.size());
        assertSame(creditA, result.get(0));
        assertSame(debitA, result.get(1));
        assertSame(debitB, result.get(2));
    }

    @Test
    void sortedByNumberOrdersByAccountNumber()
    {
        List<BankAccount> result = bank.sortedByNumber();

        assertSame(debitA, result.get(0));
        assertSame(debitB, result.get(1));
        assertSame(creditA, result.get(2));
    }

    @Test
    void sortedByTypeThenBalanceOrdersByClassThenBalance()
    {
        DebitAccount debitPoor = new DebitAccount(new AccountNumber("0000000004"), "D", 10_000);
        bank.openAccount(debitPoor);

        List<BankAccount> result = bank.sortedByTypeThenBalance();

        assertSame(creditA, result.get(0));
        assertSame(debitPoor, result.get(1));
        assertSame(debitB, result.get(2));
        assertSame(debitA, result.get(3));
    }

    @Test
    void sortedByOwnerThenNumberOrdersByOwnerThenNumber()
    {
        List<BankAccount> result = bank.sortedByOwnerThenNumber();

        // владелец A: счёт 0000000001, затем 0000000003; затем владелец B
        assertSame(debitA, result.get(0));
        assertSame(creditA, result.get(1));
        assertSame(debitB, result.get(2));
    }

    @Test
    void sortingDoesNotChangeRepositoryOrder()
    {
        List<BankAccount> before = new ArrayList<>(repository.findAll());

        bank.richestFirst();
        bank.sortedByNumber();
        bank.sortedByTypeThenBalance();
        bank.sortedByOwnerThenNumber();

        List<BankAccount> after = repository.findAll();

        assertEquals(before, after);
    }
}
