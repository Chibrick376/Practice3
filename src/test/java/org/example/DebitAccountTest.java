package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebitAccountTest
{

    @Test
    void initialBalanceIsStoredCorrectly()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000001"), "Алиса", 5_000);
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void depositIncreasesBalance()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000002"), "Борис", 1_000);
        account.deposit(500);
        assertEquals(1_500, account.getBalance());
    }

    @Test
    void negativeDepositThrowsException()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000003"), "Виктор", 1_000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(-100)
        );
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void zeroDepositThrowsException()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000004"), "Галина", 1_000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(0)
        );
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void withdrawDecreasesBalance()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000005"), "Дарья", 10_000);

        assertDoesNotThrow(() -> account.withdraw(3_000));
        assertEquals(7_000, account.getBalance());
    }

    @Test
    void cannotWithdrawMoreThanBalance()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000006"), "Егор", 1_000);

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(3_000)
        );
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void zeroWithdrawIsRejected()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000007"), "Жанна", 5_000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(0)
        );
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void negativeWithdrawIsRejected()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000008"), "Иван", 5_000);

        assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(-100)
        );
        assertEquals(5_000, account.getBalance());
    }
}