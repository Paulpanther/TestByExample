package com.testbyexample

import java.io.File

class AttachConnector {
    init {
        start()
    }

    private fun start() {
        val currentProcess = ProcessHandle.current()
        val jar = File("../attach/build/libs/attach.jar")
        val jvm = ProcessHandle.current().info().command().orElse(null) ?: error("Could not start attach process")
        val process = ProcessBuilder(jvm, "-jar", jar.absolutePath, currentProcess.pid().toString()).start()
        process.waitFor()
        if (process.exitValue() != 0) {
            println("Error in attached process: ${process.errorStream.bufferedReader().readText()}")
            // TODO error not shown in Test Runner
        }
        println(process.inputStream.bufferedReader().readText())
    }
}
