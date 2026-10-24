package org.example;

import java.util.List;
import java.util.function.Predicate;

/**
 * Сервис для управления счетами и переводами.
 */
public class BankService
{

    private final Repository<AccountNumber, BankAccount> accounts;
    private final TransferService transferService;

    public BankService(
            Repository<AccountNumber, BankAccount> accounts,
            TransferService transferService)
    {
        if (accounts == null)
            throw new IllegalArgumentException("Repository must not be null");
        if (transferService == null)
            throw new IllegalArgumentException("TransferService must not be null");

        this.accounts = accounts;
        this.transferService = transferService;
    }

    public void openAccount(BankAccount account)
    {
        if (account == null)
            throw new IllegalArgumentException("Account must not be null");

        accounts.save(account);
    }

    public BankAccount findAccount(AccountNumber number)
    {
        return accounts.findById(number);
    }

    public boolean closeAccount(AccountNumber number)
    {
        return accounts.deleteById(number);
    }

    public void transfer(
            AccountNumber fromNumber,
            AccountNumber toNumber,
            double amount)
    {
        BankAccount from = accounts.findById(fromNumber);
        if (from == null)
            throw new IllegalArgumentException("Source account not found");

        BankAccount to = accounts.findById(toNumber);
        if (to == null)
            throw new IllegalArgumentException("Destination account not found");

        transferService.transfer(from, to, amount);
    }

    public List<BankAccount> findAccounts(Predicate<? super BankAccount> condition)
    {
        List<BankAccount> allAccounts = accounts.findAll();
        return CollectionFunctions.filter(allAccounts, condition);
    }
}