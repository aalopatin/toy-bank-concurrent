package ru.aal;

import java.util.concurrent.atomic.AtomicInteger;

public class BackEndSystem {

    private AtomicInteger balance;
    private final String SUCCESS = "Бэк система: %s УСПЕШНО ВЫПОЛНЕНА. Получена от %s. Баланс банка: %d\n";
    private final String FAILURE = "Бэк система: %s НЕ ВЫПОЛНЕНА. Получена от %s. " +
            "Сумма больше баланса банка. Баланс банка: %d\n";


    public BackEndSystem(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    public void executeRequest(Request request) {

        switch (request.getType()) {
            case CREDIT:
                credit(request.getAmount(), request.toString());
                break;
            case REPAYMENT:
                repayment(request.getAmount(), request.toString());
                break;
        }
    }

    private void credit(int amount, String request) {
        int oldValue;
        int newValue;
        while (true) {
            oldValue = balance.get();
            newValue = oldValue - amount;
            if (oldValue >= amount && balance.compareAndSet(oldValue, newValue)) {
                System.out.printf(SUCCESS,
                        request,
                        Thread.currentThread().getName(),
                        newValue);
                break;
            } else if (oldValue < amount) {
                System.out.printf(FAILURE,
                        request,
                        Thread.currentThread().getName(),
                        oldValue);
                break;
            }
        }
    }

    private void repayment(int amount, String request) {

        System.out.printf(SUCCESS,
                request,
                Thread.currentThread().getName(),
                balance.addAndGet(amount));
    }
}
