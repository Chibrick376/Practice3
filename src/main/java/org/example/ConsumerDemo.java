package org.example;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConsumerDemo
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

        List<AccountSummary> summaries = CollectionFunctions.map(accounts, toSummary);

        System.out.println("=== Через лямбду ===");
        Consumer<AccountSummary> printer = summary -> System.out.println(summary);
        CollectionFunctions.forEach(summaries, printer);

        System.out.println("=== Через method reference ===");
        CollectionFunctions.forEach(summaries, System.out::println);
    }
}
