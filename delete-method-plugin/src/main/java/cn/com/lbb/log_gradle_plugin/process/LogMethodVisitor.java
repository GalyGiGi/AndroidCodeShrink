package cn.com.lbb.log_gradle_plugin.process;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cn.com.lbb.log_gradle_plugin.utils.LogUtil;

public class LogMethodVisitor extends MethodVisitor {
    public LogMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    /**
     * 往onCreate函数中插入以下日志
     * <p>
     * E/ASM_CUSTOM_LOG: custom transform log
     */
    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLdcInsn("ASM_CUSTOM_LOG");
        mv.visitLdcInsn("custom transform log");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e",
                "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }


    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
    }
}
