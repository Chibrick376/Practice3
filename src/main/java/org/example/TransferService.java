package org.example;

/**
 * Сервис переводов между счетами.
 * Работает с абстрактным BankAccount, политикой комиссии
 * CommissionPolicy и сервисом уведомлений NotificationService.
 */
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

    /**
     * Переводит деньги со счёта from на счёт to с учётом комиссии.
     * После успешного перевода отправляет уведомление.
     *
     * @return true, если перевод прошёл успешно, иначе false
     */
    public boolean transfer(BankAccount from, BankAccount to, double amount)
    {
        // Сумма перевода должна быть больше нуля
        if (amount <= 0)
            return false;

        // Нельзя переводить на тот же самый счёт
        if (from == to)
            return false;

        // Считаем комиссию
        double commission = commissionPolicy.calculate(amount);

        // Итоговая сумма списания с отправителя
        double total = amount + commission;

        // Пытаемся списать всю сумму одной операцией
        boolean withdrawn = from.withdraw(total);

        // Если списание не удалось — перевод неуспешен,
        // уведомление не отправляем
        if (!withdrawn)
            return false;

        // Деньги списаны — зачисляем получателю сумму перевода
        to.deposit(amount);

        // Отправляем уведомление об успешном переводе
        notificationService.notify("Transfer " + amount + " completed");

        return true;
    }
}