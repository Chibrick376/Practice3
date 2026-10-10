package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRepositoryTest
{

    private DebitAccount account(String number, String owner, double balance)
    {
        return new DebitAccount(new AccountNumber(number), owner, balance);
    }

    @Test
    void saveAndFindById()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        AccountNumber id = new AccountNumber("0000000001");
        BankAccount account = account("0000000001", "Ivan", 1_000);

        repository.save(account);

        assertSame(account, repository.findById(id));
    }

    @Test
    void existsByIdForExistingAndMissing()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        repository.save(account("0000000002", "Ivan", 1_000));

        assertTrue(repository.existsById(new AccountNumber("0000000002")));
        assertFalse(repository.existsById(new AccountNumber("0000000003")));
    }

    @Test
    void sizeGrowsAfterNewSave()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        repository.save(account("0000000004", "Ivan", 1_000));
        repository.save(account("0000000005", "Petr", 2_000));

        assertEquals(2, repository.size());
    }

    @Test
    void savingSameIdReplacesOldValue()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        AccountNumber id = new AccountNumber("0000000001");
        BankAccount oldAccount = new DebitAccount(id, "Ivan", 1000);
        BankAccount newAccount = new DebitAccount(id, "Petr", 5000);

        repository.save(oldAccount);
        repository.save(newAccount);

        assertEquals(1, repository.size());
        assertSame(newAccount, repository.findById(id));
    }

    @Test
    void deleteByIdRemovesExisting()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        repository.save(account("0000000006", "Ivan", 1_000));

        assertTrue(repository.deleteById(new AccountNumber("0000000006")));
        assertFalse(repository.existsById(new AccountNumber("0000000006")));
        assertEquals(0, repository.size());
    }

    @Test
    void deleteByIdReturnsFalseForMissing()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();

        assertFalse(repository.deleteById(new AccountNumber("0000000007")));
    }

    @Test
    void findAllReturnsAllSaved()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();
        repository.save(account("0000000008", "Ivan", 1_000));
        repository.save(account("0000000009", "Petr", 2_000));

        List<BankAccount> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void saveNullIsRejected()
    {
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );
        assertEquals(0, repository.size());
    }
}