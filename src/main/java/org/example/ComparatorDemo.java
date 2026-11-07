package org.example;

import java.util.Comparator;
import java.util.List;

public class ComparatorDemo
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

        DebitAccount acc1 = new DebitAccount(new AccountNumber("0000000003"), "A", 50_000);
        DebitAccount acc2 = new DebitAccount(new AccountNumber("0000000001"), "B", 150_000);
        CreditAccount acc3 = new CreditAccount(new AccountNumber("0000000002"), "C", 100_000, 1000);

        bank.openAccount(acc1);
        bank.openAccount(acc2);
        bank.openAccount(acc3);

        Comparator<BankAccount> byBalance =
                Comparator.comparingDouble(BankAccount::getBalance);
        Comparator<BankAccount> byBalanceDesc = byBalance.reversed();
        Comparator<BankAccount> byNumber =
                Comparator.comparing(a -> a.getId().value());
        Comparator<BankAccount> byTypeThenBalance =
                Comparator
                        .comparing((BankAccount a) -> a.getClass().getSimpleName())
                        .thenComparingDouble(BankAccount::getBalance);

        System.out.println("=== По балансу по возрастанию ===");
        List<BankAccount> byBalanceAsc = bank.sortedAccounts(byBalance);
        byBalanceAsc.forEach(System.out::println);

        System.out.println("=== По балансу по убыванию ===");
        List<BankAccount> byBalanceDescResult = bank.sortedAccounts(byBalanceDesc);
        byBalanceDescResult.forEach(System.out::println);

        System.out.println("=== По номеру счёта ===");
        List<BankAccount> byNumberResult = bank.sortedAccounts(byNumber);
        byNumberResult.forEach(System.out::println);

        System.out.println("=== По типу счёта, затем по балансу ===");
        List<BankAccount> byTypeThenBalanceResult = bank.sortedAccounts(byTypeThenBalance);
        byTypeThenBalanceResult.forEach(System.out::println);
    }
}
