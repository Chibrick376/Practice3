package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class FunctionTest
{

    private List<BankAccount> accounts;

    @BeforeEach
    void setUp()
    {
        accounts = List.of(
                new DebitAccount(new AccountNumber("0000000001"), "A", 150_000),
                new DebitAccount(new AccountNumber("0000000002"), "B", 50_000),
                new CreditAccount(new AccountNumber("0000000003"), "C", 200_000, 1000)
        );
    }

    @Test
    void mapAccountsToSummaries()
    {
        Function<BankAccount, AccountSummary> toSummary =
                account -> new AccountSummary(
                        account.getId(),
                        account.getClass().getSimpleName(),
                        account.getBalance()
                );

        List<AccountSummary> summaries = CollectionFunctions.map(accounts, toSummary);

        assertEquals(3, summaries.size());

        AccountSummary first = summaries.get(0);
        assertEquals(new AccountNumber("0000000001"), first.number());
        assertEquals("DebitAccount", first.type());
        assertEquals(150_000, first.balance());

        assertEquals("CreditAccount", summaries.get(2).type());
        assertEquals(200_000, summaries.get(2).balance());
    }

    @Test
    void mapAccountsToLabels()
    {
        Function<BankAccount, String> label =
                account -> account.getId() + " : " + account.getBalance();

        List<String> labels = CollectionFunctions.map(accounts, label);

        assertEquals(3, labels.size());
        assertEquals("AccountNumber[value=0000000001] : 150000.0", labels.get(0));
        assertEquals("AccountNumber[value=0000000002] : 50000.0", labels.get(1));
        assertEquals("AccountNumber[value=0000000003] : 200000.0", labels.get(2));
    }

    @Test
    void mapEmptyListReturnsEmptyList()
    {
        Function<BankAccount, AccountSummary> toSummary =
                account -> new AccountSummary(
                        account.getId(),
                        account.getClass().getSimpleName(),
                        account.getBalance()
                );

        List<AccountSummary> summaries = CollectionFunctions.map(List.of(), toSummary);

        assertTrue(summaries.isEmpty());
    }

    @Test
    void mapDoesNotChangeSourceList()
    {
        Function<BankAccount, String> label =
                account -> account.getId().value();

        CollectionFunctions.map(accounts, label);

        assertEquals(3, accounts.size());
        assertEquals(new AccountNumber("0000000002"), accounts.get(1).getId());
    }
}
