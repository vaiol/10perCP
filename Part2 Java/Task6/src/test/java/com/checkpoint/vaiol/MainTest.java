package com.checkpoint.vaiol;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MainTest {

    @Test
    public void shakespeareTest() {

        WordCounter wordCounter1 = new WordCounter("filesForTesting/shakespeare.txt");
        WordCounter wordCounter5 = new WordCounter("filesForTesting/shakespeare.txt", 5);
        WordCounter wordCounter9 = new WordCounter("filesForTesting/shakespeare.txt", 9);

        assertEquals(26856, wordCounter9.getCount("the")); // knowing constant
        assertEquals(wordCounter1.getCount("the"), wordCounter5.getCount("the"));
        assertEquals(wordCounter1.getCount("the"), wordCounter9.getCount("the"));

        assertEquals(6891, wordCounter9.getCount("be")); // knowing constant
        assertEquals(wordCounter1.getCount("be"), wordCounter5.getCount("be"));
        assertEquals(wordCounter1.getCount("be"), wordCounter9.getCount("be"));

        assertEquals(1556, wordCounter9.getCount("should")); // knowing constant
        assertEquals(wordCounter1.getCount("should"), wordCounter5.getCount("should"));
        assertEquals(wordCounter1.getCount("should"), wordCounter9.getCount("should"));

        assertEquals(459, wordCounter9.getCount("hold")); // knowing constant
        assertEquals(wordCounter1.getCount("hold"), wordCounter5.getCount("hold"));
        assertEquals(wordCounter1.getCount("hold"), wordCounter9.getCount("hold"));
    }
}
