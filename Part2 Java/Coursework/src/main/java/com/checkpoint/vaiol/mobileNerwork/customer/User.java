package com.checkpoint.vaiol.mobileNerwork.customer;

import com.checkpoint.vaiol.mobileNerwork.MobileNetwork;
import com.checkpoint.vaiol.mobileNerwork.Operator;
import com.checkpoint.vaiol.mobileNerwork.Position;
import com.checkpoint.vaiol.mobileNerwork.Tower;
import com.checkpoint.vaiol.mobileNerwork.packages.Package;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class User {

    private static Random random = new Random();

    private List<Tower> availableTowers;
    private double balance;
    private boolean available;
    private UserStatusEnum status;
    private com.checkpoint.vaiol.mobileNerwork.packages.Package aPackage;
    private String number; //unique id
    private Position position;

    public User(Package aPackage, String number) {
        balance = 10;
        status = UserStatusEnum.wait;
        position = new Position(0,0);
        this.aPackage = aPackage;
        this.number = number;
        availableTowers = new LinkedList<Tower>();
        connectToNetwork();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isAvailable() {
        return available;
    }


    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        if (status.equals(UserStatusEnum.calls) || status.equals(UserStatusEnum.talk)) {
            available = false;
        } else {
            available = true;
        }
        this.status = status;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public String getNumber() {
        return number;
    }

    public Position getPosition() {
        return position;
    }

    public void move(int x, int y) {
        position = new Position(x, y);
        connectToNetwork();
    }

    public void move() {
        int x, y;
        if(random.nextBoolean()) {
            x = position.getX() + random.nextInt(10);
        } else {
            x = position.getX() - random.nextInt(10);
        }
        if(random.nextBoolean()) {
            y = position.getY() + random.nextInt(10);
        } else {
            y = position.getY() - random.nextInt(10);
        }
        position = new Position(x, y);
        connectToNetwork();
    }


    private void connectToNetwork() {
        Operator operator = MobileNetwork.getMyOperator(this);
        for (Tower tower : operator.getTowers()) {
            if(tower.registerNewUser(this)) {
                availableTowers.add(tower);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (number != null ? !number.equals(user.number) : user.number != null) return false;

        return true;
    }

}
