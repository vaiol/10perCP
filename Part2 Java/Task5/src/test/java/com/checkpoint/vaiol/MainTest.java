package com.checkpoint.vaiol;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Take the source code from the file "srcWithComments",
 * delete comments and write to a file "srcWithoutComments",
 * all parallel testing.
 */
public class MainTest {

    @Test
    public void testCalcAndThreads() {

        String tmpString = Loader.read("filesForTesting/srcWithComments.java");

        Pattern multilineComments = Pattern.compile("/\\*(?:[^*]*(?:\\*(?!/))*)*\\*/");
        Pattern singlelineComments = Pattern.compile("//.*\n");

        //try to find comments
        Matcher regexMatcher = multilineComments.matcher(tmpString);
        assertTrue(regexMatcher.find());
        regexMatcher = singlelineComments.matcher(tmpString);
        assertTrue(regexMatcher.find());

        //delete comments
        tmpString = CodeCleaner.clean(tmpString);
        Loader.write(tmpString, "filesForTesting/srcWithoutComments.java");

        //try to find comments
        regexMatcher = multilineComments.matcher(tmpString);
        assertFalse(regexMatcher.find());
        regexMatcher = singlelineComments.matcher(tmpString);
        assertFalse(regexMatcher.find());
    }

}
