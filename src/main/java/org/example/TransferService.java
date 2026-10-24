package org.example;

public class TransferService
{

    private static final double MAX_TRANSFER_AMOUNT = 50_000;

    private final CommissionPolicy commissionPolicy;
    private final NotificationService notificationService;
    private final BlockedAccountRegistry blockedAccounts;
    private final TransactionJournal journal;

    public TransferService(CommissionPolicy commissionPolicy,
                           NotificationService notificationService,
                           BlockedAccountRegistry blockedAccounts,
                           TransactionJournal journal)
    {
        if (commissionPolicy == null)
            throw new IllegalArgumentException("Commission policy must not be null");
        if (notificationService == null)
            throw new IllegalArgumentException("Notification service must not be null");
        if (blockedAccounts == null)
            throw new IllegalArgumentException("Blocked account registry must not be null");
        if (journal == null)
            throw new IllegalArgumentException("Transaction journal must not be null");

        this.commissionPolicy = commissionPolicy;
        this.notificationService = notificationService;
        this.blockedAccounts = blockedAccounts;
        this.journal = journal;
    }

    public void transfer(BankAccount from, BankAccount to, double amount)
    {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        if (amount > MAX_TRANSFER_AMOUNT)
            throw new TransferLimitExceededException("Transfer limit exceeded");

        if (from.equals(to))
            throw new IllegalArgumentException("Cannot transfer to the same account");

        if (blockedAccounts.isBlocked(from.getNumber()))
            throw new IllegalStateException("Source account is blocked");

        double commission = commissionPolicy.calculate(amount);
        double total = amount + commission;

        from.withdraw(total);

        to.deposit(amount);

        journal.add(new Transaction(
                TransactionType.TRANSFER,
                from.getNumber(),
                amount,
                TransactionStatus.SUCCESS
        ));

        notificationService.notify("Transfer " + amount + " completed");
    }
}