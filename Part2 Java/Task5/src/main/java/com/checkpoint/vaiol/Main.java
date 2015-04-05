package com.checkpoint.vaiol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String tmpString = SourceExample.getString();
        Pattern regex = Pattern.compile("/\\*(?:[^*]*(?:\\*(?!/))*)*\\*/");
        Matcher regexMatcher = regex.matcher(tmpString);

        if (regexMatcher.find()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        tmpString = CodeCleaner.clean(tmpString);


        Matcher regexMatcher2 = regex.matcher(tmpString);
        if (regexMatcher2.find()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
