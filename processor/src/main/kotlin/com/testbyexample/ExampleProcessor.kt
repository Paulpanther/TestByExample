package com.testbyexample

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import java.io.OutputStream

class ExampleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = Example::class.qualifiedName ?: return listOf()
        val symbols = resolver
            .getSymbolsWithAnnotation(annotationName)
            .filterIsInstance<KSFunctionDeclaration>()

        for (symbol in symbols) {
            val packageName = symbol.packageName.asString()
            val examples = symbol.annotations.mapNotNull { ksToExample(it) }
            val name = (symbol.qualifiedName?.asString() ?: symbol.simpleName.asString()).replace(".", "")
            val functionName = symbol.simpleName.asString()

            for ((i, example) in examples.withIndex()) {
                val fileName = "$name$i"

                val out = codeGenerator.createNewFile(
                    Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
                    packageName,
                    fileName)

                out += "package $packageName\n\n"
                out += "fun runExample(): Boolean {\n"
                out += "  val self = ${example.self}\n"
                out += "  val result = ${example.result}\n"
                out += "  val actualResult = self.${functionName}(\n"
                for (param in example.params) {
                    out += "    $param,\n"
                }
                out += "  )\n"
                out += "  return result == actualResult\n"
                out += "}\n"

                out.close()
            }
        }
        return symbols.filterNot { it.validate() }.toList()
    }

    private fun ksToExample(annotation: KSAnnotation): Example? {
        val params = annotation.arguments.map {
            val name = it.name?.asString() ?: return null
            val value = it.value
            name to value
        }.toMap()
        val selfP = params["self"] as? String ?: return null
        val paramsP = params["params"] as? ArrayList<*> ?: return null
        val resultP = params["result"] as? String ?: return null
        val saveParamsP = paramsP.map { it as? String ?: return null }.toTypedArray()

        return Example(selfP, saveParamsP, resultP)
    }

    private operator fun OutputStream.plusAssign(str: String) {
        this.write(str.toByteArray())
    }
}

