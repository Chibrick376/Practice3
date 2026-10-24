package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferQueueTest
{

    private TransferRequest request(String from, String to, double amount)
    {
        return new TransferRequest(
                new AccountNumber(from),
                new AccountNumber(to),
                amount
        );
    }

    @Test
    void nextReturnsElementsInFifoOrder()
    {
        TransferQueue queue = new TransferQueue();
        TransferRequest a = request("0000000001", "0000000002", 100);
        TransferRequest b = request("0000000003", "0000000004", 200);
        TransferRequest c = request("0000000005", "0000000006", 300);

        queue.add(a);
        queue.add(b);
        queue.add(c);

        assertEquals(a, queue.next());
        assertEquals(b, queue.next());
        assertEquals(c, queue.next());
    }

    @Test
    void peekDoesNotRemoveFirstElement()
    {
        TransferQueue queue = new TransferQueue();
        TransferRequest a = request("0000000001", "0000000002", 100);

        queue.add(a);

        assertEquals(a, queue.peek());
        assertEquals(1, queue.size());
        assertEquals(a, queue.peek());
    }

    @Test
    void nextOnEmptyQueueReturnsNull()
    {
        TransferQueue queue = new TransferQueue();

        assertNull(queue.next());
    }

    @Test
    void peekOnEmptyQueueReturnsNull()
    {
        TransferQueue queue = new TransferQueue();

        assertNull(queue.peek());
    }

    @Test
    void sizeDecreasesAfterNext()
    {
        TransferQueue queue = new TransferQueue();
        queue.add(request("0000000001", "0000000002", 100));
        queue.add(request("0000000003", "0000000004", 200));

        assertEquals(2, queue.size());

        queue.next();

        assertEquals(1, queue.size());
    }

    @Test
    void isEmptyReflectsQueueState()
    {
        TransferQueue queue = new TransferQueue();

        assertTrue(queue.isEmpty());

        queue.add(request("0000000001", "0000000002", 100));

        assertFalse(queue.isEmpty());
    }
}