package com.testbyexample;

public class ProbeTest {
    @Example(
            self = "new ProbeTest()",
            params = "\"Hello World\"",
            result = "\"hello world\"")
    public String foo(String bla) {
        return bla.toLowerCase();
    }
}
