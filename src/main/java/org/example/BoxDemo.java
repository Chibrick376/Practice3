package org.example;

public class BoxDemo
{
    public static void main(String[] args)
    {
        // Сценарий 1: работает
        Box box1 = new Box();
        box1.set("Java");
        String value1 = (String) box1.get();
        System.out.println("Сценарий 1: " + value1);

        // Сценарий 2: ClassCastException
        try {
            Box box2 = new Box();
            box2.set(123);
            String value2 = (String) box2.get();
            System.out.println("Сценарий 2: " + value2);
        } catch (ClassCastException e) {
            System.out.println("Сценарий 2: ClassCastException — " + e.getMessage());
        }
    }
}