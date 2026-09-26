package org.example;

public class TransferService
{

    private static final double MAX_TRANSFER_AMOUNT = 50_000;

    private final CommissionPolicy commissionPolicy;
    private final NotificationService notificationService;

    public TransferService(CommissionPolicy commissionPolicy,
                           NotificationService notificationService)
    {
        if (commissionPolicy == null)
            throw new IllegalArgumentException("Commission policy must not be null");
        if (notificationService == null)
            throw new IllegalArgumentException("Notification service must not be null");

        this.commissionPolicy = commissionPolicy;
        this.notificationService = notificationService;
    }

    public void transfer(BankAccount from, BankAccount to, double amount)
    {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        if (amount > MAX_TRANSFER_AMOUNT)
            throw new TransferLimitExceededException("Transfer limit exceeded");

        if (from == to)
            throw new IllegalArgumentException("Cannot transfer to the same account");

        double commission = commissionPolicy.calculate(amount);
        double total = amount + commission;

        from.withdraw(total);

        to.deposit(amount);

        notificationService.notify("Transfer " + amount + " completed");
    }
}