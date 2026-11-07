package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class StreamShortCircuitTest
{

    private BankService bank;
    private BlockedAccountRegistry blocked;

    private DebitAccount debitRich;
    private DebitAccount debitZero;
    private CreditAccount credit;

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

        debitRich = new DebitAccount(new AccountNumber("0000000001"), "A", 150_000);
        debitZero = new DebitAccount(new AccountNumber("0000000002"), "B", 0);
        credit = new CreditAccount(new AccountNumber("0000000003"), "C", 200_000, 1000);

        bank.openAccount(debitRich);
        bank.openAccount(debitZero);
        bank.openAccount(credit);
    }

    @Test
    void hasBlockedAccountsTrueWhenSomeAccountBlocked()
    {
        blocked.block(debitRich.getNumber());

        assertTrue(bank.hasBlockedAccounts());
    }

    @Test
    void hasBlockedAccountsFalseWhenNothingBlocked()
    {
        assertFalse(bank.hasBlockedAccounts());
    }

    @Test
    void allAccountsPositiveFalseWhenZeroBalanceExists()
    {
        assertFalse(bank.allAccountsPositive());
    }

    @Test
    void allAccountsPositiveTrueWhenAllBalancesAboveZero()
    {
        bank.closeAccount(debitZero.getNumber());

        assertTrue(bank.allAccountsPositive());
    }

    @Test
    void noCreditAccountsFalseWhenCreditExists()
    {
        assertFalse(bank.noCreditAccounts());
    }

    @Test
    void noCreditAccountsTrueWhenOnlyDebit()
    {
        bank.closeAccount(credit.getNumber());

        assertTrue(bank.noCreditAccounts());
    }

    @Test
    void anyMatchStopsBeforeEndOfList()
    {
        List<BankAccount> accounts = List.of(debitRich, debitZero, credit);
        AtomicInteger calls = new AtomicInteger();

        boolean result = accounts.stream()
                .anyMatch(a ->
                {
                    calls.incrementAndGet();
                    return a.getBalance() >= 100_000;
                });

        assertTrue(result);
        assertEquals(1, calls.get(), "anyMatch должен остановиться после первого совпадения");
    }

    @Test
    void anyMatchScansWholeListWhenNoMatch()
    {
        List<BankAccount> accounts = List.of(debitZero, debitZero, debitZero);
        AtomicInteger calls = new AtomicInteger();

        boolean result = accounts.stream()
                .anyMatch(a ->
                {
                    calls.incrementAndGet();
                    return a.getBalance() >= 100_000;
                });

        assertFalse(result);
        assertEquals(3, calls.get(), "без совпадения предикат вызывается для каждого элемента");
    }
}
