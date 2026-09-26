package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest
{

    @Test
    void canWithdrawWhenMinimumBalanceIsKept()
    {
        SavingsAccount account = new SavingsAccount(
                new AccountNumber("0000000011"), "Алиса", 10_000, 1_000);

        assertDoesNotThrow(() -> account.withdraw(8_500));
        assertEquals(1_500, account.getBalance());
    }

    @Test
    void cannotWithdrawBelowMinimumBalance()
    {
        SavingsAccount account = new SavingsAccount(
                new AccountNumber("0000000012"), "Борис", 10_000, 1_000);

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(9_500)
        );
        assertEquals(10_000, account.getBalance());
    }

    @Test
    void balanceDoesNotChangeOnFailedWithdraw()
    {
        SavingsAccount account = new SavingsAccount(
                new AccountNumber("0000000013"), "Виктор", 10_000, 1_000);
        double before = account.getBalance();

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(9_999)
        );
        assertEquals(before, account.getBalance());
    }
}