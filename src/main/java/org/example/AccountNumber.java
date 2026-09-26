package org.example;

/**
 * Номер банковского счёта.
 * Ровно 10 цифр, не null.
 */
public record AccountNumber(String value)
{
    public AccountNumber
    {
        if (value == null || !value.matches("\\d{10}"))
        {
            throw new IllegalArgumentException(
                    "Account number must contain 10 digits");
        }
    }
}