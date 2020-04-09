package me.aolphn.asm_plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LifeCycleClassVisitor extends ClassVisitor {
    private String className;
    private String superName;
    private boolean isClassChanged;
    public LifeCycleClassVisitor( ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access,name,descriptor,signature,exceptions);
//        System.out.println("check superName:"+superName);
        if ("androidx/appcompat/app/AppCompatActivity".equals(superName)) {
            if (name.equals("selfDefine")) {
                isClassChanged = true;
                return new LifeCycleMethodVisitor(mv,className,name);
            }
        }
        return mv;
    }

    public boolean isClassChanged(){
        return isClassChanged;
    }
}
