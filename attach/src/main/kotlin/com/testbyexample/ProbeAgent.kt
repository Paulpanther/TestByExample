package com.testbyexample

import java.lang.instrument.Instrumentation

object ProbeAgent {
    @JvmStatic
    fun agentmain(agentArgs: String?, inst: Instrumentation) {
        println("Hello from agent")
    }
}
