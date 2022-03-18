package cn.com.lbb.log_gradle_plugin.process;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.com.lbb.log_gradle_plugin.TypeWrapper;
import cn.com.lbb.log_gradle_plugin.config.Target;
import cn.com.lbb.log_gradle_plugin.utils.LogUtil;
import cn.com.lbb.log_gradle_plugin.utils.TypeUtil;

public class DeleteMethodTransformer implements ITransformer {

    ClassReader reader;
    ClassNode clazz;

    public DeleteMethodTransformer(ClassReader reader) {
        this.reader = reader;
        clazz = new ClassNode(Opcodes.ASM5);
    }

    private void process(MethodNode method) {
        List<AbstractInsnNode> list = collectInsn(method);
        AbstractInsnNode last = null;
        int indexEnd = 0;
        for (AbstractInsnNode insn : list) {
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insn;
                if (Target.hit(methodInsnNode)) {
                    LogUtil.print("找到目标方法调用处----" + method.name);

                    TypeWrapper returnType = new TypeWrapper(Type.getReturnType(methodInsnNode.desc));
                    if (returnType.rawType() == Type.VOID_TYPE) {
                        last = insn;
                        LogUtil.print("找到终止指令,index:" + indexEnd);
                        break;
                    } else if (TypeUtil.SLOT.equals(returnType.getClassName())) {
                        AbstractInsnNode popInsn = insn.getNext();
                        if (popInsn.getOpcode() == Opcodes.POP) {
                            indexEnd++;
                            LogUtil.print("找到终止指令POP,index:" + indexEnd);
                            last = popInsn;
                            break;
                        } else {
                            LogUtil.print("没有找到终止指令POP,index:" + indexEnd);
                        }
                    } else if (TypeUtil.SLOT_X2.equals(returnType.getClassName())) {
                        AbstractInsnNode popInsn = insn.getNext();
                        if (popInsn.getOpcode() == Opcodes.POP2) {
                            indexEnd++;
                            LogUtil.print("找到终止指令POP2,index:" + indexEnd);
                            last = popInsn;
                            break;
                        } else {
                            LogUtil.print("没有找到终止指令POP2,index:" + indexEnd);
                        }
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
        OpStackProcessor stackUtil = new OpStackProcessor(clazz);
        stackUtil.revertStackOp(stack, last, method);
        LogUtil.print("先对最后一条指令逆操作");
        AbstractInsnNode curNode = last.getPrevious();
        int indexStart = indexEnd - 1;
        while (stack.size() > 0 && curNode != null) {
            boolean suc = stackUtil.revertStackOp(stack, curNode, method);
            if (!suc) {
                LogUtil.print("遇到不支持的字节码，放弃对这个方法的处理");
                return;
            }
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
            return;
        }

        //从起始指令开始到终止指令，全部删除
        for (int i = indexStart; i <= indexEnd; i++) {
            AbstractInsnNode cur = list.get(i);
            LogUtil.print("删除指令：" + cur + ",opcode:" + cur.getOpcode());
            method.instructions.remove(cur);
        }
        LogUtil.print("删除目标函数完成");
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
        reader.accept(clazz, ClassReader.EXPAND_FRAMES);
        for (MethodNode method : clazz.methods) {
            process(method);
        }
    }

    @Override
    public void accept(ClassWriter classWriter) {
        clazz.accept(classWriter);
    }
}
