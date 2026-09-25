package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для DebitAccount.
 * Покрывают все случаи из задания:
 *  - начальный баланс
 *  - deposit (успешный, нулевой, отрицательный)
 *  - withdraw (успешный, превышение, нулевой, отрицательный)
 */
class DebitAccountTest
{

    // ---------- 1. Начальный баланс сохраняется ----------

    @Test
    void initialBalanceIsStoredCorrectly()
    {
        // Arrange + Act
        DebitAccount account = new DebitAccount("D-001", "Алиса", 5_000);

        // Assert
        assertEquals(5_000, account.getBalance());
    }

    // ---------- 2. deposit увеличивает баланс ----------

    @Test
    void depositIncreasesBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-002", "Борис", 1_000);

        // Act
        account.deposit(500);

        // Assert
        assertEquals(1_500, account.getBalance());
    }

    // ---------- 3. Нулевой deposit не изменяет баланс ----------

    @Test
    void zeroDepositDoesNotChangeBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-003", "Виктор", 1_000);

        // Act
        account.deposit(0);

        // Assert
        assertEquals(1_000, account.getBalance());
    }

    // ---------- 4. Отрицательный deposit не изменяет баланс ----------

    @Test
    void negativeDepositDoesNotChangeBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-004", "Галина", 1_000);

        // Act
        account.deposit(-500);

        // Assert
        assertEquals(1_000, account.getBalance());
    }

    // ---------- 5. withdraw уменьшает баланс ----------

    @Test
    void withdrawDecreasesBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-005", "Дарья", 10_000);

        // Act
        boolean result = account.withdraw(3_000);

        // Assert
        assertTrue(result);
        assertEquals(7_000, account.getBalance());
    }

    // ---------- 6. Нельзя снять больше остатка ----------

    @Test
    void cannotWithdrawMoreThanBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-006", "Егор", 1_000);

        // Act
        boolean result = account.withdraw(3_000);

        // Assert
        assertFalse(result);
        assertEquals(1_000, account.getBalance());
    }

    // ---------- 7. Нулевое снятие запрещено ----------

    @Test
    void zeroWithdrawIsRejected()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-007", "Жанна", 5_000);

        // Act
        boolean result = account.withdraw(0);

        // Assert
        assertFalse(result);
        assertEquals(5_000, account.getBalance());
    }

    // ---------- 8. Отрицательное снятие запрещено ----------

    @Test
    void negativeWithdrawIsRejected()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-008", "Иван", 5_000);

        // Act
        boolean result = account.withdraw(-100);

        // Assert
        assertFalse(result);
        assertEquals(5_000, account.getBalance());
    }
}