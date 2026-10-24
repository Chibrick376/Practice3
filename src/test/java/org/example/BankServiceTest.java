package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest
{

    private BankService createService()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotificationService(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        return new BankService(repository, transferService);
    }

    @Test
    void openAndFindAccount()
    {
        BankService service = createService();
        BankAccount account = new DebitAccount(
                new AccountNumber("0000000001"), "Ivan", 1_000);

        service.openAccount(account);
        BankAccount found = service.findAccount(new AccountNumber("0000000001"));

        assertSame(account, found);
    }

    @Test
    void openNullAccountIsRejected()
    {
        BankService service = createService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.openAccount(null)
        );
    }

    @Test
    void savingSameIdReplacesOldValue()
    {
        BankService service = createService();
        AccountNumber id = new AccountNumber("0000000002");
        BankAccount oldAccount = new DebitAccount(id, "Ivan", 1_000);
        BankAccount newAccount = new DebitAccount(id, "Petr", 5_000);

        service.openAccount(oldAccount);
        service.openAccount(newAccount);

        assertSame(newAccount, service.findAccount(id));
    }

    @Test
    void closeAccountRemovesIt()
    {
        BankService service = createService();
        AccountNumber id = new AccountNumber("0000000003");
        service.openAccount(new DebitAccount(id, "Ivan", 1_000));

        boolean closed = service.closeAccount(id);

        assertTrue(closed);
        assertNull(service.findAccount(id));
    }

    @Test
    void closeMissingAccountReturnsFalse()
    {
        BankService service = createService();

        boolean closed = service.closeAccount(new AccountNumber("0000000004"));

        assertFalse(closed);
    }
}