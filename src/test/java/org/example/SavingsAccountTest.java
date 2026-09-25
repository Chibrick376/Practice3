package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для SavingsAccount.
 * Покрывают случаи из задания:
 *  - снятие с сохранением минимума
 *  - отказ при попытке опустить баланс ниже minimumBalance
 *  - баланс не меняется при неуспешном снятии
 */
class SavingsAccountTest
{

    // ---------- 1. Можно снять, если после операции сохраняется минимум ----------

    @Test
    void canWithdrawWhenMinimumBalanceIsKept()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-001", "Алиса", 10_000, 1_000);

        // Act
        boolean result = account.withdraw(8_500);

        // Assert
        assertTrue(result);
        assertEquals(1_500, account.getBalance());
    }

    // ---------- 2. Нельзя опустить баланс ниже minimumBalance ----------

    @Test
    void cannotWithdrawBelowMinimumBalance()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-002", "Борис", 10_000, 1_000);

        // Act — попытка снять так, что остаток стал бы 500 (ниже 1000)
        boolean result = account.withdraw(9_500);

        // Assert
        assertFalse(result);
        assertEquals(10_000, account.getBalance());
    }

    // ---------- 3. При неуспешном снятии баланс не изменяется ----------

    @Test
    void balanceDoesNotChangeOnFailedWithdraw()
    {
        // Arrange
        SavingsAccount account = new SavingsAccount("S-003", "Виктор", 10_000, 1_000);
        double balanceBefore = account.getBalance();

        // Act — заведомо неуспешное снятие
        boolean result = account.withdraw(9_999);

        // Assert
        assertFalse(result);
        assertEquals(balanceBefore, account.getBalance());
    }
}