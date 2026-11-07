package org.example;

import java.util.ArrayList;
import java.util.Comparator;
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

    /**
     * Возвращает счета в заданном порядке.
     * Исходное хранилище не меняется: сортировка выполняется
     * на копии списка.
     */
    public List<BankAccount> sortedAccounts(Comparator<? super BankAccount> comparator)
    {
        if (comparator == null)
            throw new IllegalArgumentException("Comparator must not be null");

        List<BankAccount> result = new ArrayList<>(accounts.findAll());
        result.sort(comparator);
        return result;
    }

    public List<BankAccount> findRichAccounts(double minBalance)
    {
        return accounts.findAll().stream()
                .filter(a -> a.getBalance() >= minBalance)
                .toList();
    }

    public List<BankAccount> findDebitAccounts()
    {
        return accounts.findAll().stream()
                .filter(DebitAccount.class::isInstance)
                .toList();
    }

    public List<BankAccount> findAccountsByOwner(String owner)
    {
        return accounts.findAll().stream()
                .filter(a -> a.getOwner().equals(owner))
                .toList();
    }

    public List<AccountSummary> getAccountSummaries()
    {
        return accounts.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    private AccountSummary toSummary(BankAccount account)
    {
        return new AccountSummary(
                account.getId(),
                account.getClass().getSimpleName(),
                account.getBalance());
    }

    public List<BankAccount> richestFirst()
    {
        return accounts.findAll().stream()
                .sorted(Comparator
                        .comparingDouble(BankAccount::getBalance)
                        .reversed())
                .toList();
    }

    public List<BankAccount> sortedByNumber()
    {
        return accounts.findAll().stream()
                .sorted(Comparator.comparing(a -> a.getId().value()))
                .toList();
    }

    public List<BankAccount> sortedByTypeThenBalance()
    {
        return accounts.findAll().stream()
                .sorted(Comparator
                        .comparing((BankAccount a) -> a.getClass().getSimpleName())
                        .thenComparingDouble(BankAccount::getBalance))
                .toList();
    }

    public List<BankAccount> sortedByOwnerThenNumber()
    {
        return accounts.findAll().stream()
                .sorted(Comparator
                        .comparing(BankAccount::getOwner)
                        .thenComparing(a -> a.getId().value()))
                .toList();
    }

    public boolean hasBlockedAccounts()
    {
        BlockedAccountRegistry blocked = transferService.getBlockedAccounts();
        return accounts.findAll().stream()
                .anyMatch(a -> blocked.isBlocked(a.getId()));
    }

    public boolean allAccountsPositive()
    {
        return accounts.findAll().stream()
                .allMatch(a -> a.getBalance() > 0);
    }

    public boolean noCreditAccounts()
    {
        return accounts.findAll().stream()
                .noneMatch(CreditAccount.class::isInstance);
    }
}