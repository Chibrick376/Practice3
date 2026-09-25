package org.example;

/**
 * Сервис переводов между счетами.
 * Работает с абстрактным BankAccount,
 * поэтому подходит для любого типа счёта
 * (DebitAccount, SavingsAccount, CreditAccount).
 */
public class TransferService
{

    /**
     * Переводит деньги со счёта from на счёт to.
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

        // Пытаемся списать деньги у отправителя
        // по правилам его типа счёта
        boolean withdrawn = from.withdraw(amount);

        // Если списание не удалось — перевод неуспешен,
        // баланс получателя не трогаем
        if (!withdrawn)
            return false;

        // Списание прошло — зачисляем получателю
        to.deposit(amount);
        return true;
    }
}