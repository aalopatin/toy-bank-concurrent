package ru.aal;

import java.util.Objects;

public class Handler implements Runnable {
    private FrontEndSystem frontEndSystem;
    private BackEndSystem backEndSystem;
    private Request dummy;

    public Handler(FrontEndSystem frontEndSystem, BackEndSystem backEndSystem, Request dummy) {
        this.frontEndSystem = frontEndSystem;
        this.backEndSystem = backEndSystem;
        this.dummy = dummy;

    }

    @Override
    public void run() {
        boolean done = false;
        while(!done) {
            Request request = frontEndSystem.getRequest();
            if (Objects.nonNull(request)) {
                if (request == dummy) {
                    done = true;
                    frontEndSystem.addRequest(request);
                } else {
                    backEndSystem.executeRequest(request);
                }
            }
        }
    }
}
