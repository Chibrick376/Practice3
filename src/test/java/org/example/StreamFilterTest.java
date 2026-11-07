package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreamFilterTest
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

        debit1 = new DebitAccount(new AccountNumber("0000000001"), "A", 150_000);
        credit1 = new CreditAccount(new AccountNumber("0000000002"), "C", 200_000, 1000);
        debit2 = new DebitAccount(new AccountNumber("0000000003"), "B", 50_000);

        bank.openAccount(debit1);
        bank.openAccount(credit1);
        bank.openAccount(debit2);
    }

    @Test
    void findRichAccounts()
    {
        List<BankAccount> result = bank.findRichAccounts(100_000);

        assertEquals(2, result.size());
        assertTrue(result.contains(debit1));
        assertTrue(result.contains(credit1));
    }

    @Test
    void findDebitAccounts()
    {
        List<BankAccount> result = bank.findDebitAccounts();

        assertEquals(2, result.size());
        assertTrue(result.contains(debit1));
        assertTrue(result.contains(debit2));
    }

    @Test
    void findAccountsByOwner()
    {
        List<BankAccount> result = bank.findAccountsByOwner("C");

        assertEquals(1, result.size());
        assertSame(credit1, result.get(0));
    }

    @Test
    void filtersDoNotChangeRepository()
    {
        bank.findRichAccounts(100_000);
        bank.findDebitAccounts();
        bank.findAccountsByOwner("A");

        assertEquals(3, bank.sortedAccounts(
                Comparator.comparing(a -> a.getId().value())).size());
        assertSame(debit1, bank.findAccount(debit1.getNumber()));
        assertSame(credit1, bank.findAccount(credit1.getNumber()));
        assertSame(debit2, bank.findAccount(debit2.getNumber()));
    }

    @Test
    void filterPreservesOrder()
    {
        List<BankAccount> byNumber = bank.sortedAccounts(
                Comparator.comparing(a -> a.getId().value()));

        List<BankAccount> result = bank.findRichAccounts(100_000);

        // порядок внутри результата совпадает с порядком обхода хранилища
        int posDebit1 = byNumber.indexOf(debit1);
        int posCredit1 = byNumber.indexOf(credit1);
        assertTrue(result.indexOf(debit1) < result.indexOf(credit1) == posDebit1 < posCredit1);
    }

    @Test
    void emptyRepositoryReturnsEmptyList()
    {
        BankService emptyBank = new BankService(
                new InMemoryRepository<>(),
                new TransferService(
                        new NoCommission(),
                        new FakeNotificationService(),
                        new BlockedAccountRegistry(),
                        new TransactionJournal()
                )
        );

        assertTrue(emptyBank.findRichAccounts(0).isEmpty());
        assertTrue(emptyBank.findDebitAccounts().isEmpty());
        assertTrue(emptyBank.findAccountsByOwner("A").isEmpty());
    }
}
