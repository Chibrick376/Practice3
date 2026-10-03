package org.example;

import java.util.List;

/**
 * Утилиты для работы со счетами.
 * Демонстрация принципа PECS: Producer Extends.
 */
public final class AccountUtils {

    private AccountUtils() {
    }

    /**
     * Считает суммарный баланс любого списка счетов.
     * Принимает List<BankAccount>, List<DebitAccount>,
     * List<SavingsAccount>, List<CreditAccount> и т.д.
     *
     * accounts — "producer": мы только читаем из него элементы
     * типа BankAccount, поэтому используется ? extends BankAccount.
     */
    public static double totalBalance(List<? extends BankAccount> accounts) {
        double total = 0;
        for (BankAccount account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    /*
     * ВНУТРИ этого метода нельзя писать:
     *
     *     accounts.add(new DebitAccount(...));  // ошибка компиляции
     *
     * Причина: компилятор не знает реальный тип списка. Он может быть
     * List<DebitAccount>, List<SavingsAccount> или List<CreditAccount>.
     * Если бы разрешили добавить DebitAccount в List<SavingsAccount>,
     * типобезопасность бы разрушилась. Поэтому для ? extends X
     * добавление (кроме null) запрещено.
     *
     * PECS: Producer Extends, Consumer Super.
     */
}