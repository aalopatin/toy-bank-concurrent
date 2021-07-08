package ru.aal;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FrontEndSystem {
    private BlockingQueue<Request> requests = new ArrayBlockingQueue<>(2);
    private Request dummy;

    public FrontEndSystem(Request dummy) {
        this.dummy = dummy;
    }

    public void addRequest(Request request) {
        try {
            requests.put(request);
            if(dummy != request) {
                System.out.printf("Клиент(%s): %s отправлена в банк\n", request.getClientThreadName(), request);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Request getRequest() {
        try {
            Request request = requests.take();
            if(dummy != request) {
                System.out.printf("Обработчик (%s): получена заявка на обработку по клиенту - %s \n", Thread.currentThread().getName(), request.getClientThreadName());
            }
            return request;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }
}
