package com.checkpoint.vaiol.mobileNerwork;

import com.checkpoint.vaiol.mobileNerwork.events.Event;

import java.util.LinkedList;
import java.util.List;

public class Tower {
    private int radius;
    private Position position;
    private Operator operator;
    private List<User> availableUsers;

    public Tower(Operator operator, Position position, int radius) {
        this.operator = operator;
        this.position = position;
        this.radius = radius;
        availableUsers = new LinkedList<User>();
    }

    public synchronized boolean isAvailableUser(User user) {
        return availableUsers.contains(user);
    }

    public synchronized boolean registerNewUser(User user) {
        if (isAvailableUser(user)) {
            return true;
        }
        int x = user.getPosition().getX();
        int y = user.getPosition().getY();
        int a = position.getX();
        int b = position.getY();
        double distance = Math.sqrt(Math.pow((a - x), 2) + Math.pow((b - y), 2));
        if (distance < radius) {
            availableUsers.add(user);
            return true;
        }
        return false;
    }

    public synchronized void handleEvent(Event event) {

    }
}
