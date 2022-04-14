package com.testbyexample

import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.discovery.ClassSelector
import org.junit.platform.engine.discovery.MethodSelector
import java.lang.reflect.Method


internal fun EngineDiscoveryRequest.validClass(clazz: Class<*>) = classSelectors.isEmpty() || classSelectors.any { it.javaClass == clazz }
internal fun EngineDiscoveryRequest.validMethod(method: Method) = methodSelectors.isEmpty() || methodSelectors.any { it.javaMethod == method }


internal val EngineDiscoveryRequest.classSelectors get() = getSelectorsByType(ClassSelector::class.java)
internal val EngineDiscoveryRequest.methodSelectors get() = getSelectorsByType(MethodSelector::class.java)

