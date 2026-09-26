package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EqualsHashCodeTest
{

    @Test
    void sameNumberMeansEqual()
    {
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);
        DebitAccount b = new DebitAccount("001", "Petr", 5_000);

        assertEquals(a, b);
    }

    @Test
    void differentNumberMeansNotEqual()
    {
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);
        DebitAccount b = new DebitAccount("002", "Ivan", 1_000);

        assertNotEquals(a, b);
    }

    @Test
    void balanceDoesNotAffectEquality()
    {
        DebitAccount a = new DebitAccount("001", "Ivan", 100);
        DebitAccount b = new DebitAccount("001", "Ivan", 999_999);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void ownerDoesNotAffectEquality()
    {
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);
        DebitAccount b = new DebitAccount("001", "Anna", 1_000);

        assertEquals(a, b);
    }

    @Test
    void differentAccountTypesAreNotEqual()
    {
        DebitAccount debit = new DebitAccount("001", "Ivan", 1_000);
        SavingsAccount savings = new SavingsAccount("001", "Ivan", 1_000, 100);

        assertNotEquals(debit, savings);
    }

    @Test
    void equalsWorksThroughBaseClassReference()
    {
        BankAccount a = new DebitAccount("001", "Ivan", 1_000);
        BankAccount b = new DebitAccount("001", "Petr", 2_000);

        assertEquals(a, b);
    }

    @Test
    void equalsIsReflexive()
    {
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);

        assertEquals(a, a);
    }

    @Test
    void equalsReturnsFalseForNull()
    {
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);

        assertNotEquals(null, a);
    }

    @Test
    void hashCodeIsConsistentForEqualObjects()
    {
        DebitAccount a = new DebitAccount("001", "Ivan", 1_000);
        DebitAccount b = new DebitAccount("001", "Petr", 500);

        assertEquals(a.hashCode(), b.hashCode());
    }
}