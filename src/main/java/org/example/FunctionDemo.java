package org.example;

import java.util.List;
import java.util.function.Function;

public class FunctionDemo
{

    public static void main(String[] args)
    {
        BlockedAccountRegistry blocked = new BlockedAccountRegistry();
        BankService bank = new BankService(
                new InMemoryRepository<>(),
                new TransferService(
                        new NoCommission(),
                        new ConsoleNotificationService(),
                        blocked,
                        new TransactionJournal()
                )
        );

        DebitAccount acc1 = new DebitAccount(new AccountNumber("0000000001"), "A", 150_000);
        DebitAccount acc2 = new DebitAccount(new AccountNumber("0000000002"), "B", 50_000);
        CreditAccount acc3 = new CreditAccount(new AccountNumber("0000000003"), "C", 200_000, 1000);

        bank.openAccount(acc1);
        bank.openAccount(acc2);
        bank.openAccount(acc3);

        List<BankAccount> accounts = bank.findAccounts(account -> true);

        Function<BankAccount, AccountSummary> toSummary =
                account -> new AccountSummary(
                        account.getId(),
                        account.getClass().getSimpleName(),
                        account.getBalance()
                );

        System.out.println("=== Проекция в AccountSummary ===");
        List<AccountSummary> summaries = CollectionFunctions.map(accounts, toSummary);
        summaries.forEach(System.out::println);

        Function<BankAccount, String> label =
                account -> account.getId() + " : " + account.getBalance();

        System.out.println("=== Строковые метки ===");
        List<String> labels = CollectionFunctions.map(accounts, label);
        labels.forEach(System.out::println);
    }
}
