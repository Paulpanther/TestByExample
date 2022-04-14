package com.testbyexample

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.MethodSource
import java.lang.reflect.Method

class ExampleTestDescriptor(
    val method: Method,
    id: UniqueId,
    methodName: String = "${method.declaringClass.simpleName}#${method.name}"
): AbstractTestDescriptor(id.append("testbyexample", methodName), methodName, MethodSource.from(method)) {
    override fun getType() = TestDescriptor.Type.TEST
}
