package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для CreditAccount.
 * Покрывают случаи из задания:
 *  - уход в отрицательный баланс
 *  - использование кредитного лимита
 *  - отказ при превышении кредитного лимита
 *  - баланс не меняется при неуспешном снятии
 */
class CreditAccountTest
{

    // ---------- 1. Счёт может уйти в отрицательный баланс ----------

    @Test
    void accountCanGoNegative()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-001", "Алиса", 1_000, 5_000);

        // Act
        boolean result = account.withdraw(4_000);

        // Assert
        assertTrue(result);
        assertEquals(-3_000, account.getBalance());
    }

    // ---------- 2. Можно использовать кредитный лимит ----------

    @Test
    void canUseFullCreditLimit()
    {
        // Arrange — баланс 0, лимит 5000
        CreditAccount account = new CreditAccount("C-002", "Борис", 0, 5_000);

        // Act — снимаем ровно на всю величину лимита
        boolean result = account.withdraw(5_000);

        // Assert
        assertTrue(result);
        assertEquals(-5_000, account.getBalance());
    }

    // ---------- 3. Нельзя превысить кредитный лимит ----------

    @Test
    void cannotExceedCreditLimit()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-003", "Виктор", 0, 5_000);

        // Act — пытаемся снять на 1 больше лимита
        boolean result = account.withdraw(5_001);

        // Assert
        assertFalse(result);
        assertEquals(0, account.getBalance());
    }

    // ---------- 4. При неуспешном снятии баланс не изменяется ----------

    @Test
    void balanceDoesNotChangeOnFailedWithdraw()
    {
        // Arrange
        CreditAccount account = new CreditAccount("C-004", "Галина", 1_000, 5_000);
        double balanceBefore = account.getBalance();

        // Act — заведомо неуспешное снятие
        boolean result = account.withdraw(10_000);

        // Assert
        assertFalse(result);
        assertEquals(balanceBefore, account.getBalance());
    }
}