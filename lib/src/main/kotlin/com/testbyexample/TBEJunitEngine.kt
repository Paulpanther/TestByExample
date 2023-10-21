package com.testbyexample

import org.junit.platform.engine.*
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import java.lang.reflect.Method

class TBEJunitEngine: TestEngine {
    init {
        AttachConnector()
    }

    override fun getId() = "testbyexample"

    override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
        val methodSignatures = findAndRun("com.testbyexample.ExampleLookup", "methodsUnderTest") {
            (it as List<*>).map { item -> (item as List<*>).map { subItem -> subItem as String } }
        }
        val allMethods = methodSignatures.map { m -> findMethodFromSignature(m) }
        val validMethods = validateMethods(allMethods, discoveryRequest)

        val descriptor = EngineDescriptor(uniqueId, "Test by Example")
        validMethods.forEach {
            val desc = ExampleTestDescriptor(it, uniqueId)
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
            val fileName = "com.testbyexample." + (exampleDesc.method.declaringClass.canonicalName + exampleDesc.method.name).replace(".", "") + "0"

            val result = findAndRun(fileName, "runExample") { it as Pair<*, *> }
            val status = createStatus(result)
            request.engineExecutionListener.executionFinished(descriptor, status)

//            if (!result) allStatus = TestExecutionResult.failed(Exception("Something failed"))
        }

//        request.engineExecutionListener.executionFinished(root, allStatus)
    }

    private fun createStatus(res: Pair<*, *>): TestExecutionResult {
        val (expected, actual) = res
        val equal = expected == actual
        if (equal) return TestExecutionResult.successful()
        val e = Exception("Actual and expected results differ:\nExpected: $expected\nActual: $actual")
        return TestExecutionResult.failed(e)
    }

    private fun validateMethods(methods: List<Method>, request: EngineDiscoveryRequest): List<Method> {
        return methods.filter { method ->
            request.validClass(method.declaringClass) && request.validMethod(method)
        }
    }

    private fun findMethodFromSignature(signature: List<String>): Method {
        val (clazzName, methodName) = signature
        val paramNames = signature.subList(2, signature.size)
        return findMethod(clazzName, methodName, paramNames)
    }

    private fun <T> findAndRun(path: String, methodName: String, params: List<String> = listOf(), caster: (Any) -> T): T {
        val method = findMethod(path, methodName, params)
        return caster(method.invoke(method.declaringClass.getConstructor().newInstance()))
    }

    private fun findMethod(path: String, methodName: String, paramNames: List<String>): Method {
        val loader = TBEJunitEngine::class.java.classLoader
        val runner = loader.loadClass(path)
        val params = paramNames.map { loader.loadClass(it) }
        return runner.getMethod(methodName, *params.toTypedArray())
    }
}