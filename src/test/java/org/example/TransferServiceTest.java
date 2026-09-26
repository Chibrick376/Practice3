package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest
{

    // ==========================================================
    //  ОБЯЗАТЕЛЬНЫЕ ТЕСТЫ НА ИСКЛЮЧЕНИЯ
    // ==========================================================

    @Test
    void negativeTransferThrowsException()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000105"), "Дарья", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000106"), "Егор", 1_000);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from, to, -500)
        );
        assertEquals("Amount must be positive", ex.getMessage());
        assertEquals(5_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    @Test
    void zeroTransferThrowsException()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000107"), "Жанна", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000108"), "Иван", 1_000);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from, to, 0)
        );
        assertEquals("Amount must be positive", ex.getMessage());
        assertEquals(5_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    @Test
    void transferToSameAccountThrowsException()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount account = new DebitAccount(
                new AccountNumber("0000000109"), "Ксения", 5_000);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(account, account, 1_000)
        );
        assertEquals("Cannot transfer to the same account", ex.getMessage());
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void transferWithoutEnoughMoneyThrowsException()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000103"), "Виктор", 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000104"), "Галина", 2_000);

        InsufficientFundsException ex = assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 5_000)
        );
        assertEquals("Insufficient funds", ex.getMessage());
        assertEquals(1_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void transferOverLimitThrowsException()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000130"), "Максим", 1_000_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000131"), "Нина", 0);

        TransferLimitExceededException ex = assertThrows(
                TransferLimitExceededException.class,
                () -> service.transfer(from, to, 50_001)
        );
        assertEquals("Transfer limit exceeded", ex.getMessage());
        assertEquals(1_000_000, from.getBalance());
        assertEquals(0, to.getBalance());
    }

    // ==========================================================
    //  ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ
    // ==========================================================

    @Test
    void transferWithoutEnoughMoneyForCommissionThrowsException()
    {
        TransferService service = new TransferService(
                new PercentCommission(1), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000114"), "Пётр", 10_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000115"), "Раиса", 2_000);

        InsufficientFundsException ex = assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 10_000)
        );
        assertEquals("Insufficient funds", ex.getMessage());
        assertEquals(10_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void transferExactlyAtLimitSucceeds()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000132"), "Тест", 100_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000133"), "Тест", 0);

        assertDoesNotThrow(() -> service.transfer(from, to, 50_000));

        assertEquals(50_000, from.getBalance());
        assertEquals(50_000, to.getBalance());
    }

    // ==========================================================
    //  УСПЕШНЫЕ ПЕРЕВОДЫ
    // ==========================================================

    @Test
    void successfulTransferChangesBothBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000101"), "Алиса", 10_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000102"), "Борис", 2_000);

        assertDoesNotThrow(() -> service.transfer(from, to, 3_000));

        assertEquals(7_000, from.getBalance());
        assertEquals(5_000, to.getBalance());
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

        assertDoesNotThrow(() -> service.transfer(from, to, 10_000));

        assertEquals(11_000, to.getBalance());
        assertEquals(9_500, from.getBalance());
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

        assertDoesNotThrow(() -> service.transfer(from, to, 10_000));

        assertEquals(900, from.getBalance());
        assertEquals(12_000, to.getBalance());
    }

    // ==========================================================
    //  КОМБИНАЦИИ ТИПОВ СЧЕТОВ
    // ==========================================================

    @Test
    void transferFromDebitToDebit()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000120"), "Анна", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000121"), "Борис", 1_000);

        assertDoesNotThrow(() -> service.transfer(from, to, 2_000));
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

        assertDoesNotThrow(() -> service.transfer(from, to, 2_500));
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

        assertDoesNotThrow(() -> service.transfer(from, to, 3_000));
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

        assertDoesNotThrow(() -> service.transfer(from, to, 7_000));
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

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 3_500)
        );
        assertEquals(5_000, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void transferFromCreditFailsWhenLimitWouldBeExceeded()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new CreditAccount(
                new AccountNumber("0000000134"), "Максим", 0, 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000135"), "Нина", 500);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 2_000)
        );
        assertEquals(0, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    // ==========================================================
    //  УВЕДОМЛЕНИЯ
    // ==========================================================

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
                new AccountNumber("0000000144"), "Роман", 500);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000145"), "Светлана", 0);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 1_000)
        );
        assertEquals(0, notificationService.getNotificationCount());
    }

    @Test
    void notificationTextMatchesExpected()
    {
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(
                new NoCommission(), notificationService);
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000146"), "Тимур", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000147"), "Ульяна", 0);

        service.transfer(from, to, 3_000);

        assertEquals("Transfer 3000.0 completed",
                notificationService.getLastMessage());
    }
}