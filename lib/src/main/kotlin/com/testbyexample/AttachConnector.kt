package com.testbyexample

import java.io.File

class AttachConnector {
    private val i = 4

    init {
        startAttach()
    }

    private fun startAttach() {
        val currentProcess = ProcessHandle.current()
        val jar = File("attach/build/libs/attach.jar")
        val jvm = ProcessHandle.current().info().command().orElse(null) ?: return
        val process = ProcessBuilder(jvm, "-jar", jar.absolutePath, currentProcess.pid().toString()).start()
        process.waitFor()
        ProbeTest().foo("Hey")
    }
}


fun main() {
    AttachConnector()
}
