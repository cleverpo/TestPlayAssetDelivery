import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class ModifySuperClassMethodAdapter(
    private val superClassName: String,
    api: Int,
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {
    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        var owner_wrapper = owner;
        if(opcode == Opcodes.INVOKESPECIAL && name.equals("<init>")){
            owner_wrapper = superClassName;
        }
        super.visitMethodInsn(opcode, owner_wrapper, name, descriptor, isInterface)
    }
}

class AttachBaseContextMethodAdapter(
    api: Int,
    methodVisitor: MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {
    override fun onMethodEnter() {
        println("Method Enter: $name.$methodDesc");
        // 往栈上加载两个变量，用于后面的函数调用
        mv.visitLdcInsn("LogMethodVisitor")
        mv.visitLdcInsn("enter: .$name")
        // 调用 android.util.Log 函数
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/util/Log",
            "i",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
//        mv.visitInsn(POP)
        super.onMethodEnter();

    }

    override fun onMethodExit(opcode: Int) {
        println("Method Exit: " + getName() + methodDesc);

        super.onMethodExit(opcode);

        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESTATIC, "com/google/android/play/core/splitcompat/SplitCompat", "installActivity", "(Landroid/content/Context;)Z", false);
        mv.visitInsn(POP);
    }
}

class PrivacyClassVisitor(nextVisitor: ClassVisitor, private val className: String) : ClassVisitor(Opcodes.ASM7, nextVisitor){
    val newSuperName = "com/quik/testpad/SplitCompatBaseActivity"
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        println("ClassName: $name, oldSuperName: $superName, newSuperName: $newSuperName")

        super.visit(version, access, name, signature, newSuperName, interfaces)
    }
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        var mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if(mv != null && name.equals("<init>")){
            //替换父类还要替换构造函数
            mv = ModifySuperClassMethodAdapter(newSuperName, Opcodes.ASM7, mv, access, name, descriptor)
        }
//        if(name.equals("attachBaseContext")){
//            mv = MethodAttachBaseContextAdapter(Opcodes.ASM7, mv, access, name, descriptor)
//        }

        return mv;
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
//        if(classData.superClasses.contains("android.app.Activity"))
//            return true;
        if(classData.className.equals("com.quik.testpad.test1.Test1Activity")){
            return true;
        }
        return false;
    }
}