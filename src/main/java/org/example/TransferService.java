package org.example;

public class TransferService
{

    private final CommissionPolicy commissionPolicy;
    private final NotificationService notificationService;

    public TransferService(CommissionPolicy commissionPolicy,
                           NotificationService notificationService)
    {
        if (commissionPolicy == null)
            throw new IllegalArgumentException("Политика комиссии не может быть null!");
        if (notificationService == null)
            throw new IllegalArgumentException("Сервис уведомлений не может быть null!");

        this.commissionPolicy = commissionPolicy;
        this.notificationService = notificationService;
    }

    public boolean transfer(BankAccount from, BankAccount to, double amount)
    {
        if (amount <= 0)
            return false;

        if (from == to)
            return false;

        double commission = commissionPolicy.calculate(amount);
        double total = amount + commission;

        boolean withdrawn = from.withdraw(total);

        if (!withdrawn)
            return false;

        to.deposit(amount);

        notificationService.notify("Transfer " + amount + " completed");

        return true;
    }
}