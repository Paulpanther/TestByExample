package com.testbyexample

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
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

        val methodsUnderTest = MethodSpec.methodBuilder("methodsUnderTest")
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(List::class.java, String::class.java))
            .addStatement("return \$T.of(\$S)", List::class.java, signatures.joinToString())
            .build()
        val exampleLookup = TypeSpec.classBuilder("ExampleLookup")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(methodsUnderTest)
            .build()
        val exampleLookupFile = JavaFile.builder("com.testbyexample", exampleLookup)
            .build()
        processingEnv.filer.createFile(exampleLookupFile)

        val classes = signatures.groupBy { it.clazz }
        for ((clazz, clazzSignatures) in classes) {
            val clazzPackage = (clazz.enclosingElement as? QualifiedNameable)
            if (clazzPackage == null || clazzPackage.kind != ElementKind.PACKAGE) continue

            val methods = mutableListOf<MethodSpec>()
            for (signature in clazzSignatures) {
                val examples = signature.method.getAnnotationsByType(Example::class.java)
                val methodName = methodAndParamsToString(signature)

                for ((i, example) in examples.withIndex()) {
                    val selfCall = "(${example.self}).${signature.method.simpleName}(${example.params.joinToString(", ")})"
                    methods += MethodSpec.methodBuilder("${methodName}___$i")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .returns(ParameterizedTypeName.get(Pair::class.java, Object::class.java, Object::class.java))
                        .addStatement("return new \$T<>((${example.result}), ($selfCall))", Pair::class.java)
                        .build()
                }
            }

            val exampleClazz = TypeSpec.classBuilder("___" + clazz.simpleName.toString())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethods(methods)
                .build()
            val exampleFile = JavaFile.builder("com.testbyexample.examples.${clazzPackage.qualifiedName}", exampleClazz)
                .build()
            processingEnv.filer.createFile(exampleFile, listOf(clazz.qualifiedName.toString()))
        }

        return true
    }

    private fun Filer.createFile(obj: JavaFile, imports: List<String> = listOf()) {
        val file = createSourceFile("${obj.packageName}.${obj.typeSpec.name}")

        // Hacky because JavaPoet is mean to me
        val source = obj.toString()
        val modifiedSource = source.split("\n").joinToString("\n") {
            if (it.startsWith("package") && imports.isNotEmpty()) {
                it + "\n" + imports.joinToString("\n") { import -> "import $import;" }
            } else {
                it
            }
        }

        file.openWriter().use { it.write(modifiedSource) }
    }

}

