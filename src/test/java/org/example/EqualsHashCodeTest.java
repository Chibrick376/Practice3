package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EqualsHashCodeTest
{

    @Test
    void accountsWithSameNumberAreEqual()
    {
        // Arrange
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);
        DebitAccount b = new DebitAccount("001", "Petr", 5_000);

        // Act + Assert
        assertEquals(a, b);
    }

    @Test
    void accountsWithDifferentNumbersAreNotEqual()
    {
        // Arrange
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);
        DebitAccount b = new DebitAccount("002", "Ivan", 1_000);

        // Act + Assert
        assertNotEquals(a, b);
    }

    @Test
    void accountEqualsItself()
    {
        // Arrange
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);

        // Act + Assert
        assertEquals(a, a);
    }

    @Test
    void accountDoesNotEqualNull()
    {
        // Arrange
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);

        // Act + Assert
        assertNotEquals(null, a);
    }

    @Test
    void equalAccountsHaveSameHashCode()
    {
        // Arrange
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);
        DebitAccount b = new DebitAccount("001", "Petr", 5_000);

        // Act + Assert
        assertEquals(a.hashCode(), b.hashCode());
    }
}