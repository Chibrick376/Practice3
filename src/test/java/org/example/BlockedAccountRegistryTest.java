package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockedAccountRegistryTest
{

    @Test
    void blockedNumberIsBlocked()
    {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000001");

        registry.block(number);

        assertTrue(registry.isBlocked(number));
    }

    @Test
    void blockingSameAccountTwiceDoesNotCreateDuplicate()
    {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000001");

        registry.block(number);
        registry.block(number);

        assertEquals(1, registry.size());
    }

    @Test
    void unblockRemovesNumber()
    {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();
        AccountNumber number = new AccountNumber("0000000002");

        registry.block(number);
        registry.unblock(number);

        assertFalse(registry.isBlocked(number));
        assertEquals(0, registry.size());
    }

    @Test
    void unknownNumberIsNotBlocked()
    {
        BlockedAccountRegistry registry = new BlockedAccountRegistry();

        assertFalse(registry.isBlocked(new AccountNumber("0000000003")));
    }
}