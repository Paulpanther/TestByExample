package com.testbyexample;

public class ProbeTest {
    public String foo(String bla) {
        return addProbe(bla.toLowerCase(), 11);
    }

    public static String addProbe(String value, int code) {
//        System.out.println(code + value);
        return value;
    }
}
