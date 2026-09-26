package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для TransferService.
 * Покрывают обязательные случаи из задания
 * и комбинации разных типов счетов.
 * Отдельно проверяют работу уведомлений.
 */
class TransferServiceTest
{

    // ==========================================================
    //  1. Успешный перевод изменяет оба баланса
    // ==========================================================

    @Test
    void successfulTransferChangesBothBalances()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-001", "Алиса", 10_000);
        BankAccount to = new DebitAccount("B-001", "Борис", 2_000);

        boolean result = service.transfer(from, to, 3_000);

        assertTrue(result);
        assertEquals(7_000, from.getBalance());
        assertEquals(5_000, to.getBalance());
    }

    // ==========================================================
    //  2. Неуспешный перевод не изменяет ни один баланс
    // ==========================================================

    @Test
    void failedTransferDoesNotChangeAnyBalance()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-002", "Виктор", 1_000);
        BankAccount to = new DebitAccount("B-002", "Галина", 2_000);

        boolean result = service.transfer(from, to, 3_000);

        assertFalse(result);
        assertEquals(1_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    // ==========================================================
    //  3. Нельзя переводить отрицательную сумму
    // ==========================================================

    @Test
    void cannotTransferNegativeAmount()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-003", "Дарья", 5_000);
        BankAccount to = new DebitAccount("B-003", "Егор", 1_000);

        boolean result = service.transfer(from, to, -500);

        assertFalse(result);
        assertEquals(5_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    // ==========================================================
    //  4. Нельзя переводить нулевую сумму
    // ==========================================================

    @Test
    void cannotTransferZeroAmount()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-004", "Жанна", 5_000);
        BankAccount to = new DebitAccount("B-004", "Иван", 1_000);

        boolean result = service.transfer(from, to, 0);

        assertFalse(result);
        assertEquals(5_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    // ==========================================================
    //  5. Нельзя переводить деньги самому себе
    // ==========================================================

    @Test
    void cannotTransferToSameAccount()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount account = new DebitAccount("A-005", "Ксения", 5_000);

        boolean result = service.transfer(account, account, 1_000);

        assertFalse(result);
        assertEquals(5_000, account.getBalance());
    }

    // ==========================================================
    //  6. Комиссия списывается с отправителя
    // ==========================================================

    @Test
    void commissionIsChargedFromSender()
    {
        TransferService service = new TransferService(
                new PercentCommission(1),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-006", "Леонид", 11_000);
        BankAccount to = new DebitAccount("B-006", "Мария", 2_000);

        boolean result = service.transfer(from, to, 10_000);

        assertTrue(result);
        assertEquals(900, from.getBalance()); // 11 000 - 10 100
    }

    // ==========================================================
    //  7. Получатель получает ровно сумму перевода
    // ==========================================================

    @Test
    void receiverGetsExactlyTransferAmount()
    {
        TransferService service = new TransferService(
                new PercentCommission(5),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-007", "Николай", 20_000);
        BankAccount to = new DebitAccount("B-007", "Ольга", 1_000);

        boolean result = service.transfer(from, to, 10_000);

        assertTrue(result);
        assertEquals(11_000, to.getBalance());   // 1 000 + 10 000
        assertEquals(9_500, from.getBalance());  // 20 000 - 10 000 - 500
    }

    // ==========================================================
    //  8. Перевод не выполняется, если денег недостаточно с учётом комиссии
    // ==========================================================

    @Test
    void transferFailsWhenFundsAreInsufficientForCommission()
    {
        TransferService service = new TransferService(
                new PercentCommission(1),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-008", "Пётр", 10_000);
        BankAccount to = new DebitAccount("B-008", "Раиса", 2_000);

        boolean result = service.transfer(from, to, 10_000);

        assertFalse(result);
        assertEquals(10_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    // ==========================================================
    //  КОМБИНАЦИИ ТИПОВ СЧЕТОВ
    // ==========================================================

    @Test
    void transferFromDebitToDebit()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("D-100", "Анна", 5_000);
        BankAccount to = new DebitAccount("D-101", "Борис", 1_000);

        boolean result = service.transfer(from, to, 2_000);

        assertTrue(result);
        assertEquals(3_000, from.getBalance());
        assertEquals(3_000, to.getBalance());
    }

    @Test
    void transferFromDebitToSavings()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("D-102", "Вера", 5_000);
        BankAccount to = new SavingsAccount("S-102", "Глеб", 2_000, 1_000);

        boolean result = service.transfer(from, to, 2_500);

        assertTrue(result);
        assertEquals(2_500, from.getBalance());
        assertEquals(4_500, to.getBalance());
    }

    @Test
    void transferFromCreditToDebit()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new CreditAccount("C-103", "Дмитрий", 1_000, 5_000);
        BankAccount to = new DebitAccount("D-103", "Елена", 500);

        boolean result = service.transfer(from, to, 3_000);

        assertTrue(result);
        assertEquals(-2_000, from.getBalance());
        assertEquals(3_500, to.getBalance());
    }

    @Test
    void transferFromSavingsToDebit()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new SavingsAccount("S-104", "Жанна", 10_000, 1_000);
        BankAccount to = new DebitAccount("D-104", "Игорь", 500);

        boolean result = service.transfer(from, to, 7_000);

        assertTrue(result);
        assertEquals(3_000, from.getBalance());
        assertEquals(7_500, to.getBalance());
    }

    // ==========================================================
    //  ДОПОЛНИТЕЛЬНЫЕ: ОСОБЕННОСТИ РАЗНЫХ СЧЕТОВ
    // ==========================================================

    @Test
    void transferFromSavingsFailsWhenMinimumBalanceWouldBeViolated()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new SavingsAccount("S-105", "Кирилл", 5_000, 2_000);
        BankAccount to = new DebitAccount("D-105", "Лариса", 500);

        boolean result = service.transfer(from, to, 3_500);

        assertFalse(result);
        assertEquals(5_000, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void transferFromCreditFailsWhenLimitWouldBeExceeded()
    {
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new CreditAccount("C-106", "Максим", 0, 1_000);
        BankAccount to = new DebitAccount("D-106", "Нина", 500);

        boolean result = service.transfer(from, to, 2_000);

        assertFalse(result);
        assertEquals(0, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    // ==========================================================
    //  УВЕДОМЛЕНИЯ (Этап 9)
    // ==========================================================

    // ---------- 1. После успешного перевода отправлено ровно одно уведомление ----------

    @Test
    void exactlyOneNotificationIsSentAfterSuccessfulTransfer()
    {
        // Arrange
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);
        BankAccount from = new DebitAccount("A-N1", "Олег", 5_000);
        BankAccount to = new DebitAccount("B-N1", "Полина", 0);

        // Act
        boolean result = service.transfer(from, to, 1_000);

        // Assert
        assertTrue(result);
        assertEquals(1, notificationService.getNotificationCount());
    }

    // ---------- 2. После неуспешного перевода уведомление не отправляется ----------

    @Test
    void noNotificationIsSentOnFailedTransfer()
    {
        // Arrange
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);
        BankAccount from = new DebitAccount("A-N2", "Роман", 500);
        BankAccount to = new DebitAccount("B-N2", "Светлана", 0);

        // Act
        boolean result = service.transfer(from, to, 1_000);

        // Assert
        assertFalse(result);
        assertEquals(0, notificationService.getNotificationCount());
    }

    // ---------- 3. Текст уведомления соответствует ожидаемому ----------

    @Test
    void notificationTextMatchesExpected()
    {
        // Arrange
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);
        BankAccount from = new DebitAccount("A-N3", "Тимур", 5_000);
        BankAccount to = new DebitAccount("B-N3", "Ульяна", 0);

        // Act
        service.transfer(from, to, 3_000);

        // Assert — ожидаем сообщение "Transfer 3000.0 completed"
        assertEquals(
                "Transfer 3000.0 completed",
                notificationService.getLastMessage()
        );
    }

    // ---------- Дополнительно: уведомления не отправляются при разных ошибках ----------

    @Test
    void noNotificationOnZeroAmount()
    {
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);
        BankAccount from = new DebitAccount("A-N4", "Фёдор", 5_000);
        BankAccount to = new DebitAccount("B-N4", "Харитон", 0);

        service.transfer(from, to, 0);

        assertEquals(0, notificationService.getNotificationCount());
    }

    @Test
    void noNotificationOnSameAccountTransfer()
    {
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), notificationService);
        BankAccount account = new DebitAccount("A-N5", "Цезарь", 5_000);

        service.transfer(account, account, 1_000);

        assertEquals(0, notificationService.getNotificationCount());
    }

    @Test
    void exactlyOneNotificationForPercentCommissionTransfer()
    {
        // Arrange
        FakeNotificationService notificationService = new FakeNotificationService();
        TransferService service = new TransferService(
                new PercentCommission(1),
                notificationService
        );
        BankAccount from = new DebitAccount("A-N6", "Чарли", 11_000);
        BankAccount to = new DebitAccount("B-N6", "Шерлок", 2_000);

        // Act
        boolean result = service.transfer(from, to, 10_000);

        // Assert
        assertTrue(result);
        assertEquals(1, notificationService.getNotificationCount());
        assertEquals("Transfer 10000.0 completed", notificationService.getLastMessage());
    }
}