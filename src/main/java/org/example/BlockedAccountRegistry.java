package org.example;

import java.util.HashSet;
import java.util.Set;

/**
 * Реестр заблокированных номеров счетов.
 */
public class BlockedAccountRegistry
{

    private final Set<AccountNumber> blocked = new HashSet<>();

    public void block(AccountNumber number)
    {
        blocked.add(number);
    }

    public void unblock(AccountNumber number)
    {
        blocked.remove(number);
    }

    public boolean isBlocked(AccountNumber number)
    {
        return blocked.contains(number);
    }

    public int size()
    {
        return blocked.size();
    }
}