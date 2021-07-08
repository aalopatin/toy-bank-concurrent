package ru.aal;

import java.util.Random;
import java.util.concurrent.Callable;

public class BalanceGenerator implements Callable<Integer> {

    private Random random = new Random();

    @Override
    public Integer call() throws Exception {
        System.out.printf("Генератор (%s) начал работу\n", Thread.currentThread().getName());
        Thread.sleep(random.nextInt(5000) + 5000);
        int value = random.nextInt(200_000) + 200_000;
        System.out.printf("Генератор (%s) сгенерировал значение: %d\n", Thread.currentThread().getName(), value);
        return value;
    }
}
