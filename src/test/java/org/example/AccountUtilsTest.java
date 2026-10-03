package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountUtilsTest {

    @Test
    void totalBalanceForBankAccountList() {
        List<BankAccount> list = new ArrayList<>();
        list.add(new DebitAccount(new AccountNumber("0000000001"), "A", 1_000));
        list.add(new DebitAccount(new AccountNumber("0000000002"), "B", 2_000));

        assertEquals(3_000, AccountUtils.totalBalance(list));
    }

    @Test
    void totalBalanceForDebitAccountList() {
        List<DebitAccount> list = new ArrayList<>();
        list.add(new DebitAccount(new AccountNumber("0000000003"), "C", 500));
        list.add(new DebitAccount(new AccountNumber("0000000004"), "D", 700));

        assertEquals(1_200, AccountUtils.totalBalance(list));
    }

    @Test
    void totalBalanceForSavingsAccountList() {
        List<SavingsAccount> list = new ArrayList<>();
        list.add(new SavingsAccount(new AccountNumber("0000000005"), "E", 5_000, 1_000));
        list.add(new SavingsAccount(new AccountNumber("0000000006"), "F", 3_000, 1_000));

        assertEquals(8_000, AccountUtils.totalBalance(list));
    }

    @Test
    void totalBalanceForCreditAccountList() {
        List<CreditAccount> list = new ArrayList<>();
        CreditAccount c = new CreditAccount(new AccountNumber("0000000007"), "G", 0, 5_000);
        c.withdraw(1_000);
        list.add(c);

        assertEquals(-1_000, AccountUtils.totalBalance(list));
    }

    @Test
    void totalBalanceForEmptyListIsZero() {
        List<DebitAccount> list = new ArrayList<>();
        assertEquals(0, AccountUtils.totalBalance(list));
    }
}