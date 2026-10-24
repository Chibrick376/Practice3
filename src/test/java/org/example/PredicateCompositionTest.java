package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class PredicateCompositionTest
{

    private BankService bank;
    private BlockedAccountRegistry blocked;

    private DebitAccount debit1;
    private DebitAccount debit2;
    private CreditAccount credit1;

    @BeforeEach
    void setUp()
    {
        blocked = new BlockedAccountRegistry();
        bank = new BankService(
                new InMemoryRepository<>(),
                new TransferService(
                        new NoCommission(),
                        new FakeNotificationService(),
                        blocked,
                        new TransactionJournal()
                )
        );

        debit1 = new DebitAccount(new AccountNumber("0000000001"), "A", 150_000);
        debit2 = new DebitAccount(new AccountNumber("0000000002"), "B", 50_000);
        credit1 = new CreditAccount(new AccountNumber("0000000003"), "C", 200_000, 1000);

        bank.openAccount(debit1);
        bank.openAccount(debit2);
        bank.openAccount(credit1);

        blocked.block(debit1.getNumber());
    }

    private Predicate<BankAccount> rich()
    {
        return a -> a.getBalance() >= 100_000;
    }

    private Predicate<BankAccount> notBlocked()
    {
        return a -> !blocked.isBlocked(a.getId());
    }

    private Predicate<BankAccount> debit()
    {
        return DebitAccount.class::isInstance;
    }

    private Predicate<BankAccount> credit()
    {
        return CreditAccount.class::isInstance;
    }

    @Test
    void accountsWithBalanceAtLeastGivenSum()
    {
        List<BankAccount> result = bank.findAccounts(rich());
        assertEquals(2, result.size());
        assertTrue(result.contains(debit1));
        assertTrue(result.contains(credit1));
    }

    @Test
    void notBlockedAccounts()
    {
        List<BankAccount> result = bank.findAccounts(notBlocked());
        assertEquals(2, result.size());
        assertTrue(result.contains(debit2));
        assertTrue(result.contains(credit1));
    }

    @Test
    void onlyDebitAccounts()
    {
        List<BankAccount> result = bank.findAccounts(debit());
        assertEquals(2, result.size());
        assertTrue(result.contains(debit1));
        assertTrue(result.contains(debit2));
    }

    @Test
    void richAndNotBlocked()
    {
        Predicate<BankAccount> candidates = rich().and(notBlocked());
        List<BankAccount> result = bank.findAccounts(candidates);

        assertEquals(1, result.size());
        assertSame(credit1, result.get(0));
    }

    @Test
    void notDebitAccounts()
    {
        List<BankAccount> result = bank.findAccounts(debit().negate());
        assertEquals(1, result.size());
        assertSame(credit1, result.get(0));
    }

    @Test
    void richOrCredit()
    {
        Predicate<BankAccount> candidates = rich().or(credit());
        List<BankAccount> result = bank.findAccounts(candidates);

        assertEquals(2, result.size());
        assertTrue(result.contains(debit1));
        assertTrue(result.contains(credit1));
    }
}