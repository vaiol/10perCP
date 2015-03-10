package com.checkpoint.vaiol;

import static org.junit.Assert.assertEquals;

import com.checkpoint.vaiol.usingBigInteger.Calculator;
import org.junit.Test;

public class JTest {

    @Test
    public void testSum() {
        assertEquals(new Calculator(10, 5).calculate(), new Calculator(10, 5).calculateWithThreads());
    }

    @Test
    public void testSum1() {
        assertEquals(new Calculator(11, 5).calculate(), new Calculator(11, 5).calculateWithThreads());
    }
}
