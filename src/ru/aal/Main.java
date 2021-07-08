package ru.aal;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    private static final Request DUMMY = new Request("", 0, RequestType.DUMMY);
    private static FrontEndSystem frontEndSystem= new FrontEndSystem(DUMMY);
    private static BackEndSystem backEndSystem;

    public static void main(String[] args) throws InterruptedException {

        int balance = generateBalance();

        backEndSystem = new BackEndSystem(balance);

        System.out.println("Начальный баланс банка: " + balance);
        System.out.println();

        int countClients = readCountClients();
        int countHandlers = readCountHandlers();

        Client client = new Client(frontEndSystem);
        List<Future<?>> clients = createTasks(countClients, client);

        Handler handler = new Handler(frontEndSystem, backEndSystem, DUMMY);
        List<Future<?>> handlers = createTasks(countHandlers, handler);

        try {
            for (Future<?> futureClient : clients) {
                futureClient.get();
            }
            frontEndSystem.addRequest(DUMMY);
        } catch (InterruptedException | ExecutionException e) {

        }

    }

    private static int generateBalance() throws InterruptedException {
        int balance = 0;

        ExecutorService executorServiceGenerators = Executors.newCachedThreadPool();

        Callable<Integer> balanceGenerator = new BalanceGenerator();
        List<Future<Integer>> generators = executorServiceGenerators.invokeAll(
                asList(balanceGenerator, balanceGenerator, balanceGenerator)
        );
        for (Future<Integer> futureGenerator :
                generators) {
            try {
                balance += futureGenerator.get();
            } catch (ExecutionException e) {
                balance += 300_000;
            }
        }

        executorServiceGenerators.shutdown();

        return balance;
    }

    private static int readCountClients() {
        System.out.println("Введите количество клиентов (не меньше 4): ");
        Scanner scanner = new Scanner(System.in);
        return Math.max(scanner.nextInt(), 4);
    }

    private static int readCountHandlers() {
        System.out.println("Введите количество обработчиков (не меньше 2): ");
        Scanner scanner = new Scanner(System.in);
        return Math.max(scanner.nextInt(), 2);
    }

    private static List<Future<?>> createTasks(int count, Runnable r) {

        ExecutorService executorServiceHandlers = Executors.newCachedThreadPool();
        List<Future<?>> handlers = new ArrayList<>();

        for (int i = 1; i < count + 1; i++) {
            handlers.add(executorServiceHandlers.submit(r));
        }

        executorServiceHandlers.shutdown();

        return handlers;
    }

}
