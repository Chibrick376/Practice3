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
        boolean result = account.withdraw(8_500);
        assertTrue(result);
        assertEquals(1_500, account.getBalance());
    }

    @Test
    void cannotWithdrawBelowMinimumBalance()
    {
        SavingsAccount account = new SavingsAccount(
                new AccountNumber("0000000012"), "Борис", 10_000, 1_000);
        boolean result = account.withdraw(9_500);
        assertFalse(result);
        assertEquals(10_000, account.getBalance());
    }

    @Test
    void balanceDoesNotChangeOnFailedWithdraw()
    {
        SavingsAccount account = new SavingsAccount(
                new AccountNumber("0000000013"), "Виктор", 10_000, 1_000);
        double before = account.getBalance();
        boolean result = account.withdraw(9_999);
        assertFalse(result);
        assertEquals(before, account.getBalance());
    }
}