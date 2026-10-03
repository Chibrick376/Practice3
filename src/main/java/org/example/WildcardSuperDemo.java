package org.example;

import java.util.ArrayList;
import java.util.List;

public class WildcardSuperDemo {

    public static void main(String[] args) {
        List<DebitAccount> debits = new ArrayList<>();
        List<BankAccount> accounts = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        AccountAdder.addDemoDebitAccounts(debits);
        AccountAdder.addDemoDebitAccounts(accounts);
        AccountAdder.addDemoDebitAccounts(objects);

        System.out.println("debits.size()   = " + debits.size());
        System.out.println("accounts.size() = " + accounts.size());
        System.out.println("objects.size()  = " + objects.size());
    }
}