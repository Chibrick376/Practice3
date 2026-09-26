package org.example;

public class Demo
{

    public static void main(String[] args)
    {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService()
        );

        BankAccount from = new DebitAccount(
                new AccountNumber("0000000001"), "Ivan", 10_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000002"), "Petr", 2_000);

        System.out.println("=== Успешный перевод ===");
        try
        {
            transferService.transfer(from, to, 5_000);
            System.out.println("Transfer completed");
        }
        catch (InsufficientFundsException e)
        {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        System.out.println("from: " + from);
        System.out.println("to:   " + to);

        System.out.println();
        System.out.println("=== Неуспешный перевод (не хватает денег) ===");
        try
        {
            transferService.transfer(from, to, 5_000);
            System.out.println("Transfer completed");
        }
        catch (InsufficientFundsException e)
        {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        System.out.println("from: " + from);
        System.out.println("to:   " + to);

        System.out.println();
        System.out.println("=== Перевод с превышением лимита ===");
        try
        {
            transferService.transfer(from, to, 100_000);
            System.out.println("Transfer completed");
        }
        catch (TransferLimitExceededException e)
        {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        System.out.println("from: " + from);
        System.out.println("to:   " + to);

        System.out.println();
        System.out.println("=== Перевод на тот же счёт ===");
        try
        {
            transferService.transfer(from, from, 100);
            System.out.println("Transfer completed");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        System.out.println("from: " + from);

        System.out.println();
        System.out.println("=== Перевод с комиссией 1% ===");
        TransferService withCommission = new TransferService(
                new PercentCommission(1),
                new ConsoleNotificationService()
        );
        BankAccount from2 = new DebitAccount(
                new AccountNumber("0000000003"), "Anna", 20_000);
        BankAccount to2 = new DebitAccount(
                new AccountNumber("0000000004"), "Olga", 1_000);

        try
        {
            withCommission.transfer(from2, to2, 10_000);
            System.out.println("Transfer completed");
        }
        catch (InsufficientFundsException e)
        {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        System.out.println("from: " + from2);
        System.out.println("to:   " + to2);
    }
}