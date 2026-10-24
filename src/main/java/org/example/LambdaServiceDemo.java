package org.example;

public class LambdaServiceDemo
{

    public static void main(String[] args)
    {
        CommissionPolicy noCommission = amount -> 0.0;
        CommissionPolicy onePercent = amount -> amount * 0.01;
        NotificationService console = System.out::println;

        TransferService noCommService = new TransferService(
                noCommission,
                console,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        TransferService percentService = new TransferService(
                onePercent,
                console,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        BankAccount from = new DebitAccount(new AccountNumber("0000000001"), "Ivan", 10_000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000002"), "Petr", 0);

        System.out.println("=== Без комиссии ===");
        noCommService.transfer(from, to, 1_000);

        System.out.println("=== С комиссией 1% ===");
        percentService.transfer(from, to, 1_000);
    }
}