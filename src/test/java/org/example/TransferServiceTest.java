package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для TransferService.
 * Покрывают обязательные случаи из задания
 * и комбинации разных типов счетов.
 */
class TransferServiceTest
{

    // ==========================================================
    //  1. Успешный перевод изменяет оба баланса
    // ==========================================================

    @Test
    void successfulTransferChangesBothBalances()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-001", "Алиса", 10_000);
        BankAccount to = new DebitAccount("B-001", "Борис", 2_000);

        // Act
        boolean result = service.transfer(from, to, 3_000);

        // Assert
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
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-002", "Виктор", 1_000);
        BankAccount to = new DebitAccount("B-002", "Галина", 2_000);

        // Act — денег не хватает
        boolean result = service.transfer(from, to, 3_000);

        // Assert
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
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-003", "Дарья", 5_000);
        BankAccount to = new DebitAccount("B-003", "Егор", 1_000);

        // Act
        boolean result = service.transfer(from, to, -500);

        // Assert
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
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-004", "Жанна", 5_000);
        BankAccount to = new DebitAccount("B-004", "Иван", 1_000);

        // Act
        boolean result = service.transfer(from, to, 0);

        // Assert
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
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount account = new DebitAccount("A-005", "Ксения", 5_000);

        // Act
        boolean result = service.transfer(account, account, 1_000);

        // Assert
        assertFalse(result);
        assertEquals(5_000, account.getBalance());
    }

    // ==========================================================
    //  6. Комиссия списывается с отправителя
    // ==========================================================

    @Test
    void commissionIsChargedFromSender()
    {
        // Arrange
        TransferService service = new TransferService(
                new PercentCommission(1),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-006", "Леонид", 11_000);
        BankAccount to = new DebitAccount("B-006", "Мария", 2_000);

        // Act — 10 000 + 1% комиссии (100) = 10 100 списывается
        boolean result = service.transfer(from, to, 10_000);

        // Assert
        assertTrue(result);
        // 11 000 - 10 100 = 900
        assertEquals(900, from.getBalance());
    }

    // ==========================================================
    //  7. Получатель получает ровно сумму перевода
    // ==========================================================

    @Test
    void receiverGetsExactlyTransferAmount()
    {
        // Arrange
        TransferService service = new TransferService(
                new PercentCommission(5),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-007", "Николай", 20_000);
        BankAccount to = new DebitAccount("B-007", "Ольга", 1_000);

        // Act — комиссия 5% от 10 000 = 500, получатель получает ровно 10 000
        boolean result = service.transfer(from, to, 10_000);

        // Assert
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
        // Arrange
        TransferService service = new TransferService(
                new PercentCommission(1),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-008", "Пётр", 10_000);
        BankAccount to = new DebitAccount("B-008", "Раиса", 2_000);

        // Act — хватает на 10 000, но не на 10 100
        boolean result = service.transfer(from, to, 10_000);

        // Assert
        assertFalse(result);
        assertEquals(10_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    // ==========================================================
    //  КОМБИНАЦИИ ТИПОВ СЧЕТОВ
    // ==========================================================

    // ---------- DebitAccount → DebitAccount ----------

    @Test
    void transferFromDebitToDebit()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("D-100", "Анна", 5_000);
        BankAccount to = new DebitAccount("D-101", "Борис", 1_000);

        // Act
        boolean result = service.transfer(from, to, 2_000);

        // Assert
        assertTrue(result);
        assertEquals(3_000, from.getBalance());
        assertEquals(3_000, to.getBalance());
    }

    // ---------- DebitAccount → SavingsAccount ----------

    @Test
    void transferFromDebitToSavings()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("D-102", "Вера", 5_000);
        BankAccount to = new SavingsAccount("S-102", "Глеб", 2_000, 1_000);

        // Act
        boolean result = service.transfer(from, to, 2_500);

        // Assert
        assertTrue(result);
        assertEquals(2_500, from.getBalance());
        assertEquals(4_500, to.getBalance());
    }

    // ---------- CreditAccount → DebitAccount ----------

    @Test
    void transferFromCreditToDebit()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new CreditAccount("C-103", "Дмитрий", 1_000, 5_000);
        BankAccount to = new DebitAccount("D-103", "Елена", 500);

        // Act — снимаем 3 000, баланс уходит в минус: 1000 - 3000 = -2000
        boolean result = service.transfer(from, to, 3_000);

        // Assert
        assertTrue(result);
        assertEquals(-2_000, from.getBalance());
        assertEquals(3_500, to.getBalance());
    }

    // ---------- SavingsAccount → DebitAccount ----------

    @Test
    void transferFromSavingsToDebit()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new SavingsAccount("S-104", "Жанна", 10_000, 1_000);
        BankAccount to = new DebitAccount("D-104", "Игорь", 500);

        // Act — снимаем 7 000, остаток 3 000 (>= 1 000) — OK
        boolean result = service.transfer(from, to, 7_000);

        // Assert
        assertTrue(result);
        assertEquals(3_000, from.getBalance());
        assertEquals(7_500, to.getBalance());
    }

    // ==========================================================
    //  ДОПОЛНИТЕЛЬНЫЕ ПОЛЕЗНЫЕ ТЕСТЫ
    // ==========================================================

    @Test
    void transferFromSavingsFailsWhenMinimumBalanceWouldBeViolated()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new SavingsAccount("S-105", "Кирилл", 5_000, 2_000);
        BankAccount to = new DebitAccount("D-105", "Лариса", 500);

        // Act — 5 000 - 3 500 = 1 500 < 2 000 → отказ
        boolean result = service.transfer(from, to, 3_500);

        // Assert
        assertFalse(result);
        assertEquals(5_000, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void transferFromCreditFailsWhenLimitWouldBeExceeded()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new CreditAccount("C-106", "Максим", 0, 1_000);
        BankAccount to = new DebitAccount("D-106", "Нина", 500);

        // Act — 0 - 2 000 = -2 000 < -1 000 → отказ
        boolean result = service.transfer(from, to, 2_000);

        // Assert
        assertFalse(result);
        assertEquals(0, from.getBalance());
        assertEquals(500, to.getBalance());
    }

    @Test
    void notificationIsSentAfterSuccessfulTransfer()
    {
        // Arrange
        FakeNotificationService fake = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), fake);
        BankAccount from = new DebitAccount("A-N1", "Олег", 5_000);
        BankAccount to = new DebitAccount("B-N1", "Полина", 0);

        // Act
        service.transfer(from, to, 1_000);

        // Assert
        assertEquals(1, fake.getCount());
        assertEquals("Transfer 1000.0 completed", fake.getLastMessage());
    }

    @Test
    void notificationIsNotSentOnFailedTransfer()
    {
        // Arrange
        FakeNotificationService fake = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), fake);
        BankAccount from = new DebitAccount("A-N2", "Роман", 500);
        BankAccount to = new DebitAccount("B-N2", "Светлана", 0);

        // Act
        service.transfer(from, to, 1_000);

        // Assert
        assertEquals(0, fake.getCount());
    }
}