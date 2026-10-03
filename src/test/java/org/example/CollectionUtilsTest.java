package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilsTest {

    @Test
    void copiesDebitAccountsToBankAccountList() {
        List<DebitAccount> source = new ArrayList<>();
        source.add(new DebitAccount(new AccountNumber("0000001001"), "A", 1_000));
        source.add(new DebitAccount(new AccountNumber("0000001002"), "B", 2_000));

        List<BankAccount> target = new ArrayList<>();
        CollectionUtils.copy(source, target);

        assertEquals(2, target.size());
        assertEquals(source.get(0), target.get(0));
        assertEquals(source.get(1), target.get(1));
    }

    @Test
    void copiesDebitAccountsToObjectList() {
        List<DebitAccount> source = new ArrayList<>();
        source.add(new DebitAccount(new AccountNumber("0000001003"), "C", 500));

        List<Object> target = new ArrayList<>();
        CollectionUtils.copy(source, target);

        assertEquals(1, target.size());
        assertTrue(target.get(0) instanceof DebitAccount);
    }

    @Test
    void copiesBankAccountsToBankAccountList() {
        List<BankAccount> source = new ArrayList<>();
        source.add(new DebitAccount(new AccountNumber("0000001004"), "D", 700));
        source.add(new SavingsAccount(new AccountNumber("0000001005"), "E", 1_000, 500));

        List<BankAccount> target = new ArrayList<>();
        CollectionUtils.copy(source, target);

        assertEquals(2, target.size());
        assertEquals(source, target);
    }

    @Test
    void preservesOrder() {
        List<DebitAccount> source = new ArrayList<>();
        source.add(new DebitAccount(new AccountNumber("0000001006"), "First", 1));
        source.add(new DebitAccount(new AccountNumber("0000001007"), "Second", 2));
        source.add(new DebitAccount(new AccountNumber("0000001008"), "Third", 3));

        List<BankAccount> target = new ArrayList<>();
        CollectionUtils.copy(source, target);

        assertEquals("First", target.get(0).getOwner());
        assertEquals("Second", target.get(1).getOwner());
        assertEquals("Third", target.get(2).getOwner());
    }

    @Test
    void copyFromEmptySourceDoesNothing() {
        List<DebitAccount> source = new ArrayList<>();
        List<BankAccount> target = new ArrayList<>();

        CollectionUtils.copy(source, target);

        assertTrue(target.isEmpty());
    }
}