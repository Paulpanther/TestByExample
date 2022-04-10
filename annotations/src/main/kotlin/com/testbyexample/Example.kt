package com.testbyexample

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Example (
    val self: String,
    val params: Array<String>,
    val result: String,
)
