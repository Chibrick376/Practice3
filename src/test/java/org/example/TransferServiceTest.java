package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest
{

    @Test
    void successfulTransferChangesBothBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000101"), "Алиса", 10_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000102"), "Борис", 2_000);

        boolean result = service.transfer(from, to, 3_000);

        assertTrue(result);
        assertEquals(7_000, from.getBalance());
        assertEquals(5_000, to.getBalance());
    }

    @Test
    void failedTransferDoesNotChangeAnyBalance()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000103"), "Виктор", 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000104"), "Галина", 2_000);

        boolean result = service.transfer(from, to, 3_000);

        assertFalse(result);
        assertEquals(1_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void cannotTransferNegativeAmount()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000105"), "Дарья", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000106"), "Егор", 1_000);

        assertFalse(service.transfer(from, to, -500));
    }

    @Test
    void cannotTransferZeroAmount()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000107"), "Жанна", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000108"), "Иван", 1_000);

        assertFalse(service.transfer(from, to, 0));
    }

    @Test
    void cannotTransferToSameAccount()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount account = new DebitAccount(
                new AccountNumber("0000000109"), "Ксения", 5_000);

        assertFalse(service.transfer(account, account, 1_000));
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void commissionIsChargedFromSender()
    {
        TransferService service = new TransferService(
                new PercentCommission(1), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000110"), "Леонид", 11_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000111"), "Мария", 2_000);

        boolean result = service.transfer(from, to, 10_000);

        assertTrue(result);
        assertEquals(900, from.getBalance());
    }

    @Test
    void receiverGetsExactlyTransferAmount()
    {
        TransferService service = new TransferService(
                new PercentCommission(5), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000112"), "Николай", 20_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000113"), "Ольга", 1_000);

        boolean result = service.transfer(from, to, 10_000);

        assertTrue(result);
        assertEquals(11_000, to.getBalance());
        assertEquals(9_500, from.getBalance());
    }

    @Test
    void transferFailsWhenFundsAreInsufficientForCommission()
    {
        TransferService service = new TransferService(
                new PercentCommission(1), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000114"), "Пётр", 10_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000115"), "Раиса", 2_000);

        boolean result = service.transfer(from, to, 10_000);

        assertFalse(result);
        assertEquals(10_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void transferFromDebitToDebit()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000120"), "Анна", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000121"), "Борис", 1_000);

        assertTrue(service.transfer(from, to, 2_000));
        assertEquals(3_000, from.getBalance());
        assertEquals(3_000, to.getBalance());
    }

    @Test
    void transferFromDebitToSavings()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000122"), "Вера", 5_000);
        BankAccount to = new SavingsAccount(
                new AccountNumber("0000000123"), "Глеб", 2_000, 1_000);

        assertTrue(service.transfer(from, to, 2_500));
        assertEquals(2_500, from.getBalance());
        assertEquals(4_500, to.getBalance());
    }

    @Test
    void transferFromCreditToDebit()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new CreditAccount(
                new AccountNumber("0000000124"), "Дмитрий", 1_000, 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000125"), "Елена", 500);

        assertTrue(service.transfer(from, to, 3_000));
        assertEquals(-2_000, from.getBalance());
        assertEquals(3_500, to.getBalance());
    }

    @Test
    void transferFromSavingsToDebit()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new SavingsAccount(
                new AccountNumber("0000000126"), "Жанна", 10_000, 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000127"), "Игорь", 500);

        assertTrue(service.transfer(from, to, 7_000));
        assertEquals(3_000, from.getBalance());
        assertEquals(7_500, to.getBalance());
    }

    @Test
    void transferFromSavingsFailsWhenMinimumBalanceWouldBeViolated()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new SavingsAccount(
                new AccountNumber("0000000128"), "Кирилл", 5_000, 2_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000129"), "Лариса", 500);

        assertFalse(service.transfer(from, to, 3_500));
        assertEquals(5_000, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void transferFromCreditFailsWhenLimitWouldBeExceeded()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new CreditAccount(
                new AccountNumber("0000000130"), "Максим", 0, 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000131"), "Нина", 500);

        assertFalse(service.transfer(from, to, 2_000));
        assertEquals(0, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void exactlyOneNotificationIsSentAfterSuccessfulTransfer()
    {
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(
                new NoCommission(), notificationService);
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000140"), "Олег", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000141"), "Полина", 0);

        service.transfer(from, to, 1_000);

        assertEquals(1, notificationService.getNotificationCount());
    }

    @Test
    void noNotificationIsSentOnFailedTransfer()
    {
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(
                new NoCommission(), notificationService);
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000142"), "Роман", 500);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000143"), "Светлана", 0);

        service.transfer(from, to, 1_000);

        assertEquals(0, notificationService.getNotificationCount());
    }

    @Test
    void notificationTextMatchesExpected()
    {
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(
                new NoCommission(), notificationService);
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000144"), "Тимур", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000145"), "Ульяна", 0);

        service.transfer(from, to, 3_000);

        assertEquals("Transfer 3000.0 completed",
                notificationService.getLastMessage());
    }
}