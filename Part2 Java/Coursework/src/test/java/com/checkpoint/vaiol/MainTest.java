package com.checkpoint.vaiol;


import com.checkpoint.vaiol.mobileNerwork.MobileNetwork;
import com.checkpoint.vaiol.mobileNerwork.Operator;
import com.checkpoint.vaiol.mobileNerwork.customer.Subscriber;
import com.checkpoint.vaiol.mobileNerwork.customer.UserStatusEnum;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MainTest {
    private static Random random = new Random();
    private Operator operator;
    private Subscriber sub1;
    private Subscriber sub2;

    @Before
    public void setUp() {
        operator = new Operator("kyivstar");
        MobileNetwork.registerNewOperator(operator);
        operator.createNewTower(0, 0, 40);

        sub1 = operator.createNewSubscriber();
        sub2 = operator.createNewSubscriber();


    }


    @Test
    public void testCallingWithoutAnswer() { //вызов без ответа
        //абоненты бездействуют
        assertEquals(UserStatusEnum.wait, sub1.getStatus());
        assertEquals(UserStatusEnum.wait, sub2.getStatus());

        //совершаем звонок
        assertTrue(sub1.makeCall(sub2.getNumber()));

        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

        //абоненты находятся в состоянии вызова (не разговора)
        assertEquals(UserStatusEnum.calls, sub1.getStatus());
        assertEquals(UserStatusEnum.calls, sub2.getStatus());

        //ждем пока вызов оборвется по истичению времени
        try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}

        //абоненты бездействуют
        assertEquals(UserStatusEnum.wait, sub1.getStatus());
        assertEquals(UserStatusEnum.wait, sub2.getStatus());
    }

    @Test
    public void testCallingWithAnswer() { //вызов без ответа
        //абоненты бездействуют
        assertEquals(UserStatusEnum.wait, sub1.getStatus());
        assertEquals(UserStatusEnum.wait, sub2.getStatus());

        //совершаем звонок
        assertTrue(sub1.makeCall(sub2.getNumber()));

        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

        //абоненты находятся в состоянии вызова (не разговора)
        assertEquals(UserStatusEnum.calls, sub1.getStatus());
        assertEquals(UserStatusEnum.calls, sub2.getStatus());

        //принимаем вызов
        sub2.receiveCall();

        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

        //абоненты находятся в состоянии разговора
        assertEquals(UserStatusEnum.talk, sub1.getStatus());
        assertEquals(UserStatusEnum.talk, sub2.getStatus());


        //кто-то из абонентов завершает вызов
        if (random.nextBoolean()) {
            sub2.endTheCall();
        } else {
            sub1.endTheCall();
        }

        try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

        //абоненты бездействуют
        assertEquals(UserStatusEnum.wait, sub1.getStatus());
        assertEquals(UserStatusEnum.wait, sub2.getStatus());
    }

}
