package com.checkpoint.vaiol.mobileNerwork.events;

import com.checkpoint.vaiol.mobileNerwork.customer.Subscriber;
import com.checkpoint.vaiol.mobileNerwork.customer.UserStatusEnum;

public class Call implements Runnable {
    private static final int MAX_RESPONSE_TIME = 5000;
    private long callStart;
    private Integer talkingTime;
    private Subscriber calling; //тот кто вызывает
    private Subscriber callee = null; //тот кого вызывают
    private String calleeNumber;
    private boolean putPhoneDown = false;

    public Call(Subscriber calling, String calleeNumber) {
        talkingTime = null;
        callStart = System.currentTimeMillis();
        this.calling = calling;
        this.calleeNumber = calleeNumber;
        new Thread(this).start();
    }

    public void stopTalking() {
        putPhoneDown = true;
    }

    @Override
    public void run() {
        while (callee == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - callStart > MAX_RESPONSE_TIME) {
                putPhoneDown = true;
                calling.setStatus(UserStatusEnum.wait);
                return;
            }
            if (putPhoneDown) {
                return;
            }
        }
        //there is a connection
        calling.setStatus(UserStatusEnum.talk);
        callee.setStatus(UserStatusEnum.talk);
        long conversationStart = System.currentTimeMillis();

        while ( ! putPhoneDown) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        talkingTime = (int)(System.currentTimeMillis() - conversationStart);
        callee.setStatus(UserStatusEnum.wait);
        calling.setStatus(UserStatusEnum.wait);
    }

    public String getCalleeNumber() {
        return calleeNumber;
    }

    public Integer getTalkingTime() {
        return talkingTime;
    }

    public Subscriber getCalling() {
        return calling;
    }

    public Subscriber getCallee() {
        return callee;
    }

    public void setCallee(Subscriber callee) {
        this.callee = callee;
    }

    public boolean isActive() {
        return ! putPhoneDown;
    }
}
