package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditAccountTest
{

    @Test
    void accountCanGoNegative()
    {
        CreditAccount account = new CreditAccount(
                new AccountNumber("0000000021"), "Алиса", 1_000, 5_000);

        assertDoesNotThrow(() -> account.withdraw(4_000));
        assertEquals(-3_000, account.getBalance());
    }

    @Test
    void canUseFullCreditLimit()
    {
        CreditAccount account = new CreditAccount(
                new AccountNumber("0000000022"), "Борис", 0, 5_000);

        assertDoesNotThrow(() -> account.withdraw(5_000));
        assertEquals(-5_000, account.getBalance());
    }

    @Test
    void cannotExceedCreditLimit()
    {
        CreditAccount account = new CreditAccount(
                new AccountNumber("0000000023"), "Виктор", 0, 5_000);

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(5_001)
        );
        assertEquals(0, account.getBalance());
    }

    @Test
    void balanceDoesNotChangeOnFailedWithdraw()
    {
        CreditAccount account = new CreditAccount(
                new AccountNumber("0000000024"), "Галина", 1_000, 5_000);
        double before = account.getBalance();

        assertThrows(
                InsufficientFundsException.class,
                () -> account.withdraw(10_000)
        );
        assertEquals(before, account.getBalance());
    }
}