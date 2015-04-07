package com.checkpoint.vaiol.mobileNerwork.customer;

import com.checkpoint.vaiol.mobileNerwork.MobileNetwork;
import com.checkpoint.vaiol.mobileNerwork.Operator;
import com.checkpoint.vaiol.mobileNerwork.Position;
import com.checkpoint.vaiol.mobileNerwork.Tower;
import com.checkpoint.vaiol.mobileNerwork.events.Call;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Subscriber {

    private static Random random = new Random();

    private List<Tower> availableTowers;
    private double balance;
    private boolean available;
    private UserStatusEnum status;
    private String number; //unique id
    private Position position;
    private int talkingBonusTime;
    private int messageBonusCount;
    private Call incomingCall = null;
    private Call outgoingCall = null;

    public Subscriber(String number) {
        balance = 10;
        status = UserStatusEnum.wait;
        position = new Position(0,0);
        this.number = number;
        availableTowers = new LinkedList<Tower>();
        talkingBonusTime = 0;
        messageBonusCount = 0;
        available = true;
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

    public String getNumber() {
        return number;
    }

    public Position getPosition() {
        return position;
    }

    public int getTalkingBonusTime() {
        return talkingBonusTime;
    }

    public int getMessageBonusCount() {
        return messageBonusCount;
    }

    public void setMessageBonusCount(int messageBonusCount) {
        this.messageBonusCount = messageBonusCount;
    }

    public void setTalkingBonusTime(int talkingBonusTime) {
        this.talkingBonusTime = talkingBonusTime;
    }


    public void setIncomingCall(Call incomingCall) {
        if (incomingCall != null) {
            System.out.println("Subscriber(" + number + ") have new incoming call. Tink-Tink!!!");
        }
        this.incomingCall = incomingCall;
    }

    public void receiveCall() {
        incomingCall.setCallee(this);
    }

    public void endTheCall() {
        boolean checkError1 = false;
        boolean checkError2 = false;

        if (incomingCall != null) {
            incomingCall.stopTalking();
            checkError1 = true;
        }
        if (outgoingCall != null) {
            outgoingCall.stopTalking();
            checkError2 = true;
        }

        if (checkError1 && checkError2) {
            throw new RuntimeException("Impossible situation!");
        }
    }

    public boolean makeCall(String number) {
        if (availableTowers.isEmpty()) {
            System.out.println("Subscriber(" + this.number + "): don't have connection!");
            return false;
        }
        if ( ! available) {
            return false;
        }
        status = UserStatusEnum.calls;
        System.out.println("Subscriber(" + this.number + "): make new call to " + number);
        outgoingCall = new Call(this, number);
        availableTowers.get(random.nextInt(availableTowers.size())).handleCall(outgoingCall);
        return true;
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




    public void connectToNetwork() {
        Operator operator = MobileNetwork.getMyOperator(this);
        for (Tower tower : operator.getTowers()) {
            if(tower.registerNewSubscriber(this)) {
                availableTowers.add(tower);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscriber subscriber = (Subscriber) o;

        if (number != null ? !number.equals(subscriber.number) : subscriber.number != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Subscriber(" + number + "): " + status + ", " + balance;
    }
}
