package com.checkpoint.vaiol;


import com.checkpoint.vaiol.numberValidator.PhoneNumberValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class MainTest {

    @Before
    public void setUpCalcTest() {

    }



    @Test
    public void testValidNumbers() {

        assertTrue(PhoneNumberValidator.validate("+3 (8067) 236 45 54"));
        assertTrue(PhoneNumberValidator.validate("+3-(8067)-236-45-54"));
        assertTrue(PhoneNumberValidator.validate("+3-(8067) 236 45 54"));
        assertTrue(PhoneNumberValidator.validate("+3 (8067)-236 45 54"));
        assertTrue(PhoneNumberValidator.validate("+3 (8067) 236-45-54"));

        assertTrue(PhoneNumberValidator.validate("(8067) 236-45-54"));
        assertTrue(PhoneNumberValidator.validate("8068-236 45 54"));
        assertTrue(PhoneNumberValidator.validate("806 236-45 54"));
        assertTrue(PhoneNumberValidator.validate("8067-236 452 542"));
        assertTrue(PhoneNumberValidator.validate("8067 23 45 54"));
        assertTrue(PhoneNumberValidator.validate("808 23-45-54"));

        assertTrue(PhoneNumberValidator.validate("236-45-54"));
        assertTrue(PhoneNumberValidator.validate("236 45 54"));
        assertTrue(PhoneNumberValidator.validate("236 454 544"));
        assertTrue(PhoneNumberValidator.validate("236 45 544"));
        assertTrue(PhoneNumberValidator.validate("236-454-54"));
        assertTrue(PhoneNumberValidator.validate("23 454 544"));

        //with service area code (8):
        assertTrue(PhoneNumberValidator.validate("8 (8067) 236 45 54"));
        assertTrue(PhoneNumberValidator.validate("8-(8067)-236-45-54"));
        assertTrue(PhoneNumberValidator.validate("8-(8067) 236 45 54"));
        assertTrue(PhoneNumberValidator.validate("8 (8067)-236 45 54"));
        assertTrue(PhoneNumberValidator.validate("8 (8067) 236-45-54"));
    }

    @Test
    public void testNonValidNumbers() {
        assertFalse(PhoneNumberValidator.validate("+3 8067) 236 45 54"));
        assertFalse(PhoneNumberValidator.validate("+3 (8067 236 45 54"));
        assertFalse(PhoneNumberValidator.validate("+3 (67) 236 45 54"));
        assertFalse(PhoneNumberValidator.validate("3 (8067) 236 45 54"));
        assertFalse(PhoneNumberValidator.validate("+32 (8067) 236 45 54"));
        assertFalse(PhoneNumberValidator.validate("+3-(8067)-2336-45-54"));
        assertFalse(PhoneNumberValidator.validate("+3 (8067) 236 4533 54"));
        assertFalse(PhoneNumberValidator.validate("+3 (8067) 236 45 5224"));
        assertFalse(PhoneNumberValidator.validate("+3 (8067) 2 45 54"));
        assertFalse(PhoneNumberValidator.validate("+3 (8067) 236+45 54"));
        assertFalse(PhoneNumberValidator.validate("+3 (8067) 236 45254"));
        assertFalse(PhoneNumberValidator.validate("-3 (8067) 236-45 54"));
        assertFalse(PhoneNumberValidator.validate("+3 (8067) 236 45 54-34"));
        assertFalse(PhoneNumberValidator.validate("+3--(8067) 236 45 54"));
        assertFalse(PhoneNumberValidator.validate("+3  (8067) 236 45 54")); //multi space
    }

}
