package org.example;

/**
 * Сервис переводов между счетами.
 * Работает с абстрактным BankAccount и политикой комиссии CommissionPolicy.
 */
public class TransferService
{

    private final CommissionPolicy commissionPolicy;

    public TransferService(CommissionPolicy commissionPolicy)
    {
        if (commissionPolicy == null)
            throw new IllegalArgumentException("Политика комиссии не может быть null!");

        this.commissionPolicy = commissionPolicy;
    }

    /**
     * Переводит деньги со счёта from на счёт to с учётом комиссии.
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

        // Пытаемся списать всю сумму одной операцией.
        // Если отправитель может оплатить перевод, но не может
        // оплатить перевод вместе с комиссией — перевод не выполняется.
        boolean withdrawn = from.withdraw(total);

        if (!withdrawn)
            return false;

        // Деньги списаны — зачисляем получателю только сумму перевода
        to.deposit(amount);
        return true;
    }
}