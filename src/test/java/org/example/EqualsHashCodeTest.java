package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EqualsHashCodeTest
{

    @Test
    void accountsWithSameNumberAreEqual()
    {
        DebitAccount a = new DebitAccount(
                new AccountNumber("1234567890"), "Ivan", 1_000);
        DebitAccount b = new DebitAccount(
                new AccountNumber("1234567890"), "Petr", 5_000);

        assertEquals(a, b);
    }

    @Test
    void accountsWithDifferentNumbersAreNotEqual()
    {
        DebitAccount a = new DebitAccount(
                new AccountNumber("1234567890"), "Ivan", 1_000);
        DebitAccount b = new DebitAccount(
                new AccountNumber("1234567891"), "Ivan", 1_000);

        assertNotEquals(a, b);
    }

    @Test
    void accountEqualsItself()
    {
        DebitAccount a = new DebitAccount(
                new AccountNumber("1234567890"), "Ivan", 1_000);

        assertEquals(a, a);
    }

    @Test
    void accountDoesNotEqualNull()
    {
        DebitAccount a = new DebitAccount(
                new AccountNumber("1234567890"), "Ivan", 1_000);

        assertNotEquals(null, a);
    }

    @Test
    void equalAccountsHaveSameHashCode()
    {
        DebitAccount a = new DebitAccount(
                new AccountNumber("1234567890"), "Ivan", 1_000);
        DebitAccount b = new DebitAccount(
                new AccountNumber("1234567890"), "Petr", 5_000);

        assertEquals(a.hashCode(), b.hashCode());
    }
}