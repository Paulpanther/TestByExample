package com.testbyexample

class HelloWorld {

    @Example(
        self = "HelloWorld()",
        params = ["\"FooBar\""],
        result = "\"FooBar 42\"")
    fun foo(inp: String): String {
        return "$inp 42"
    }

}

fun main() {
    println("Hey")
}
