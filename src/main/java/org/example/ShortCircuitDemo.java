package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShortCircuitDemo
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

        DebitAccount rich = new DebitAccount(new AccountNumber("0000000001"), "A", 150_000);
        DebitAccount poor1 = new DebitAccount(new AccountNumber("0000000002"), "B", 50_000);
        DebitAccount poor2 = new DebitAccount(new AccountNumber("0000000003"), "C", 10_000);

        bank.openAccount(rich);
        bank.openAccount(poor1);
        bank.openAccount(poor2);

        blocked.block(poor1.getNumber());

        System.out.println("Есть заблокированные счета: " + bank.hasBlockedAccounts());
        System.out.println("Все балансы положительные: " + bank.allAccountsPositive());
        System.out.println("Нет кредитных счетов: " + bank.noCreditAccounts());

        List<BankAccount> accounts = bank.findAccounts(account -> true);

        System.out.println("=== Эксперимент: anyMatch останавливается до конца списка ===");
        AtomicInteger calls = new AtomicInteger();

        boolean foundRichFirst = accounts.stream()
                .anyMatch(a ->
                {
                    calls.incrementAndGet();
                    return a.getBalance() >= 100_000;
                });
        System.out.println("Богатый первый в списке: " + foundRichFirst
                + ", вызовов предиката: " + calls.get()
                + " из " + accounts.size());

        calls.set(0);
        boolean foundRichLast = accounts.stream()
                .sorted(Comparator.comparingDouble(BankAccount::getBalance))
                .anyMatch(a ->
                {
                    calls.incrementAndGet();
                    return a.getBalance() >= 100_000;
                });
        System.out.println("Богатый последний в списке: " + foundRichLast
                + ", вызовов предиката: " + calls.get()
                + " из " + accounts.size());
    }
}
