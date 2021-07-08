package ru.aal;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontEndSystem {
    //Выбрал BlockingQueue потому, что этот интрефейс позволяет задать максимальный размер очереди,
    //что соответсвует условию задачи, где требуется ограничить одновременное количество заявок
    //во фронде 2мя.
    //BlockingQueue автоматически переходит в ожидание, когда идет попытка положить n+1 элемент и когда
    //идет попытка получить элемент из пустой очереди.
    //Тем самым это нам позволяет обойтись без Lock механизмов для организации многопоточности.
    private BlockingQueue<Request> requests = new LinkedBlockingQueue<>(2);
    private Request dummy;

    public FrontEndSystem(Request dummy) {
        this.dummy = dummy;
    }

    public void addRequest(Request request) {
        try {
            requests.put(request);
            if(dummy != request) {
                System.out.printf("Клиент(%s): %s отправлена в банк\n",
                        request.getClientThreadName(), request);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Request getRequest() {
        try {
            Request request = requests.take();
            if(dummy != request) {
                System.out.printf("Обработчик (%s): получена заявка на обработку по клиенту - %s \n",
                        Thread.currentThread().getName(), request.getClientThreadName());
            }
            return request;
        } catch (InterruptedException e) {
            return null;
        }
    }
}
