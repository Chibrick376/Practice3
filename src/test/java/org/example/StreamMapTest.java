package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreamMapTest
{

    private BankService bank;

    private DebitAccount debit1;
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

        bank.openAccount(debit1);
        bank.openAccount(credit1);
    }

    @Test
    void getAccountSummariesMapsEveryAccount()
    {
        List<AccountSummary> summaries = bank.getAccountSummaries();

        assertEquals(2, summaries.size());
    }

    @Test
    void summaryContainsNumberTypeAndBalance()
    {
        List<AccountSummary> summaries = bank.getAccountSummaries();

        AccountSummary debitSummary = summaries.stream()
                .filter(s -> s.number().equals(new AccountNumber("0000000001")))
                .findFirst()
                .orElseThrow();

        assertEquals("DebitAccount", debitSummary.type());
        assertEquals(150_000, debitSummary.balance());
    }

    @Test
    void summaryTypeComesFromRuntimeClass()
    {
        List<AccountSummary> summaries = bank.getAccountSummaries();

        AccountSummary creditSummary = summaries.stream()
                .filter(s -> s.number().equals(new AccountNumber("0000000002")))
                .findFirst()
                .orElseThrow();

        assertEquals("CreditAccount", creditSummary.type());
        assertEquals(200_000, creditSummary.balance());
    }

    @Test
    void getAccountSummariesDoesNotChangeRepository()
    {
        bank.getAccountSummaries();

        assertSame(debit1, bank.findAccount(debit1.getNumber()));
        assertSame(credit1, bank.findAccount(credit1.getNumber()));
    }

    @Test
    void emptyRepositoryReturnsEmptySummaryList()
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

        assertTrue(emptyBank.getAccountSummaries().isEmpty());
    }
}
