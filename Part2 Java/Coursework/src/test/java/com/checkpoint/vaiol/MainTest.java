package com.checkpoint.vaiol;


import com.checkpoint.vaiol.mobileNerwork.MobileNetwork;
import com.checkpoint.vaiol.mobileNerwork.Operator;
import com.checkpoint.vaiol.mobileNerwork.customer.Subscriber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MainTest {
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
    public void testCalling() {
        System.out.println(sub1);
        System.out.println(sub2);
        sub1.makeCall(sub2.getNumber());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sub1);
        System.out.println(sub2);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sub1);
        System.out.println(sub2);



    }

}
