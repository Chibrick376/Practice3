package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountAdderTest {

    @Test
    void addsToDebitAccountList() {
        List<DebitAccount> list = new ArrayList<>();
        AccountAdder.addDemoDebitAccounts(list);
        assertEquals(2, list.size());
        assertTrue(list.get(0) instanceof DebitAccount);
    }

    @Test
    void addsToBankAccountList() {
        List<BankAccount> list = new ArrayList<>();
        AccountAdder.addDemoDebitAccounts(list);
        assertEquals(2, list.size());
        assertEquals(1_000, list.get(0).getBalance());
        assertEquals(2_000, list.get(1).getBalance());
    }

    @Test
    void addsToObjectList() {
        List<Object> list = new ArrayList<>();
        AccountAdder.addDemoDebitAccounts(list);
        assertEquals(2, list.size());
        assertTrue(list.get(0) instanceof DebitAccount);
    }

    @Test
    void readingFromSuperOnlyGivesObject() {
        List<Object> list = new ArrayList<>();
        AccountAdder.addDemoDebitAccounts(list);

        Object value = list.get(0);
        assertNotNull(value);

        DebitAccount debit = (DebitAccount) value;
        assertEquals("Demo1", debit.getOwner());
    }
}