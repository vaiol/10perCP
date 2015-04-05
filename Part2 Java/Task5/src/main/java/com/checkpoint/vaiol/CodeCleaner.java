package com.checkpoint.vaiol;

/**
 * Class that can cleans java source code to comments.
 */
public class CodeCleaner {
    /**
     * Method delete all comments in source code.
     * Clean multiline and single-line comments.
     * Also delete "end of line" symbol that remains after the removal of;
     * @param src java source code;
     * @return clean from comments java source code;
     */
    public static String clean(String src) {
        String tmpString = src;
        tmpString = tmpString.replaceAll("/\\*(?:[^*]*(?:\\*(?!/))*)*\\*/", "");
        tmpString = tmpString.replaceAll("//.*\n", "");
        return  tmpString;
    }
}
