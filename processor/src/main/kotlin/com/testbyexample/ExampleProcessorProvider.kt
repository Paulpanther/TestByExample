package com.testbyexample

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

//@AutoService(SymbolProcessorProvider::class)
class ExampleProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ExampleProcessor(
            environment.codeGenerator,
            environment.logger,
            environment.options)
    }
}