package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LambdaDemo
{

    public static void main(String[] args)
    {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new DebitAccount(new AccountNumber("0000000001"), "Ivan", 5000));
        accounts.add(new SavingsAccount(new AccountNumber("0000000002"), "Petr", 1000, 100));
        accounts.add(new CreditAccount(new AccountNumber("0000000003"), "Anna", 3000, 1000));

        Comparator<BankAccount> byBalance = Comparator.comparingDouble(BankAccount::getBalance);
        accounts.sort(byBalance);

        System.out.println("=== Отсортировано по балансу ===");
        for (BankAccount account : accounts)
        {
            System.out.println(account);
        }
    }
}