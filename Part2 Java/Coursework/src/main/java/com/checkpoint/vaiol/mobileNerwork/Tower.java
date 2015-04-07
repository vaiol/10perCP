package com.checkpoint.vaiol.mobileNerwork;

import com.checkpoint.vaiol.mobileNerwork.customer.Subscriber;
import com.checkpoint.vaiol.mobileNerwork.events.Call;

import java.util.LinkedList;
import java.util.List;

public class Tower {
    private int radius;
    private Position position;
    private Operator operator;
    private List<Subscriber> availableSubscribers;

    public Tower(Operator operator, Position position, int radius) {
        this.operator = operator;
        this.position = position;
        this.radius = radius;
        availableSubscribers = new LinkedList<Subscriber>();
    }

    public synchronized boolean isAvailableSubscriber(Subscriber subscriber) {
        return availableSubscribers.contains(subscriber);
    }

    public synchronized boolean registerNewSubscriber(Subscriber subscriber) {
        if (isAvailableSubscriber(subscriber)) {
            return true;
        }
        int x = subscriber.getPosition().getX();
        int y = subscriber.getPosition().getY();
        int a = position.getX();
        int b = position.getY();
        double distance = Math.sqrt(Math.pow((a - x), 2) + Math.pow((b - y), 2));
        if (distance < radius) {
            availableSubscribers.add(subscriber);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tower tower = (Tower) o;

        if (radius != tower.radius) return false;
        if (!availableSubscribers.equals(tower.availableSubscribers)) return false;
        if (!operator.equals(tower.operator)) return false;
        if (!position.equals(tower.position)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = radius;
        result = 31 * result + position.hashCode();
        result = 31 * result + operator.hashCode();
        result = 31 * result + availableSubscribers.hashCode();
        return result;
    }

    public synchronized void handleCall(Call call) {
        operator.handle(call);
    }
}
