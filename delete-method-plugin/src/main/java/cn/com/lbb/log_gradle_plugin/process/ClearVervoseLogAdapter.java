package cn.com.lbb.log_gradle_plugin.process;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.com.lbb.log_gradle_plugin.Slot;
import cn.com.lbb.log_gradle_plugin.utils.LogUtil;

public class ClearVervoseLogAdapter implements ITransformer {
    ClassReader reader;
    ClassNode clazz;

    public ClearVervoseLogAdapter(ClassReader reader) {
        this.reader = reader;
        clazz = new ClassNode(Opcodes.ASM5);
    }

    private void work() {
        reader.accept(clazz, ClassReader.EXPAND_FRAMES);
        for (MethodNode method : clazz.methods) {
            process(method);
        }
    }

    private ClassNode getClassNode() {
        return clazz;
    }

    private void process(MethodNode method) {
        List<AbstractInsnNode> list = collectInsn(method);
        AbstractInsnNode last = null;
        int indexEnd = 0;
        for (AbstractInsnNode insn : list) {
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insn;
//                LogUtil.print("methodInsnNode--opCode:" + methodInsnNode.getOpcode() + ",owner:" + methodInsnNode.owner + ",name:" + methodInsnNode.name);
                if (methodInsnNode.getOpcode() == Opcodes.INVOKESTATIC && methodInsnNode.owner.equals("android/util/Log") && methodInsnNode.name.
                        equals("i")) {
                    LogUtil.print("找到目标方法android.util.Log.i调用处----" + method.name);

                    //因为android.util.Log.i返回值是int，所以invokeStatic指令后面还有个pop
                    AbstractInsnNode popInsn = insn.getNext();
                    if (popInsn.getOpcode() == Opcodes.POP) {
                        indexEnd++;
                        LogUtil.print("找到终止指令POP,index:" + indexEnd);
                        last = popInsn;
                        break;
                    }
                }

            }
            indexEnd++;
        }
        if (last == null) {
            //LogUtil.print("没有找到终止指令，return");
            return;
        }
        //建立一个栈stack，从终止指令开始往上逆序遍历，根据指令对操作数栈的操作对stack进行反操作，直到stack清空
        LinkedList<String> stack = new LinkedList();
        stack.push(Slot.class.getName());
        LogUtil.print("先入栈1个Slog");
        AbstractInsnNode curNode = last.getPrevious();
        int indexStart = indexEnd - 1;
        OpStackProcessor stackUtil = new OpStackProcessor(clazz);
        while (stack.size() > 0 && curNode != null) {
            stackUtil.revertStackOp(stack, curNode, method);
            if (stack.size() > 0) {
                curNode = curNode.getPrevious();
                indexStart--;
            } else {
                indexStart--;
                break;
            }
        }
        if (stack.size() == 0) {
            LogUtil.print("找到起始指令：" + curNode);
        } else {
            LogUtil.print("没有找到起始指令，return");
        }

        //从起始指令开始到终止指令，全部删除
        for (int i = indexStart; i <= indexEnd; i++) {
            AbstractInsnNode cur = list.get(i);
            LogUtil.print("删除指令：" + cur + ",opcode:" + cur.getOpcode());
            method.instructions.remove(cur);
        }
        LogUtil.print("删除android.util.Log.i完成");
    }


    private List<AbstractInsnNode> collectInsn(MethodNode method) {
        InsnList insnList = method.instructions;
        if (insnList.size() <= 0) {
            return Collections.emptyList();
        }
        AbstractInsnNode cur = insnList.getFirst();
        List<AbstractInsnNode> result = new ArrayList<>(insnList.size());
        while (cur != null) {
//            LogUtil.print("字节码指令：" + cur.getType());
            result.add(cur);
            cur = cur.getNext();
        }
        return result;
    }

    @Override
    public void transform() {
        work();
    }

    @Override
    public void accept(ClassWriter classWriter) {
        getClassNode().accept(classWriter);
    }
}
