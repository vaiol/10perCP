package com.checkpoint.vaiol.mobileNerwork;

import com.checkpoint.vaiol.mobileNerwork.customer.Subscriber;
import com.checkpoint.vaiol.mobileNerwork.customer.UserStatusEnum;
import com.checkpoint.vaiol.mobileNerwork.events.Call;
import com.checkpoint.vaiol.mobileNerwork.packages.Package;
import com.checkpoint.vaiol.mobileNerwork.packages.PackageFactory;

import java.util.*;

public class Operator implements Runnable {
    private String name;
    private Map<Subscriber, Package> users;
    private List<Call> callList;
    private List<Call> talkList;
    private Set<Tower> towers = new HashSet<Tower>();
    private Set<Package> oldPackages;

    private boolean threadActive = true;

    public Operator(String name) {
        this.name = name;
        users = new HashMap<Subscriber, Package>();
        oldPackages = new HashSet<Package>();
        callList = new LinkedList<Call>();
        talkList = new LinkedList<Call>();
        new Thread(this).start();
    }

    public Set<Subscriber> getUsers() {
        return users.keySet();
    }

    public Set<Tower> getTowers() {
        return towers;
    }

    public Set<Package> getOldPackages() {
        return oldPackages;
    }


    public Subscriber createNewSubscriber() {
        Subscriber tmpSubscriber = new Subscriber(MobileNetwork.generatePhoneNumber());
        users.put(tmpSubscriber, PackageFactory.getBasicPackage());
        tmpSubscriber.connectToNetwork();
        return tmpSubscriber;
    }

    public synchronized void handle(Call call) {
        callList.add(call);
    }

    public Tower createNewTower(int x, int y, int radius) {
        Tower tmpTower = new Tower(this, new Position(x, y), radius);
        towers.add(tmpTower);
        return tmpTower;
    }

    @Override
    public void finalize() {
        threadActive = false;
    }

    @Override
    public void run() {
        Integer time = 0;
        while (threadActive) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Call call : talkList) {
                if ( ! call.isActive()) {
                    if ((time = call.getTalkingTime()) != null) { // если разговор закончился
                        if (users.containsKey(call.getCallee())) { //если вызываемый принадлежит к этому оператору
                            //снимаем необходимое количество бабосов или бонусов:
                            double fee = users.get(call.getCalling()).getFeePerMinuteOnline();
                            int minute = ((time / 1000) / 60) + 1;
                            if (fee != 0) {
                                double price = minute * fee;
                                call.getCalling().setBalance(call.getCalling().getBalance() - price);
                            } else {
                                call.getCalling().setTalkingBonusTime(call.getCalling().getTalkingBonusTime() - minute);
                            }
                            //закончили снимать бабосы
                        } else { //если вызываемый НЕ принадлежит к этому оператору
                            //снимаем необходимое количество бабосов или бонусов:
                            double fee = users.get(call.getCalling()).getFeePerMinuteOffline();
                            int minute = ((time / 1000) / 60) + 1;
                            if (fee != 0) {
                                double price = minute * fee;
                                call.getCalling().setBalance(call.getCalling().getBalance() - price);
                            } else {
                                call.getCalling().setTalkingBonusTime(call.getCalling().getTalkingBonusTime() - minute);
                            }
                            //закончили снимать бабосы
                        }
                    }

                    Subscriber callee;
                    if (call.getCallee() == null) {
                        callee = MobileNetwork.getUserByNumber(call.getCalleeNumber());
                    } else {
                        callee = call.getCallee();
                    }
                    callee.setStatus(UserStatusEnum.wait);
                    callee.setIncomingCall(null);
                    talkList.remove(call);
                }

            }
            for (Call call : callList) {
                if (call.isActive()) {
                    Subscriber callee = MobileNetwork.getUserByNumber(call.getCalleeNumber());
                    if (callee.isAvailable()) {
                        System.out.println();
                        callee.setIncomingCall(call);
                        callee.setStatus(UserStatusEnum.calls);
                        talkList.add(call);
                    }
                }
                callList.remove(call);

            }
        }
    }

}
