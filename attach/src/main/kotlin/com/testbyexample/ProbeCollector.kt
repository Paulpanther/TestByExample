package com.testbyexample

object ProbeCollector {
    @JvmStatic
    fun addProbe(value: String, code: Int): String {
        println("$code: $value")
        return value
    }
}
