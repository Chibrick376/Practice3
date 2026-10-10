package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountUtilsRichestTest
{

    @Test
    void returnsRichestDebitAccount()
    {
        List<DebitAccount> list = new ArrayList<>();
        list.add(new DebitAccount(new AccountNumber("0000002001"), "A", 1_000));
        list.add(new DebitAccount(new AccountNumber("0000002002"), "B", 5_000));
        list.add(new DebitAccount(new AccountNumber("0000002003"), "C", 3_000));

        DebitAccount richest = AccountUtils.richest(list);
        assertEquals("B", richest.getOwner());
        assertEquals(5_000, richest.getBalance());
    }

    @Test
    void returnsRichestBankAccount()
    {
        List<BankAccount> list = new ArrayList<>();
        list.add(new DebitAccount(new AccountNumber("0000002004"), "D", 2_000));
        list.add(new SavingsAccount(new AccountNumber("0000002005"), "E", 9_000, 1_000));

        BankAccount richest = AccountUtils.richest(list);
        assertEquals("E", richest.getOwner());
    }

    @Test
    void singleElementIsReturned()
    {
        List<DebitAccount> list = new ArrayList<>();
        DebitAccount only = new DebitAccount(new AccountNumber("0000002006"), "Only", 100);
        list.add(only);

        assertSame(only, AccountUtils.richest(list));
    }

    @Test
    void emptyListIsRejected()
    {
        List<DebitAccount> list = new ArrayList<>();
        assertThrows(
                IllegalArgumentException.class,
                () -> AccountUtils.richest(list)
        );
    }

    @Test
    void nullListIsRejected()
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> AccountUtils.richest(null)
        );
    }

    @Test
    void firstMaxWinsOnTie()
    {
        List<DebitAccount> list = new ArrayList<>();
        DebitAccount first = new DebitAccount(new AccountNumber("0000002007"), "First", 5_000);
        DebitAccount second = new DebitAccount(new AccountNumber("0000002008"), "Second", 5_000);
        list.add(first);
        list.add(second);

        assertSame(first, AccountUtils.richest(list));
    }
}