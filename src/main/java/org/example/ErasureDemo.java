package org.example;

import java.util.ArrayList;
import java.util.List;

public class ErasureDemo
{

    public static void main(String[] args)
    {
        List<String> strings = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        System.out.println(strings.getClass());
        System.out.println(numbers.getClass());
        System.out.println(strings.getClass() == numbers.getClass());
    }
}