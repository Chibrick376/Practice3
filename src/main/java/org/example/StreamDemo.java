package org.example;

import java.util.ArrayList;
import java.util.List;

public class StreamDemo
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

        System.out.println("=== Цикл ===");
        List<BankAccount> resultByLoop = new ArrayList<>();
        for (BankAccount account : accounts)
        {
            if (account.getBalance() >= 100_000)
            {
                resultByLoop.add(account);
            }
        }
        resultByLoop.forEach(System.out::println);

        System.out.println("=== Stream API ===");
        List<BankAccount> resultByStream = accounts.stream()
                .filter(a -> a.getBalance() >= 100_000)
                .toList();
        resultByStream.forEach(System.out::println);
    }
}
