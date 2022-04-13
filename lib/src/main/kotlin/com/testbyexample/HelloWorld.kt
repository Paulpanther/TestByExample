package com.testbyexample

class HelloWorld(
    val i: Int = 0
) {

    @Example(
        self = "HelloWorld()",
        params = ["\"FooBar\""],
        result = "\"FooBar 42\"")
    fun foo(inp: String): String {
        return "$inp 42"
    }

    @Example(
        self = "HelloWorld(42)",
        params = [],
        result = "true")
    fun bar(): Boolean {
        return i == 42
    }
}
