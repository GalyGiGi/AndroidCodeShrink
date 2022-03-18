package cn.com.lbb.log_gradle_plugin.process;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cn.com.lbb.log_gradle_plugin.utils.LogUtil;

public class LogClassVisitor extends ClassVisitor {
    // api是ASM的api版本，是一个枚举
    // 接收一个Classvisitor类型参数，这里其实属于代理模式，后面用到的时候会讲到
    public LogClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    // 遍历class中的方法，会回调到这个方法
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        LogUtil.print("visitMethod:" + name + " desp:" + descriptor);
        // 返回一个MethodVisitor，从字面意思就能看出用来hook方法的
        MethodVisitor visitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        // hook OnCreate方法
        if (name.equals("onCreate")) {
            return new LogMethodVisitor(Opcodes.ASM5, visitor);
        } else return new VerboseMethodVisitor(Opcodes.ASM5, visitor);
    }

}
