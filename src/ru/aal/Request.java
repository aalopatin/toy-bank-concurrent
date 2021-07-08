package ru.aal;

public class Request {
    private String clientThreadName;
    private int amount;
    private RequestType type;

    public Request(String clientThreadName, int amount, RequestType type) {
        this.clientThreadName = clientThreadName;
        this.amount = amount;
        this.type = type;
    }

    public String getClientThreadName() {
        return clientThreadName;
    }

    public int getAmount() {
        return amount;
    }

    public RequestType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Заявка{" +
                "clientThreadName='" + clientThreadName + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                '}';
    }
}
