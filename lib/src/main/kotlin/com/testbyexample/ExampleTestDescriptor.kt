package com.testbyexample

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import java.lang.reflect.Method

class ExampleTestDescriptor(
    val clazz: Class<*>,
    val method: Method,
    id: UniqueId,
    methodName: String = "${clazz.simpleName}#${method.name}"
): AbstractTestDescriptor(id.append("testbyexample", methodName), methodName) {
    override fun getType() = TestDescriptor.Type.TEST
}