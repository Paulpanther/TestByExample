package com.testbyexample

import org.junit.platform.engine.*
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import java.io.File
import java.lang.reflect.Method

class TBEJunitEngine: TestEngine {
    override fun getId() = "testbyexample"

    override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
        val methods = findAndRun("com.testbyexample.ExampleLookup", "methodsUnderTest") {
            (it as List<*>).map { item -> (item as List<*>).map { subItem -> subItem as String } }
        }
        val descriptor = EngineDescriptor(uniqueId, "Test by Example")
        methods.forEach {
            val desc = buildDescriptorFromSignature(it, uniqueId)
            descriptor.addChild(desc)
        }
        return descriptor
    }

    override fun execute(request: ExecutionRequest) {
        val root = request.rootTestDescriptor as EngineDescriptor
//        request.engineExecutionListener.executionStarted(root)
//        var allStatus = TestExecutionResult.successful()

        for (descriptor in root.children) {
            val exampleDesc = descriptor as ExampleTestDescriptor
            request.engineExecutionListener.executionStarted(descriptor)
            val fileName = "com.testbyexample." + (exampleDesc.clazz.canonicalName + exampleDesc.method.name).replace(".", "") + "0"

            val result = findAndRun(fileName, "runExample") { it as Boolean }
            val status = if (result) TestExecutionResult.successful() else TestExecutionResult.failed(Exception("Wrong"))
            request.engineExecutionListener.executionFinished(descriptor, status)

//            if (!result) allStatus = TestExecutionResult.failed(Exception("Something failed"))
        }

//        request.engineExecutionListener.executionFinished(root, allStatus)
    }

    private fun buildDescriptorFromSignature(signature: List<String>, uniqueId: UniqueId): ExampleTestDescriptor {
        val (clazz, method) = findMethodFromSignature(signature)
        return ExampleTestDescriptor(clazz, method, uniqueId)
    }


    private fun findMethodFromSignature(signature: List<String>): Pair<Class<*>, Method> {
        val (clazzName, methodName) = signature
        val paramNames = signature.subList(2, signature.size)
        return findMethod(clazzName, methodName, paramNames)
    }

    private fun <T> findAndRun(path: String, methodName: String, params: List<String> = listOf(), caster: (Any) -> T): T {
        val (runner, method) = findMethod(path, methodName, params)
        return caster(method.invoke(runner.getConstructor().newInstance()))
    }

    private fun findMethod(path: String, methodName: String, paramNames: List<String>): Pair<Class<*>, Method> {
        val loader = TBEJunitEngine::class.java.classLoader
        val runner = loader.loadClass(path)
        val params = paramNames.map { loader.loadClass(it) }
        val method = runner.getMethod(methodName, *params.toTypedArray())
        return runner to method
    }
}
