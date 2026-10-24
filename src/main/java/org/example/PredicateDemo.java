package org.example;

import java.util.List;
import java.util.function.Predicate;

public class PredicateDemo
{

    public static void main(String[] args)
    {
        BlockedAccountRegistry blocked = new BlockedAccountRegistry();
        BankService bank = new BankService(
                new InMemoryRepository<>(),
                new TransferService(
                        new NoCommission(),
                        new ConsoleNotificationService(), // <-- Используем класс из main
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

        blocked.block(acc1.getNumber());

        Predicate<BankAccount> rich = a -> a.getBalance() >= 100_000;
        Predicate<BankAccount> notBlocked = a -> !blocked.isBlocked(a.getId());
        Predicate<BankAccount> debit = DebitAccount.class::isInstance;
        Predicate<BankAccount> credit = CreditAccount.class::isInstance;

        System.out.println("=== Богатые И незаблокированные ===");
        List<BankAccount> richAndNotBlocked = bank.findAccounts(rich.and(notBlocked));
        richAndNotBlocked.forEach(System.out::println);

        System.out.println("=== Не DebitAccount ===");
        List<BankAccount> notDebit = bank.findAccounts(debit.negate());
        notDebit.forEach(System.out::println);

        System.out.println("=== Богатые ИЛИ CreditAccount ===");
        List<BankAccount> richOrCredit = bank.findAccounts(rich.or(credit));
        richOrCredit.forEach(System.out::println);
    }
}