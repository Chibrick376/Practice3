package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LambdaTest
{

    @Test
    void sortsAccountsByBalanceUsingLambda()
    {
        List<BankAccount> accounts = new ArrayList<>();
        BankAccount a = new DebitAccount(new AccountNumber("0000000001"), "A", 5000);
        BankAccount b = new DebitAccount(new AccountNumber("0000000002"), "B", 1000);
        BankAccount c = new DebitAccount(new AccountNumber("0000000003"), "C", 3000);

        accounts.add(a);
        accounts.add(b);
        accounts.add(c);

        Comparator<BankAccount> byBalance = (acc1, acc2) -> Double.compare(acc1.getBalance(), acc2.getBalance());
        accounts.sort(byBalance);

        assertSame(b, accounts.get(0));
        assertSame(c, accounts.get(1));
        assertSame(a, accounts.get(2));
    }

    @Test
    void sortsAccountsByBalanceUsingMethodReference()
    {
        List<BankAccount> accounts = new ArrayList<>();
        BankAccount a = new DebitAccount(new AccountNumber("0000000001"), "A", 5000);
        BankAccount b = new DebitAccount(new AccountNumber("0000000002"), "B", 1000);

        accounts.add(a);
        accounts.add(b);

        Comparator<BankAccount> byBalance = Comparator.comparingDouble(BankAccount::getBalance);
        accounts.sort(byBalance);

        assertSame(b, accounts.get(0));
        assertSame(a, accounts.get(1));
    }
}