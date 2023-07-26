package com.testbyexample

import java.lang.instrument.Instrumentation

object ProbeAgent {
    @JvmStatic
    fun agentmain(agentArgs: String?, inst: Instrumentation) {
        val targetName = "com.testbyexample.ProbeTest"
        val target = Class.forName(targetName)
        inst.addTransformer(ProbeTransformer(target), true)
        inst.retransformClasses(target)
    }
}
