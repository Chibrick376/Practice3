package org.example;

import java.util.ArrayList;
import java.util.List;

public class CopyDemo {

    public static void main(String[] args) {
        List<DebitAccount> source = new ArrayList<>();
        source.add(new DebitAccount(new AccountNumber("0000001001"), "A", 1_000));
        source.add(new DebitAccount(new AccountNumber("0000001002"), "B", 2_000));

        List<BankAccount> target = new ArrayList<>();
        CollectionUtils.copy(source, target);

        System.out.println("target.size() = " + target.size());
        for (BankAccount a : target) {
            System.out.println(a);
        }
    }
}