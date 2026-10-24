package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaServiceTest
{

    @Test
    void transferWithNoCommissionLambda()
    {
        TransferService service = new TransferService(
                amount -> 0.0,
                message -> {},
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        BankAccount from = new DebitAccount(new AccountNumber("0000000001"), "Ivan", 5_000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000002"), "Petr", 0);

        service.transfer(from, to, 1_000);

        assertEquals(4_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    @Test
    void transferWithOnePercentCommissionLambda()
    {
        TransferService service = new TransferService(
                amount -> amount * 0.01,
                message -> {},
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        BankAccount from = new DebitAccount(new AccountNumber("0000000003"), "Ivan", 5_000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000004"), "Petr", 0);

        service.transfer(from, to, 1_000);

        assertEquals(3_990, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    @Test
    void notificationThroughLambda()
    {
        StringBuilder log = new StringBuilder();
        TransferService service = new TransferService(
                amount -> 0.0,
                message -> log.append(message),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        BankAccount from = new DebitAccount(new AccountNumber("0000000005"), "Ivan", 5_000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000006"), "Petr", 0);

        service.transfer(from, to, 1_000);

        assertTrue(log.toString().contains("Transfer 1000.0 completed"));
    }

    @Test
    void notificationThroughMethodReference()
    {
        StringBuilder log = new StringBuilder();

        // Используем лямбду как "мост", чтобы не писать класс.
        // В реальном коде можно написать System.out::println, если нужно в консоль.
        TransferService service = new TransferService(
                amount -> 0.0,
                log::append,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );

        BankAccount from = new DebitAccount(new AccountNumber("0000000007"), "Ivan", 5_000);
        BankAccount to = new DebitAccount(new AccountNumber("0000000008"), "Petr", 0);

        service.transfer(from, to, 1_000);

        assertTrue(log.toString().contains("Transfer 1000.0 completed"));
    }
}