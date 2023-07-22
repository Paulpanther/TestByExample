package com.testbyexample

import java.io.File

class AttachConnector {
    init {
        startAttach()
    }

    private fun startAttach() {
        val currentProcess = ProcessHandle.current()
        val jar = File("attach/build/libs/attach.jar")
        val jvm = ProcessHandle.current().info().command().orElse(null) ?: return
        val process = ProcessBuilder(jvm, "-jar", jar.absolutePath, currentProcess.pid().toString()).start()
        process.waitFor()
    }
}

fun main() {
    AttachConnector()
}
