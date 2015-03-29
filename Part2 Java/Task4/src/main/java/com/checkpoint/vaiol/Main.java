package com.checkpoint.vaiol;

import com.checkpoint.vaiol.numberValidator.PhoneNumberValidator;

public class Main {
    public static void main(String[] args) {
        String phone = "+2    (8333) 236-45-54";
        System.out.println(PhoneNumberValidator.validate(phone));

    }
}
