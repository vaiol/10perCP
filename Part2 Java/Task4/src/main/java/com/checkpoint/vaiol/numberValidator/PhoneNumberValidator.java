package com.checkpoint.vaiol.numberValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for validate phone numbers WITHOUT regular expressions.
 */
public class PhoneNumberValidator {

    /**
     * Class have only static methods.
     * Copies are not needed.
     */
    private PhoneNumberValidator() {
    }

    /**
     * Validate phone numbers.
     * Phone numbers may include:
     * main part: 3 group numbers (size: 2 - 3);
     * area part: size: 3 - 4, can optionally be taken in parentheses;
     * country part: size: 1 - 2, may include + and 1 numbers, or only 1 service numbers.
     * @param phoneNumber string of phone numbers;
     * @return valid or not this numbers;
     */
    public static boolean validate(String phoneNumber) {
        //Local constant:
        int minCountOfGroups = 3; //minimum count of groups in phone number
        int maxCountOfGroups = 5; //maximum count of groups in phone number
        int minSizeMainPart = 2; //minimum size 1 groups numbers of main parts
        int maxSizeMainPart = 3;//maximum size 1 groups numbers of main parts
        int first = 0; //index of first element array or list

        String[] numbersParts = phoneNumber.split(" ");
        String[][] parts = new String[numbersParts.length][];

        List<String> partsList = new ArrayList<String>();
        for (int i = 0; i < numbersParts.length; i++) {
            parts[i] = numbersParts[i].split("-");
            partsList.addAll(Arrays.asList(parts[i]));
        }

        if(partsList.size() < minCountOfGroups || partsList.size() > maxCountOfGroups) {
            return false;
        }

        Collections.reverse(partsList);

        for (int i = 0; i < maxSizeMainPart; i++) {
            if( ! validateNumbersPart(partsList.remove(first), minSizeMainPart, maxSizeMainPart)) {
                return false;
            }
        }

        if( ! partsList.isEmpty()) {
            if (!validateCityCodePart(partsList.remove(first))) {
                return false;
            }
        }
        if( ! partsList.isEmpty()) {
            if (!validateCountryCodePart(partsList.remove(first))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validate some part of phone number.
     * Can contains only numbers.
     * @param part some part of phone number
     * @param minPartSize minimum size of part
     * @param maxPartSize maximum size of part
     * @return valid or not this part
     */
    private static boolean validateNumbersPart(String part, int minPartSize, int maxPartSize) {
        int minNumberIndex = 48; //unicode
        int maxNumberIndex = 57; //unicode

        if(part.length() < minPartSize || part.length() > maxPartSize) {
            return false;
        }
        for (int i = 0; i < part.length(); i++) {
            char tmpChar = part.charAt(i);
            if (tmpChar < minNumberIndex || tmpChar > maxNumberIndex) {
                return false;
            }
        }
        return true;


    }

    /**
     * Validate city code in phone number;
     * Size: 3 - 4. Only numbers.
     * Can optionally be taken in parentheses.
     * @param part of number. Area code;
     * @return valid or not this part;
     */
    private static boolean validateCityCodePart(String part) {
        if(part.charAt(0) == '(' && part.charAt(part.length()-1) == ')') {
            part = part.substring(1, part.length()-1);
        }
        return validateNumbersPart(part, 3, 4);
    }

    /**
     * Validate country code part in phone number;
     * Size: 2. Contains + and one number or service number (8);
     * @param part country code;
     * @return valid or not this part;
     */
    private static boolean validateCountryCodePart(String part) {
        if(part.charAt(0) == '+') {
            part = part.substring(1, part.length());
        } else {
            if( ! part.equals("8")) {
                return false;
            }
        }
        return validateNumbersPart(part, 1, 1);
    }
}
