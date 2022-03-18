package cn.com.lbb.log_gradle_plugin.process;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cn.com.lbb.log_gradle_plugin.utils.LogUtil;

public class VerboseMethodVisitor extends MethodVisitor {
    public VerboseMethodVisitor(int api, MethodVisitor visitor) {
        super(api, visitor);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        LogUtil.print("visitAttribute:" + attribute.type);
        super.visitAttribute(attribute);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        LogUtil.print("-visitMaxs-  maxStack:" + maxStack + ",maxLocals:" + maxLocals);
        super.visitMaxs(maxStack, maxLocals);
    }


}
