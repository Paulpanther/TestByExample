package com.testbyexample.runner

import org.junit.platform.engine.*


class TBEJunitEngine: TestEngine {
    override fun getId() = "testbyexample"

    override fun discover(discoveryRequest: EngineDiscoveryRequest?, uniqueId: UniqueId?): TestDescriptor {
        TODO("Not yet implemented")
    }

    override fun execute(request: ExecutionRequest?) {
        TODO("Not yet implemented")
    }

}