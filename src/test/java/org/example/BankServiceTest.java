package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

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

    private BankService createServiceWithBlocked(BlockedAccountRegistry blocked)
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotificationService(),
                blocked,
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

    @Test
    void successfulTransferByNumbers()
    {
        BankService service = createService();
        AccountNumber fromId = new AccountNumber("0000000011");
        AccountNumber toId = new AccountNumber("0000000012");

        service.openAccount(new DebitAccount(fromId, "Ivan", 10_000));
        service.openAccount(new DebitAccount(toId, "Petr", 2_000));

        service.transfer(fromId, toId, 5_000);

        assertEquals(5_000, service.findAccount(fromId).getBalance());
        assertEquals(7_000, service.findAccount(toId).getBalance());
    }

    @Test
    void transferWithMissingSenderThrowsException()
    {
        BankService service = createService();
        AccountNumber toId = new AccountNumber("0000000013");

        service.openAccount(new DebitAccount(toId, "Petr", 2_000));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(
                        new AccountNumber("0000000014"),
                        toId,
                        1_000
                )
        );

        assertEquals(2_000, service.findAccount(toId).getBalance());
    }

    @Test
    void transferWithMissingReceiverThrowsException()
    {
        BankService service = createService();
        AccountNumber fromId = new AccountNumber("0000000015");

        service.openAccount(new DebitAccount(fromId, "Ivan", 10_000));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(
                        fromId,
                        new AccountNumber("0000000016"),
                        1_000
                )
        );

        assertEquals(10_000, service.findAccount(fromId).getBalance());
    }

    @Test
    void transferFromBlockedAccountThrowsException()
    {
        BlockedAccountRegistry blocked = new BlockedAccountRegistry();
        BankService service = createServiceWithBlocked(blocked);

        AccountNumber fromId = new AccountNumber("0000000017");
        AccountNumber toId = new AccountNumber("0000000018");

        service.openAccount(new DebitAccount(fromId, "Ivan", 10_000));
        service.openAccount(new DebitAccount(toId, "Petr", 2_000));
        blocked.block(fromId);

        assertThrows(
                IllegalStateException.class,
                () -> service.transfer(fromId, toId, 5_000)
        );

        assertEquals(10_000, service.findAccount(fromId).getBalance());
        assertEquals(2_000, service.findAccount(toId).getBalance());
    }

    @Test
    void transferWithInsufficientFundsThrowsException()
    {
        BankService service = createService();
        AccountNumber fromId = new AccountNumber("0000000019");
        AccountNumber toId = new AccountNumber("0000000020");

        service.openAccount(new DebitAccount(fromId, "Ivan", 1_000));
        service.openAccount(new DebitAccount(toId, "Petr", 2_000));

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(fromId, toId, 5_000)
        );

        assertEquals(1_000, service.findAccount(fromId).getBalance());
        assertEquals(2_000, service.findAccount(toId).getBalance());
    }

    @Test
    void findAccountsByPredicate()
    {
        BankService service = createService();
        service.openAccount(new DebitAccount(new AccountNumber("0000000021"), "A", 100));
        service.openAccount(new SavingsAccount(new AccountNumber("0000000022"), "B", 200_000, 100));
        service.openAccount(new CreditAccount(new AccountNumber("0000000023"), "C", 0, 1000));

        Predicate<BankAccount> rich = account -> account.getBalance() >= 100_000;
        List<BankAccount> result = service.findAccounts(rich);

        assertEquals(1, result.size());
        assertEquals("B", result.get(0).getOwner());
    }

    @Test
    void findAccountsReturnsNewList()
    {
        BankService service = createService();
        service.openAccount(new DebitAccount(new AccountNumber("0000000024"), "A", 100));

        List<BankAccount> result = service.findAccounts(account -> true);
        result.clear();

        assertEquals(1, service.findAccounts(account -> true).size());
    }
}