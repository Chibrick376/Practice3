package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StreamFindFirstTest
{

    private BankService bank;

    private DebitAccount debitRich;
    private DebitAccount debitPoor;

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

        debitRich = new DebitAccount(new AccountNumber("0000000001"), "A", 150_000);
        debitPoor = new DebitAccount(new AccountNumber("0000000002"), "B", 50_000);

        bank.openAccount(debitRich);
        bank.openAccount(debitPoor);
    }

    @Test
    void findFirstRichAccountReturnsPresentOptional()
    {
        Optional<BankAccount> result = bank.findFirstRichAccount(100_000);

        assertTrue(result.isPresent());
        assertSame(debitRich, result.get());
    }

    @Test
    void findFirstRichAccountReturnsEmptyOptionalWhenNoRich()
    {
        Optional<BankAccount> result = bank.findFirstRichAccount(1_000_000);

        assertTrue(result.isEmpty());
    }

    @Test
    void orElseThrowReturnsAccountWhenPresent()
    {
        BankAccount account = bank.findFirstRichAccount(100_000)
                .orElseThrow(() ->
                        new IllegalStateException("No rich account"));

        assertSame(debitRich, account);
    }

    @Test
    void orElseThrowThrowsWhenAbsent()
    {
        assertThrows(IllegalStateException.class, () ->
                bank.findFirstRichAccount(1_000_000)
                        .orElseThrow(() ->
                                new IllegalStateException("No rich account")));
    }

    @Test
    void mapWithOrElseReturnsLabelWhenPresent()
    {
        String label = bank.findFirstRichAccount(100_000)
                .map(a -> a.getId().value())
                .orElse("not found");

        assertEquals("0000000001", label);
    }

    @Test
    void mapWithOrElseReturnsDefaultWhenAbsent()
    {
        String label = bank.findFirstRichAccount(1_000_000)
                .map(a -> a.getId().value())
                .orElse("not found");

        assertEquals("not found", label);
    }
}
