package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest
{

    // ==========================================================
    //  ОБЯЗАТЕЛЬНЫЕ ТЕСТЫ НА ИСКЛЮЧЕНИЯ (Этап 12)
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
    //  ЭТАП 13. СОСТОЯНИЕ СИСТЕМЫ ПОСЛЕ ОШИБКИ
    // ==========================================================

    @Test
    void failedTransferDoesNotChangeBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000201"), "Тест", 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000202"), "Тест", 2_000);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 5_000)
        );
        assertEquals(1_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void failedTransferByLimitDoesNotChangeBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000203"), "Тест", 100_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000204"), "Тест", 1_000);

        assertThrows(
                TransferLimitExceededException.class,
                () -> service.transfer(from, to, 50_001)
        );
        assertEquals(100_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    @Test
    void failedTransferBySameAccountDoesNotChangeBalance()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount account = new DebitAccount(
                new AccountNumber("0000000205"), "Тест", 5_000);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(account, account, 1_000)
        );
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void failedTransferByInvalidAmountDoesNotChangeBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000206"), "Тест", 5_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000207"), "Тест", 1_000);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.transfer(from, to, -100)
        );
        assertEquals(5_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    @Test
    void failedTransferByCommissionDoesNotChangeBalances()
    {
        TransferService service = new TransferService(
                new PercentCommission(1), new FakeNotificationService());
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000208"), "Тест", 10_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000209"), "Тест", 2_000);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 10_000)
        );
        assertEquals(10_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void failedTransferFromSavingsDoesNotChangeBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new SavingsAccount(
                new AccountNumber("0000000210"), "Тест", 5_000, 2_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000211"), "Тест", 500);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 3_500)
        );
        assertEquals(5_000, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void failedTransferFromCreditDoesNotChangeBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(), new FakeNotificationService());
        BankAccount from = new CreditAccount(
                new AccountNumber("0000000212"), "Тест", 0, 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000213"), "Тест", 500);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 2_000)
        );
        assertEquals(0, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void failedTransferDoesNotSendNotification()
    {
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(
                new NoCommission(), notificationService);
        BankAccount from = new DebitAccount(
                new AccountNumber("0000000214"), "Тест", 1_000);
        BankAccount to = new DebitAccount(
                new AccountNumber("0000000215"), "Тест", 2_000);

        assertThrows(
                InsufficientFundsException.class,
                () -> service.transfer(from, to, 5_000)
        );
        assertEquals(0, notificationService.getNotificationCount());
    }

    // ==========================================================
    //  УСПЕШНЫЕ ПЕРЕВОДЫ (регрессионные)
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