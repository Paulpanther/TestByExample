package com.testbyexample

import com.sun.tools.attach.VirtualMachine
import java.io.File

fun main(args: Array<String>) {
    val pid = args.getOrNull(0) ?: error("Missing or incorrect pid arg")
    val vm = VirtualMachine.attach(pid)
    vm.loadAgent(File("attach/build/libs/attach.jar").absolutePath)
    vm.detach()
}
