package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest
{

    @Test
    void successfulTransferMovesMoneyBetweenAccounts()
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

    @Test
    void transferFailsWhenInsufficientFunds()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-002", "Виктор", 1_000);
        BankAccount to = new DebitAccount("B-002", "Галина", 2_000);

        // Act
        boolean result = service.transfer(from, to, 3_000);

        // Assert
        assertFalse(result);
        assertEquals(1_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void transferToSameAccountIsRejected()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount account = new DebitAccount("A-003", "Дарья", 5_000);

        // Act
        boolean result = service.transfer(account, account, 1_000);

        // Assert
        assertFalse(result);
        assertEquals(5_000, account.getBalance());
    }

    @Test
    void transferWithNegativeAmountIsRejected()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-004", "Егор", 5_000);
        BankAccount to = new DebitAccount("B-004", "Жанна", 1_000);

        // Act
        boolean result = service.transfer(from, to, -100);

        // Assert
        assertFalse(result);
        assertEquals(5_000, from.getBalance());
        assertEquals(1_000, to.getBalance());
    }

    @Test
    void transferWithZeroAmountIsRejected()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-005", "Иван", 5_000);
        BankAccount to = new DebitAccount("B-005", "Ксения", 1_000);

        // Act
        boolean result = service.transfer(from, to, 0);

        // Assert
        assertFalse(result);
    }

    @Test
    void percentCommissionIsChargedFromSender()
    {
        // Arrange
        TransferService service = new TransferService(
                new PercentCommission(1),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-006", "Леонид", 11_000);
        BankAccount to = new DebitAccount("B-006", "Мария", 2_000);

        // Act
        boolean result = service.transfer(from, to, 10_000);

        // Assert
        assertTrue(result);
        assertEquals(900, from.getBalance());   // 11 000 - 10 000 - 100 (комиссия)
        assertEquals(12_000, to.getBalance());  // 2 000 + 10 000
    }

    @Test
    void transferFailsWhenSenderCanPayAmountButNotCommission()
    {
        // Arrange
        TransferService service = new TransferService(
                new PercentCommission(1),
                new FakeNotificationService()
        );
        BankAccount from = new DebitAccount("A-007", "Николай", 10_000);
        BankAccount to = new DebitAccount("B-007", "Ольга", 2_000);

        // Act — хватает на 10 000, но не на 10 100
        boolean result = service.transfer(from, to, 10_000);

        // Assert
        assertFalse(result);
        assertEquals(10_000, from.getBalance());
        assertEquals(2_000, to.getBalance());
    }

    @Test
    void notificationIsSentAfterSuccessfulTransfer()
    {
        // Arrange
        FakeNotificationService fake = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), fake);
        BankAccount from = new DebitAccount("A-008", "Пётр", 10_000);
        BankAccount to = new DebitAccount("B-008", "Раиса", 0);

        // Act
        boolean result = service.transfer(from, to, 3_000);

        // Assert
        assertTrue(result);
        assertEquals(1, fake.getCount());
        assertEquals("Transfer 3000.0 completed", fake.getLastMessage());
    }

    @Test
    void notificationIsNotSentOnFailedTransfer()
    {
        // Arrange
        FakeNotificationService fake = new FakeNotificationService();
        TransferService service = new TransferService(new NoCommission(), fake);
        BankAccount from = new DebitAccount("A-009", "Сергей", 1_000);
        BankAccount to = new DebitAccount("B-009", "Татьяна", 0);

        // Act
        boolean result = service.transfer(from, to, 5_000);

        // Assert
        assertFalse(result);
        assertEquals(0, fake.getCount());
    }

    @Test
    void worksWithDifferentAccountTypes()
    {
        // Arrange
        TransferService service = new TransferService(
                new NoCommission(),
                new FakeNotificationService()
        );
        BankAccount savings = new SavingsAccount("S-100", "Ульяна", 10_000, 1_000);
        BankAccount credit = new CreditAccount("C-100", "Фёдор", 0, 5_000);

        // Act
        boolean result = service.transfer(savings, credit, 3_000);

        // Assert
        assertTrue(result);
        assertEquals(7_000, savings.getBalance());
        assertEquals(3_000, credit.getBalance());
    }

    @Test
    void nullCommissionPolicyIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new TransferService(null, new FakeNotificationService()));
    }

    @Test
    void nullNotificationServiceIsRejected()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new TransferService(new NoCommission(), null));
    }
}