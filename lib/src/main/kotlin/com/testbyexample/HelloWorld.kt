package com.testbyexample

/**
 * Hey
 */
class HelloWorld(
    val i: Int = 0
) {
    @Example(
        self = "HelloWorld(42)",
        params = [],
        result = "true")
    fun bar(): Boolean {
        return i == 42
    }
}
