package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberTest
{

    @Test
    void validTenDigitNumberIsAccepted()
    {
        AccountNumber number = new AccountNumber("1234567890");
        assertEquals("1234567890", number.value());
    }

    @Test
    void nullIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new AccountNumber(null));
    }

    @Test
    void emptyStringIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new AccountNumber(""));
    }

    @Test
    void shorterThanTenDigitsIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new AccountNumber("12345"));
    }

    @Test
    void longerThanTenDigitsIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new AccountNumber("12345678901"));
    }

    @Test
    void lettersAreRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new AccountNumber("12345abcde"));
    }

    @Test
    void equalValuesAreEqual()
    {
        AccountNumber a = new AccountNumber("1234567890");
        AccountNumber b = new AccountNumber("1234567890");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}