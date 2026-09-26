package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToStringTest
{

    @Test
    void debitAccountToStringMatchesExpectedFormat()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000001"), "Ivan", 10_000);
        assertEquals(
                "DebitAccount{number='0000000001', owner='Ivan', balance=10000.0}",
                account.toString()
        );
    }

    @Test
    void savingsAccountToStringUsesCorrectClassName()
    {
        SavingsAccount account = new SavingsAccount(
                new AccountNumber("0000000002"), "Maria", 5_000, 1_000);
        assertTrue(account.toString().startsWith("SavingsAccount{"));
    }

    @Test
    void creditAccountToStringUsesCorrectClassName()
    {
        CreditAccount account = new CreditAccount(
                new AccountNumber("0000000003"), "Petr", 1_000, 5_000);
        assertTrue(account.toString().startsWith("CreditAccount{"));
    }

    @Test
    void toStringReflectsBalanceChanges()
    {
        DebitAccount account = new DebitAccount(
                new AccountNumber("0000000004"), "Anna", 5_000);
        account.deposit(2_500);
        assertTrue(account.toString().contains("balance=7500.0"));
    }

    @Test
    void toStringWorksThroughBaseClassReference()
    {
        BankAccount account = new DebitAccount(
                new AccountNumber("0000000005"), "Olga", 1_000);
        assertTrue(account.toString().startsWith("DebitAccount{"));
    }
}