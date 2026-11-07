package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorTest
{

    private BankService bank;

    private DebitAccount debit1;
    private DebitAccount debit2;
    private CreditAccount credit1;

    @BeforeEach
    void setUp()
    {
        bank = new BankService(
                new InMemoryRepository<>(),
                new TransferService(
                        new NoCommission(),
                        new FakeNotificationService(),
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        debit1 = new DebitAccount(new AccountNumber("0000000003"), "A", 50_000);
        debit2 = new DebitAccount(new AccountNumber("0000000001"), "B", 150_000);
        credit1 = new CreditAccount(new AccountNumber("0000000002"), "C", 100_000, 1000);

        bank.openAccount(debit1);
        bank.openAccount(debit2);
        bank.openAccount(credit1);
    }

    @Test
    void sortedByBalanceAscending()
    {
        Comparator<BankAccount> byBalance =
                Comparator.comparingDouble(BankAccount::getBalance);

        List<BankAccount> result = bank.sortedAccounts(byBalance);

        assertEquals(3, result.size());
        assertSame(debit1, result.get(0));
        assertSame(credit1, result.get(1));
        assertSame(debit2, result.get(2));
    }

    @Test
    void sortedByBalanceDescending()
    {
        Comparator<BankAccount> byBalanceDesc =
                Comparator.comparingDouble(BankAccount::getBalance).reversed();

        List<BankAccount> result = bank.sortedAccounts(byBalanceDesc);

        assertEquals(3, result.size());
        assertSame(debit2, result.get(0));
        assertSame(credit1, result.get(1));
        assertSame(debit1, result.get(2));
    }

    @Test
    void sortedByNumber()
    {
        Comparator<BankAccount> byNumber =
                Comparator.comparing(a -> a.getId().value());

        List<BankAccount> result = bank.sortedAccounts(byNumber);

        assertEquals(3, result.size());
        assertSame(debit2, result.get(0));
        assertSame(credit1, result.get(1));
        assertSame(debit1, result.get(2));
    }

    @Test
    void sortedByTypeThenBalance()
    {
        Comparator<BankAccount> byTypeThenBalance =
                Comparator
                        .comparing((BankAccount a) -> a.getClass().getSimpleName())
                        .thenComparingDouble(BankAccount::getBalance);

        List<BankAccount> result = bank.sortedAccounts(byTypeThenBalance);

        assertEquals(3, result.size());
        assertSame(credit1, result.get(0));
        assertSame(debit1, result.get(1));
        assertSame(debit2, result.get(2));
    }

    @Test
    void sortingDoesNotModifyRepository()
    {
        Comparator<BankAccount> byBalanceDesc =
                Comparator.comparingDouble(BankAccount::getBalance).reversed();

        bank.sortedAccounts(byBalanceDesc);

        // Хранилище должно остаться прежним: счета ищутся по номерам,
        // повторная сортировка по номеру даёт исходный порядок номеров
        assertSame(debit1, bank.findAccount(debit1.getNumber()));
        assertSame(debit2, bank.findAccount(debit2.getNumber()));
        assertSame(credit1, bank.findAccount(credit1.getNumber()));

        Comparator<BankAccount> byNumber =
                Comparator.comparing(a -> a.getId().value());
        List<BankAccount> result = bank.sortedAccounts(byNumber);

        assertSame(debit2, result.get(0));
        assertSame(credit1, result.get(1));
        assertSame(debit1, result.get(2));
    }
}
