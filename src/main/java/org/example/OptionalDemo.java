package org.example;

import java.util.Optional;

public class OptionalDemo
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

        bank.openAccount(acc1);
        bank.openAccount(acc2);

        System.out.println("=== orElseThrow: богатый счёт есть ===");
        BankAccount account = bank.findFirstRichAccount(100_000)
                .orElseThrow(() ->
                        new IllegalStateException("No rich account"));
        System.out.println(account);

        System.out.println("=== map + orElse: богатый счёт есть ===");
        String label = bank.findFirstRichAccount(100_000)
                .map(a -> a.getId().value())
                .orElse("not found");
        System.out.println(label);

        System.out.println("=== map + orElse: богатых нет ===");
        String noRich = bank.findFirstRichAccount(1_000_000)
                .map(a -> a.getId().value())
                .orElse("not found");
        System.out.println(noRich);

        try
        {
            bank.findFirstRichAccount(1_000_000)
                    .orElseThrow(() ->
                            new IllegalStateException("No rich account"));
        }
        catch (IllegalStateException e)
        {
            System.out.println("=== orElseThrow бросил исключение: " + e.getMessage());
        }

        Optional<BankAccount> empty = bank.findFirstRichAccount(1_000_000);
        System.out.println("=== isPresent: " + empty.isPresent());
    }
}
