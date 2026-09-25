package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebitAccountTest
{

    @Test
    void cannotWithdrawMoreThanBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-001", "Алиса", 1_000);

        // Act
        boolean result = account.withdraw(3_000);

        // Assert
        assertFalse(result);
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void canWithdrawExactBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-002", "Борис", 5_000);

        // Act
        boolean result = account.withdraw(5_000);

        // Assert
        assertTrue(result);
        assertEquals(0, account.getBalance());
    }

    @Test
    void canWithdrawPartOfBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-003", "Виктор", 10_000);

        // Act
        boolean result = account.withdraw(3_000);

        // Assert
        assertTrue(result);
        assertEquals(7_000, account.getBalance());
    }

    @Test
    void cannotWithdrawNegativeAmount()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-004", "Галина", 5_000);

        // Act
        boolean result = account.withdraw(-100);

        // Assert
        assertFalse(result);
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void cannotWithdrawZero()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-005", "Дарья", 5_000);

        // Act
        boolean result = account.withdraw(0);

        // Assert
        assertFalse(result);
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void depositIncreasesBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-006", "Егор", 1_000);

        // Act
        account.deposit(500);

        // Assert
        assertEquals(1_500, account.getBalance());
    }

    @Test
    void depositZeroDoesNotChangeBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-007", "Жанна", 1_000);

        // Act
        account.deposit(0);

        // Assert
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void depositNegativeDoesNotChangeBalance()
    {
        // Arrange
        DebitAccount account = new DebitAccount("D-008", "Иван", 1_000);

        // Act
        account.deposit(-500);

        // Assert
        assertEquals(1_000, account.getBalance());
    }

    @Test
    void negativeInitialBalanceIsRejected()
    {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new DebitAccount("D-009", "Ксения", -100));
    }
}