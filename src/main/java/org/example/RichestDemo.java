package org.example;

import java.util.ArrayList;
import java.util.List;

public class RichestDemo
{

    public static void main(String[] args)
    {
        List<DebitAccount> values = new ArrayList<>();
        values.add(new DebitAccount(new AccountNumber("0000002001"), "Иван", 1_000));
        values.add(new DebitAccount(new AccountNumber("0000002002"), "Пётр", 5_000));
        values.add(new DebitAccount(new AccountNumber("0000002003"), "Анна", 3_000));

        DebitAccount richest = AccountUtils.richest(values);
        System.out.println(richest);
    }
}