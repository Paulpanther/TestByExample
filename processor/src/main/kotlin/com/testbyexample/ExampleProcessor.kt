package com.testbyexample

import java.io.Writer
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.QualifiedNameable
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror

private data class Signature(
    val packageE: QualifiedNameable,
    val clazz: QualifiedNameable,
    val method: ExecutableElement,
    val params: List<TypeMirror>
) {
    companion object {
        fun from(element: Element): Signature? {
            if (element.kind != ElementKind.METHOD) return null
            val method = element as ExecutableElement

            val clazz = method.enclosingElement as QualifiedNameable
            if (clazz.kind != ElementKind.CLASS) return null

            val packageE = clazz.enclosingElement as QualifiedNameable
            if (packageE.kind != ElementKind.PACKAGE) return null

            val params = method.parameters.map { it.asType() }
            return Signature(packageE, clazz, method, params)
        }
    }

    override fun toString(): String {
        return "$clazz#$method(${params.joinToString(",")})"
    }
}

@SupportedAnnotationTypes("com.testbyexample.Example")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
open class ExampleProcessor: AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (annotations?.isNotEmpty() != true) return true
        val elements = mutableListOf<Element>()
        for (annotation in annotations) {
            elements += (roundEnv ?: return false).getElementsAnnotatedWith(annotation)
        }
        val signatures = elements.mapNotNull { Signature.from(it) }

        val file = processingEnv.filer.createSourceFile("com.testbyexample.ExampleLookup")
        val out = file.openWriter()
        out += "package com.testbyexample;\n\n"
        out += "import java.util.List;\n\n"
        out += "public class ExampleLookup {\n"
        out += "  public List<List<String>> methodsUnderTest() {\n"
        out += "    return List.of(${signatures.joinToString()});\n"
        out += "  }\n"
        out += "}\n"
        out.close()

        for (signature in signatures) {
            val examples = signature.method.getAnnotationsByType(Example::class.java)

            for ((i, example) in examples.withIndex()) {
                val fileName = signature.toString()

                val file = processingEnv.filer.createSourceFile(fileName)
                val out = file.openWriter()
                out += "package ${signature.packageE};\n\n"
                out.close()
            }
        }

        return true
    }

    private fun toQualified(clazz: QualifiedNameable, method: ExecutableElement): String {
        return "${clazz.qualifiedName}#${method.simpleName}(${method.parameters.joinToString(",") { it.asType().toString() }})"
    }

    operator fun Writer.plusAssign(text: String) = this.write(text)
}

