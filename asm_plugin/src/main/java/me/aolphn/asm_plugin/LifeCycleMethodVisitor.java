package me.aolphn.asm_plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LifeCycleMethodVisitor extends MethodVisitor {
    private String className;
    private String methodName;
    public LifeCycleMethodVisitor( MethodVisitor methodVisitor,String className,String methodName) {
        super(Opcodes.ASM5, methodVisitor);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("self define visit code:"+className+"."+methodName);
        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className+"------>"+methodName);
        String descriptor = "(Ljava/lang/String;Ljava/lang/String;)I";
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,"android/util/Log","i",descriptor,false);
        mv.visitInsn(Opcodes.POP);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }
}
