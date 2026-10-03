package org.example;

import java.util.ArrayList;
import java.util.List;

public class WildcardDemo {

    public static void main(String[] args) {
        List<BankAccount> base = new ArrayList<>();
        base.add(new DebitAccount(new AccountNumber("0000000001"), "A", 1_000));
        base.add(new SavingsAccount(new AccountNumber("0000000002"), "B", 5_000, 1_000));

        List<DebitAccount> debits = new ArrayList<>();
        debits.add(new DebitAccount(new AccountNumber("0000000003"), "C", 2_000));

        List<SavingsAccount> savings = new ArrayList<>();
        savings.add(new SavingsAccount(new AccountNumber("0000000004"), "D", 7_000, 1_000));

        List<CreditAccount> credits = new ArrayList<>();
        credits.add(new CreditAccount(new AccountNumber("0000000005"), "E", 0, 5_000));
        credits.get(0).withdraw(1_000); // баланс станет -1000

        System.out.println("BankAccount   : " + AccountUtils.totalBalance(base));
        System.out.println("DebitAccount  : " + AccountUtils.totalBalance(debits));
        System.out.println("SavingsAccount: " + AccountUtils.totalBalance(savings));
        System.out.println("CreditAccount : " + AccountUtils.totalBalance(credits));
    }
}