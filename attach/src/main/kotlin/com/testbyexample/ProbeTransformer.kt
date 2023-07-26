package com.testbyexample

import javassist.ClassPool
import javassist.LoaderClassPath
import javassist.bytecode.ClassFile
import javassist.bytecode.CodeIterator
import javassist.bytecode.Mnemonic
import javassist.bytecode.Opcode
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

class ProbeTransformer(
    private val target: Class<*>
): ClassFileTransformer {
    override fun transform(
        loader: ClassLoader?,
        className: String?,
        classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain?,
        classfileBuffer: ByteArray?
    ): ByteArray? {
        if (classBeingRedefined != target || loader != target.classLoader) return null

        val classes = ClassPool.getDefault()
        classes.insertClassPath(LoaderClassPath(target.classLoader))
        val clazz = classes.get(target.name)
        val clazzFile = clazz.classFile
        val method = clazzFile.getMethod("foo")
        val iterator = method.codeAttribute.iterator()

        val probeCollectorClazz = classes.get("com.testbyexample.ProbeCollector")
        val probeCollectorIndex = clazzFile.constPool.addClassInfo(probeCollectorClazz)
        val addProbeMethodIndex = clazzFile.constPool.addMethodrefInfo(probeCollectorIndex, "addProbe", "(Ljava/lang/String;I)Ljava/lang/String;")

        while (iterator.hasNext()) {
            val index = iterator.next()
            val op = iterator.byteAt(index)
            iterator.printAt(clazzFile, index)

            if (op == Opcode.INVOKEVIRTUAL) {
                val arg = iterator.s16bitAt(index + 1)
                val type = clazzFile.constPool.getMethodrefType(arg)
                val isVoid = type.endsWith(")V")

                if (!isVoid) {
                    iterator.insert(index + 3, byteArrayOf(
//                        Opcode.BIPUSH.toByte(), 42.toByte(),
                        Opcode.INVOKESTATIC.toByte(),
                        addProbeMethodIndex.ushr(8).toByte(),
                        addProbeMethodIndex.toByte()
                    ))
                    iterator.printAt(clazzFile, index + 3)
//                    iterator.printAt(clazzFile, index + 5)
                }
            }
        }

        return clazz.toBytecode()
    }
}

private fun CodeIterator.printAt(file: ClassFile, index: Int) {
    val op = byteAt(index)

    fun printByte() {
        print(byteAt(index + 1))
    }

    fun print16Bit() {
        print(s16bitAt(index + 1))
    }

    fun printMethod() {
        val i = s16bitAt(index + 1)
        val name = file.constPool.getMethodrefName(i)
        val type = file.constPool.getMethodrefType(i)
        print("$name$type")
    }

    fun printMethodAndClass() {
        val i = s16bitAt(index + 1)
        val clazz = file.constPool.getMethodrefClassName(i)
        print("$clazz.")
        printMethod()
    }

    print("$index: ${Mnemonic.OPCODE[op]} ")
    when (op) {
        Opcode.BIPUSH -> printByte()
        Opcode.INVOKEVIRTUAL -> printMethod()
        Opcode.INVOKESTATIC -> printMethodAndClass()
    }
    println()
}
