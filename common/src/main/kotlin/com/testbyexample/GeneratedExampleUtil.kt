package com.testbyexample

object GeneratedExampleUtil {
    private fun methodAndParamsToString(signature: Signature): String {
        val params = signature.params.joinToString("___") { it.toString().replace(".", "__") }
        return "${signature.method.simpleName}___$params"
    }

}