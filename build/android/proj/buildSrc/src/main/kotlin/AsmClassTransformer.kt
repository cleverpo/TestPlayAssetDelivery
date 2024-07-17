import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.internal.impldep.org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class PrivacyClassVisitor(nextVisitor: ClassVisitor, private val className: String) : ClassVisitor(Opcodes.ASM7, nextVisitor){
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        var result = super.visitMethod(access, name, descriptor, signature, exceptions)
        println(String.format("className: %s, access: %d, name: %s, descriptor: %s, signature: %s", className, access, name, descriptor, signature))

        return result;
    }
}

abstract class MyAsmClassVisitorFactory: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return PrivacyClassVisitor(nextClassVisitor, classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true;
    }
}