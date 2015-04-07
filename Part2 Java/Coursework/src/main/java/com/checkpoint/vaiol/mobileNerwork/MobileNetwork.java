package com.checkpoint.vaiol.mobileNerwork;

import com.checkpoint.vaiol.mobileNerwork.customer.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class MobileNetwork {

    private static Random random = new Random();
    private final static int MAXIMUM_NUMBER_OF_OPERATORS = 4;
    private static List<Operator> operators = new LinkedList<Operator>();

    public synchronized boolean registerNewOperator(Operator operator) {
        if (operators.contains(operator)) {
            return true;
        }
        if (operators.size() >= MAXIMUM_NUMBER_OF_OPERATORS) {
            return false;
        }
        operators.add(operator);
        return true;
    }

    public synchronized static List<Tower> getAllTowers() {
        List<Tower> result = new LinkedList<Tower>();
        for (Operator o : operators) {
            result.addAll(o.getTowers());
        }
        return result;
    }

    public synchronized static List<User> getAllUsers() {
        List<User> result = new LinkedList<User>();
        for (Operator o : operators) {
            result.addAll(o.getUsers());
        }
        return result;
    }

    public synchronized static Operator getMyOperator(User user) {
        for (Operator o : operators) {
            if(o.getUsers().contains(user)) {
                return o;
            }
        }
        return null;
    }

    public synchronized static String generatePhoneNumber() {
        String result = "+";
        for(int i = 0; i < 10; i++) {
            result += random.nextInt(10);
        }
        for (User user : getAllUsers()) {
            if (user.getNumber().equals(result)) {
                generatePhoneNumber();
            }
        }
        return result;
    }
}
