package com.checkpoint.vaiol.mobileNerwork;

import java.util.LinkedList;
import java.util.List;

public abstract class MobileNetwork {
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

    public synchronized static Operator getMyOperator(User user) {
        for (Operator o : operators) {
            if(o.getUsers().contains(user)) {
                return o;
            }
        }
        return null;
    }
}
