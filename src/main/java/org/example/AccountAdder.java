package org.example;

import java.util.List;

public final class AccountAdder {

    private AccountAdder() {
    }

    public static void addDemoDebitAccounts(List<? super DebitAccount> target) {
        target.add(new DebitAccount(
                new AccountNumber("0000000901"), "Demo1", 1_000));
        target.add(new DebitAccount(
                new AccountNumber("0000000902"), "Demo2", 2_000));
    }
}