package cn.com.lbb.log_gradle_plugin.process;

import org.gradle.internal.impldep.org.testng.Assert;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Iterator;
import java.util.LinkedList;

import cn.com.lbb.log_gradle_plugin.Slot;
import cn.com.lbb.log_gradle_plugin.Slot_x2;
import cn.com.lbb.log_gradle_plugin.TypeWrapper;
import cn.com.lbb.log_gradle_plugin.annotation.Tested;
import cn.com.lbb.log_gradle_plugin.utils.LogUtil;
import cn.com.lbb.log_gradle_plugin.utils.TypeUtil;

public class OpStackProcessor {

    private ClassNode clazz;

    public OpStackProcessor(ClassNode clazz) {
        this.clazz = clazz;
    }

    public boolean revertStackOp(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        if (curNode == null) {
            LogUtil.print("warning curNode == null");
        }
        LogUtil.print("revertStack start...opcode:" + curNode.getOpcode());
        LogUtil.print("当前栈快照：" + snapShot(stack));
        boolean support = true;
        switch (curNode.getOpcode()) {
            case Opcodes.POP://pop 指令效果是单 slot 参数(像 int，float)出栈
                pop(stack, curNode, method);
                break;
            case Opcodes.POP2://pop2 指令效果是双 slot 参数(像 double,long)出栈
                pop2(stack, curNode, method);
                break;
            //invokestatic 要看方法的参数和返回值，正常效果是对应方法的参数从右至左依次出栈，方法返回值 int 入栈
            case Opcodes.INVOKESTATIC:
                invokeStatic(stack, curNode, method);
                break;
            //指令正常方法调用参数依次从右至左依次出栈，然后 this 对象出栈，最后方法返回值 String 入栈
            case Opcodes.INVOKEVIRTUAL:
                invokeVirtual(stack, curNode, method);
                break;

            case Opcodes.INVOKESPECIAL:
                invokeSpecial(stack, curNode, method);
                break;
            case Opcodes.LDC:
                ldc(stack, curNode, method);
                break;
            case Opcodes.ALOAD:
                aload(stack, curNode, method);
                break;
            case Opcodes.ASTORE:
                astore(stack, curNode, method);
                break;
            case Opcodes.DUP:
                dup(stack, curNode, method);
                break;
            case Opcodes.DUP2:
                dup2(stack, curNode, method);
                break;
            case Opcodes.NEW:
                New(stack, curNode, method);
                break;
            case Opcodes.ANEWARRAY:
                anewArray(stack, curNode, method);
                break;
            case Opcodes.AASTORE:
                aastore(stack, curNode, method);
                break;
            case Opcodes.AALOAD:
                aaload(stack, curNode, method);
                break;
            case Opcodes.ILOAD:
                iload(stack, curNode, method);
                break;
//            case Opcodes.GETSTATIC:
//                getStatic(stack, curNode, method);
//                break;
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
                iconst(stack, curNode, method);
                break;
            case Opcodes.LADD:
                ladd(stack, curNode, method);
                break;
            case Opcodes.LLOAD:
                lload(stack, curNode, method);
                break;
            case Opcodes.LSTORE:
                lstore(stack, curNode, method);
                break;
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
                lconst(stack, curNode, method);
                break;
            case Opcodes.PUTFIELD:
                putfield(stack, curNode, method);
                break;
            case Opcodes.GETFIELD:
                getfield(stack, curNode, method);
                break;
            case Opcodes.DUP2_X1:
                dup2_x1(stack, curNode, method);
                break;
            case Opcodes.DUP_X1:
                dup_x1(stack, curNode, method);
                break;
            default:
                support = false;
                LogUtil.print("!!!遇到不支持的指令：" + curNode.getOpcode());
                break;
        }
        return support;
    }

    /**
     * Operand Stack
     * ..., arrayref, index, value →
     * <p>
     * ...
     * <p>
     * <p>
     * The arrayref must be of type reference and must refer to an array whose components are of type reference.
     * The index must be of type int, and value must be of type reference.
     * The arrayref, index, and value are popped from the operand stack.
     *
     * @param stack
     * @param curNode
     * @param method
     */
    private void aastore(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-aastore-    start");
        stack.push(TypeUtil.ARRAY_REF);
        LogUtil.print("3.1入栈ARRAY_REF");
        stack.push(TypeUtil.SLOT);
        LogUtil.print("3.2入栈SLOT");
        stack.push(TypeUtil.REF);
        LogUtil.print("3.3入栈REF");
        LogUtil.print("-aastore-    end");
    }

    /**
     * Operand Stack
     * ..., arrayref, index →
     * <p>
     * ..., value
     *
     * @param stack
     * @param curNode
     * @param method
     */
    private void aaload(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-aaload-    start");
        String pop = stack.pop();
        LogUtil.print("3.1 应该出栈一个引用类型,实际出栈类型：" + pop);
        stack.push(TypeUtil.ARRAY_REF);
        LogUtil.print("3.2 入栈一个数组引用类型");
        stack.push(TypeUtil.SLOT);
        LogUtil.print("3.3 入栈一个int类型");
        LogUtil.print("-aaload-    end");
    }

    /**
     * Load int from local variable
     * <p>
     * Operand Stack
     * ... →
     * <p>
     * ..., value
     *
     * @param stack
     * @param curNode
     * @param method
     */
    private void iload(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        String pop = stack.pop();
        LogUtil.print("-iload- 应该出栈一个int类型，实际出栈类型：" + pop);
    }

    /**
     * Get static field from class
     *
     * Operand Stack
     * ..., →
     * <p>
     * ..., value
     *
     * The value of the class or interface field is fetched and pushed onto the operand stack.
     *
     * @param stack
     * @param curNode
     * @param method
     */
    private void getStatic(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {

    }

    /**
     * .., objectref, value →
     * <p>
     * ...
     * <p>
     * putfield 指令将栈顶的两个元素 属性 和 this 出栈
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void putfield(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-putfield-    start");
        stack.push(clazz.name);
        LogUtil.print("2.1 入栈this对象的类型：" + clazz.name);
        FieldInsnNode fieldInsnNode = (FieldInsnNode) curNode;
        TypeWrapper tw = new TypeWrapper(Type.getType(fieldInsnNode.desc));
        stack.push(tw.getClassName());
        LogUtil.print("2.2 入栈属性，类型为" + tw.getClassName());
        LogUtil.print("-putfield-    end");
    }

    /**
     * getfield指令把this出栈，然后把读到的属性入栈
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void getfield(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-getfield-    start");
        FieldInsnNode fieldInsnNode = (FieldInsnNode) curNode;
        String top = stack.pop();
        TypeWrapper tw = new TypeWrapper(Type.getType(fieldInsnNode.desc));
        LogUtil.print("2.1 出栈读到的属性，" + tw.getClassName() + ",检查实际出栈类型：" + top);
        stack.push(clazz.name);
        LogUtil.print("2.2入栈this对象的类型：" + clazz.name);
        LogUtil.print("-getfield-    end");
    }

    /**
     * ladd指令的作用是弹出栈顶的两个long型数据相加，并把结果入栈（效果就是往操作数栈出栈2个Slot_x2，再入栈1个Slot_x2）
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void ladd(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-ladd-    start");
        LogUtil.print("3.1出栈Slot_x2的result,检查实际出栈类型：" + stack.pop());
        stack.push(Slot_x2.class.getName());
        LogUtil.print("3.2入栈1个Slot_x2");
        stack.push(Slot_x2.class.getName());
        LogUtil.print("3.3入栈1个Slot_x2");
        LogUtil.print("-ladd-    end");

    }

    /**
     * lload_n 指令的作用是把第n个局部变量的long型数据放入操作数栈（效果就是往操作数栈入栈1个Slot_x2）
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void lload(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-lload- 出栈第1个Slot_x2,检查实际出栈类型：" + stack.pop());
    }

    /**
     * lstore_n 指令的作用是把操作数栈顶的long型数据弹出，赋值给第n个局部变量（效果就是往操作数栈出栈1个Slot_x2）
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void lstore(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        stack.push(Slot_x2.class.getName());
        LogUtil.print("-lstore-    入栈1个Slot_x2");
    }

    /**
     * lconst_0 / lconst_1 指令的作用是把long型常量0/1入栈（效果就是往操作数栈入栈1个Slot_x2）
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void lconst(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-lconst- 出栈第1个Slot_x2,检查实际出栈类型：" + stack.pop());
    }

    /**
     * pop2 指令效果是双 slot 参数(像 double,long)出栈
     */
    private void pop2(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        stack.push(Slot_x2.class.getName());
        LogUtil.print("-pop2-入栈1个Slot_x2");
    }

    /**
     * pop 指令效果是单 slot 参数(像 int，float)出栈
     */
    @Tested
    private void pop(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        stack.push(Slot.class.getName());
        LogUtil.print("-pop-入栈1个Slot");
    }

    /**
     * iconst_mi iconst_0 iconst_1 iconst_2 iconst_3 iconst_4 iconst_5
     * 把整数-1，0，1，2，3，4，5入栈
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void iconst(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        String top = stack.pop();
        LogUtil.print("-iconst-,栈顶元素出栈整形数据" + ",检查实际出栈类型：" + top);
    }

    /**
     * 只是创建了一个类实例引用，将这个引用压入操作数栈顶
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void New(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        TypeInsnNode typeNode = (TypeInsnNode) curNode;
        TypeWrapper type = new TypeWrapper(Type.getObjectType(typeNode.desc));
        String top = stack.pop();
        LogUtil.print("-new-,栈顶元素出栈类型：" + type.getClassName() + ",检查实际出栈类型：" + top);
    }

    /**
     * ..., count →
     * <p>
     * ..., arrayref
     * <p>
     * The count must be of type int.
     *
     * @param stack
     * @param curNode
     * @param method
     */
    private void anewArray(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        String pop = stack.pop();
        LogUtil.print("2.1-anewArray-,出栈一个array" + ",实际出栈类型：" + pop);
        stack.push(TypeUtil.SLOT);
        LogUtil.print("2.2-anewArray-,入栈一个slot");
    }

    /**
     * Form 1:
     * <p>
     * ..., value3, value2, value1 →
     * <p>
     * ..., value1, value3, value2, value1
     * <p>
     * where value1, value2, and value3 are all values of a category 1 computational type (§2.11.1).
     * <p>
     * Form 2:
     * <p>
     * ..., value2, value1 →
     * <p>
     * ..., value1, value2, value1
     * <p>
     * where value1 is a value of a category 1 computational type and value2 is a value of a category 2 computational type (§2.11.1).
     */
    private void dup_x2(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-dup_x2-  start");
        String v1 = stack.get(0);
        String v2 = stack.get(1);
        String v3 = "";
        if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category2) {
            LogUtil.print("form 2");
            String top = stack.pop();
            LogUtil.print("5.1出栈v1:" + top);

            Assert.assertEquals(v1, top);

            top = stack.pop();
            LogUtil.print("5.2出栈v2:" + top);

            Assert.assertEquals(v2, top);

            top = stack.pop();
            LogUtil.print("5.3出栈v1:" + top);

            Assert.assertEquals(v1, top);

            stack.push(v2);
            LogUtil.print("5.4入栈v2:" + v2);

            stack.push(v1);
            LogUtil.print("5.5入栈v1:" + v1);
        } else if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v3 = stack.get(2)) == TypeUtil.ComputationCategory.Category1) {
            String top = stack.pop();
            LogUtil.print("7.1出栈v1:" + top);
            Assert.assertEquals(v1, top);


            top = stack.pop();
            LogUtil.print("7.2出栈v2:" + top);
            Assert.assertEquals(v2, top);


            top = stack.pop();
            LogUtil.print("7.3出栈v3:" + top);
            Assert.assertEquals(v3, top);


            top = stack.pop();
            LogUtil.print("7.4出栈v1:" + top);
            Assert.assertEquals(v1, top);


            stack.push(v3);
            LogUtil.print("7.5入栈v3:" + v3);

            stack.push(v2);
            LogUtil.print("7.6入栈v2:" + v2);

            stack.push(v1);
            LogUtil.print("7.7入栈v1:" + v1);
        }
        LogUtil.print("-dup_x2-  end");
    }

    /**
     * .., value2, value1 →
     * <p>
     * ..., value1, value2, value1
     * <p>
     * dup_x1指令：出栈v1,出栈v2,入栈v1,入栈v2,入栈v1
     */
    @Tested
    private void dup_x1(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-dup_x1-  start");
        String v1 = stack.pop();
        LogUtil.print("5.1出栈v1:" + v1);

        String v2 = stack.pop();
        LogUtil.print("5.2出栈v2:" + v2);

        v1 = stack.pop();
        LogUtil.print("5.3出栈v1:" + v1);

        stack.push(v2);
        LogUtil.print("5.4入栈v2:" + v2);

        stack.push(v1);
        LogUtil.print("5.5入栈v1:" + v1);

        LogUtil.print("-dup_x1-  end");
    }

    /**
     * Form 1:
     * <p>
     * ..., value4, value3, value2, value1 →
     * <p>
     * ..., value2, value1, value4, value3, value2, value1
     * <p>
     * where value1, value2, value3, and value4 are all values of a category 1 computational type (§2.11.1).
     * <p>
     * Form 2:
     * <p>
     * ..., value3, value2, value1 →
     * <p>
     * ..., value1, value3, value2, value1
     * <p>
     * where value1 is a value of a category 2 computational type and value2 and value3 are both values of a category 1 computational type (§2.11.1).
     * <p>
     * Form 3:
     * <p>
     * ..., value3, value2, value1 →
     * <p>
     * ..., value2, value1, value3, value2, value1
     * <p>
     * where value1 and value2 are both values of a category 1 computational type and value3 is a value of a category 2 computational type (§2.11.1).
     * <p>
     * Form 4:
     * <p>
     * ..., value2, value1 →
     * <p>
     * ..., value1, value2, value1
     * <p>
     * where value1 and value2 are both values of a category 2 computational type (§2.11.1).
     */
    private void dup2_x2(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-dup2_x2-  start");
        String v1 = stack.get(0);
        String v2 = stack.get(1);
        String v3 = "";
        String v4 = "";
        String v5 = "";

        if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category2
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category2) {
            LogUtil.print("-form 4");
            String top = stack.pop();
            LogUtil.print("5.1出栈v1:" + top);

            Assert.assertEquals(v1, top);

            top = stack.pop();
            LogUtil.print("5.2出栈v2:" + top);

            Assert.assertEquals(v2, top);

            top = stack.pop();
            LogUtil.print("5.3出栈v1:" + top);

            Assert.assertEquals(v1, top);

            stack.push(v2);
            LogUtil.print("5.4入栈v2:" + v2);

            stack.push(v1);
            LogUtil.print("5.5入栈v1:" + v1);
        } else if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v3 = stack.get(2)) == TypeUtil.ComputationCategory.Category2) {
            LogUtil.print("-form 3");
            String top = stack.pop();
            LogUtil.print("8.1出栈v1:" + top);
            Assert.assertEquals(v1, top);

            top = stack.pop();
            LogUtil.print("8.2出栈v2:" + top);
            Assert.assertEquals(v2, top);

            top = stack.pop();
            LogUtil.print("8.3出栈v3:" + top);
            Assert.assertEquals(v3, top);

            top = stack.pop();
            LogUtil.print("8.4出栈v1:" + top);
            Assert.assertEquals(v1, top);

            top = stack.pop();
            LogUtil.print("8.5出栈v2:" + top);
            Assert.assertEquals(v2, top);

            stack.push(v3);
            LogUtil.print("8.6入栈v3:");

            stack.push(v2);
            LogUtil.print("8.7入栈v2:");

            stack.push(v1);
            LogUtil.print("8.8入栈v1:");

        } else if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category2
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v3 = stack.get(2)) == TypeUtil.ComputationCategory.Category1) {
            LogUtil.print("-form 2");
            String top = stack.pop();
            LogUtil.print("7.1出栈v1:" + top);
            Assert.assertEquals(v1, top);


            top = stack.pop();
            LogUtil.print("7.2出栈v2:" + top);
            Assert.assertEquals(v2, top);


            top = stack.pop();
            LogUtil.print("7.3出栈v3:" + top);
            Assert.assertEquals(v3, top);


            top = stack.pop();
            LogUtil.print("7.4出栈v1:" + top);
            Assert.assertEquals(v1, top);


            stack.push(v3);
            LogUtil.print("7.5入栈v3:" + v3);

            stack.push(v2);
            LogUtil.print("7.6入栈v2:" + v2);

            stack.push(v1);
            LogUtil.print("7.7入栈v1:" + v1);
        } else if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v3 = stack.get(2)) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v4 = stack.get(3)) == TypeUtil.ComputationCategory.Category1) {
            LogUtil.print("-form 2");
            String top = stack.pop();
            LogUtil.print("10.1出栈v1:" + top);
            Assert.assertEquals(v1, top);


            top = stack.pop();
            LogUtil.print("10.2出栈v2:" + top);
            Assert.assertEquals(v2, top);


            top = stack.pop();
            LogUtil.print("10.3出栈v3:" + top);
            Assert.assertEquals(v3, top);


            top = stack.pop();
            LogUtil.print("10.4出栈v4:" + top);
            Assert.assertEquals(v4, top);

            LogUtil.print("10.5出栈v1:" + top);
            Assert.assertEquals(v1, top);


            top = stack.pop();
            LogUtil.print("10.6出栈v2:" + top);
            Assert.assertEquals(v2, top);

            stack.push(v4);
            LogUtil.print("10.7入栈v4:");

            stack.push(v3);
            LogUtil.print("10.8入栈v3:");

            stack.push(v2);
            LogUtil.print("10.9入栈v2:");

            stack.push(v1);
            LogUtil.print("10.10入栈v1:");
        }
        LogUtil.print("-dup2_x2-  end");

    }

    /**
     * Form 1:
     * <p>
     * ..., value3, value2, value1 →
     * <p>
     * ..., value2, value1, value3, value2, value1
     * <p>
     * where value1, value2, and value3 are all values of a category 1 computational type (§2.11.1).
     * <p>
     * Form 2:
     * <p>
     * ..., value2, value1 →
     * <p>
     * ..., value1, value2, value1
     * <p>
     * where value1 is a value of a category 2 computational type and value2 is a value of a category 1 computational type (§2.11.1).
     *
     * @param stack
     * @param curNode
     * @param method
     */
    private void dup2_x1(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-dup2_x1-  start");
        String v1 = stack.get(0);
        String v2 = stack.get(1);
        String v3 = stack.get(2);
        LogUtil.print("检查stack元素v1:" + v1 + ",v2:" + v2 + ",v3:" + v3);
        if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category2
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category1) {
            LogUtil.print("form2");
            String top = stack.pop();
            LogUtil.print("5.1出栈v1:" + top);

            top = stack.pop();
            LogUtil.print("5.2出栈v2:" + top);

            top = stack.pop();
            LogUtil.print("5.3出栈v1:" + top);

            stack.push(v2);
            LogUtil.print("5.4入栈v2:" + v2);

            stack.push(v1);
            LogUtil.print("5.5入栈v1:" + v1);
        } else if (TypeUtil.coputeCategory(v1) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v2) == TypeUtil.ComputationCategory.Category1
                && TypeUtil.coputeCategory(v3) == TypeUtil.ComputationCategory.Category1) {
            LogUtil.print("form2");
            String top = stack.pop();
            LogUtil.print("8.1出栈v1:" + top);
            top = stack.pop();
            LogUtil.print("8.2出栈v2:" + top);
            top = stack.pop();
            LogUtil.print("8.3出栈v3:" + top);
            top = stack.pop();
            LogUtil.print("8.4出栈v1:" + top);
            top = stack.pop();
            LogUtil.print("8.5出栈v2:" + top);
            stack.push(v3);
            LogUtil.print("8.6入栈v3:" + v3);
            stack.push(v2);
            LogUtil.print("8.7入栈v2:" + v2);
            stack.push(v1);
            LogUtil.print("8.8入栈v1:" + v1);
        }


        LogUtil.print("-dup2_x1-  end");
    }

    /**
     * DUP2指令会复制栈顶元素,并入栈
     */
    @Tested
    private void dup2(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        String top = stack.pop();
        LogUtil.print("-dup2-,出栈slot_x2元素，检查实际出栈类型：" + top);
    }

    /**
     * DUP指令会复制栈顶元素,并入栈
     */
    @Tested
    private void dup(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        String top = stack.pop();
        LogUtil.print("-dup-,栈顶元素出栈:" + top + ",检查当前栈顶元素：" + stack.peekFirst());
    }

    /**
     * Operand Stack
     * ..., objectref →
     * <p>
     * ...
     */
    private void astore(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        stack.push(TypeUtil.REF);
        LogUtil.print("-astore-入栈一个引用类型");
    }

    /**
     * 处理aload_和aload指令
     * <p>
     * aload_0,aload_1,aload_2,aload_3
     * <p>
     * aload 5
     * aload 6
     * ...
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void aload(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        VarInsnNode varNode = (VarInsnNode) curNode;
        int indexOfLocal = varNode.var < 4 ? varNode.var : varNode.var - 1;
        LocalVariableNode lacalVar = getLocalVar(method, indexOfLocal);
        TypeWrapper type = new TypeWrapper(Type.getType(lacalVar.desc));
        String top = stack.pop();
        LogUtil.print("-aload_" + varNode.var + "-,出栈本地变量类型：" + type.getClassName() + ",检查出栈的类型：" + top);
    }

    /**
     * 要看方法的参数和返回值，正常效果是对应方法的参数从右至左依次出栈，方法返回值 int 入栈
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void invokeStatic(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-invokestatic-   start");
        MethodInsnNode methodInvoke = (MethodInsnNode) curNode;
        TypeWrapper returnType = new TypeWrapper(Type.getReturnType(methodInvoke.desc));
        Type[] argumentTypes = Type.getArgumentTypes(methodInvoke.desc);
        if (returnType.rawType() != Type.VOID_TYPE) {
            String top = stack.pop();
            LogUtil.print("先出栈返回值类型：" + returnType.getClassName() + ",检查出栈的类型：" + top);
            if (!returnType.getClassName().equals(top)) {
                LogUtil.print("弹出的返回值类型不在栈里面!!!");
                return;
            }
        }

        for (Type argument : argumentTypes) {
            LogUtil.print("从左往右入栈参数类型:" + argument.getClassName());
            stack.push(new TypeWrapper(argument).getClassName());
        }
        LogUtil.print("-invokestatic-   end");
    }

    /**
     * 该指令是向栈中放入一个 int,float 或 String 常量
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void ldc(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LdcInsnNode ldc = (LdcInsnNode) curNode;
        String top = stack.pop();
        LogUtil.print("-LDC-，出栈一个int,float 或 String 常量，类型为：" + ldc.cst.getClass().getName() + ",检查出栈的类型：" + top);
//        if (!top.equals(ldc.cst.getClass().getName())) {
//            LogUtil.print("-LDC- 操作结果不符预期！！！");
//        }
    }

    /**
     * 该指令对于正常方法调用参数依次从右至左依次出栈，然后 this 对象出栈，最后方法返回值入栈。
     *
     * @param stack
     * @param curNode
     * @param method
     */
    @Tested
    private void invokeVirtual(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        LogUtil.print("-invokevirtual-  start");
        MethodInsnNode methodInvoke = (MethodInsnNode) curNode;
        TypeWrapper returnType = new TypeWrapper(Type.getReturnType(methodInvoke.desc));
        Type[] argumentTypes = Type.getArgumentTypes(methodInvoke.desc);
        if (returnType.rawType() != Type.VOID_TYPE) {
            String top = stack.pop();
            LogUtil.print("出栈返回值类型：" + returnType.getClassName() + ",检查出栈的类型：" + top);
        }
        stack.push(methodInvoke.owner);
        LogUtil.print("入栈this对象的类型：" + methodInvoke.owner);
        for (Type argument : argumentTypes) {
            LogUtil.print("从左往右入栈参数类型:" + argument.getClassName());
            stack.push(new TypeWrapper(argument).getClassName());
        }
        LogUtil.print("-invokevirtual-  end");
    }

    /**
     * 这个指令用于调用私有方法，构造函数，父类的方法.
     * <p>
     * 效果为：先把栈顶元素出栈，然后把返回值入栈
     * <p>
     * 如果包含这个指令的方法本身没有返回值则会跟一个pop 指令弹出。
     * <p>
     * 除了 invokespecial 之外，其它方法调用指令所消耗的操作数栈元素是根据调用类型以及目标方法描述符来确定的.
     */
    @Tested
    private void invokeSpecial(LinkedList<String> stack, AbstractInsnNode curNode, MethodNode method) {
        MethodInsnNode methodInvoke = (MethodInsnNode) curNode;
        TypeWrapper returnType = new TypeWrapper(Type.getReturnType(methodInvoke.desc));
        if (returnType.rawType() != Type.VOID_TYPE) {
            String top = stack.pop();
            LogUtil.print("-invokespecial-,先出栈返回值类型：" + returnType.getClassName() + ",检查出栈的类型：" + top);
        }
        stack.push(methodInvoke.owner);
        LogUtil.print("-invokespecial-,入栈this对象的类型：" + methodInvoke.owner);
        Type[] argumentTypes = Type.getArgumentTypes(methodInvoke.desc);
        for (Type argument : argumentTypes) {
            LogUtil.print("-invokespecial-,从左往右入栈参数类型:" + argument.getClassName());
            stack.push(new TypeWrapper(argument).getClassName());
        }
    }

    private String snapShot(LinkedList<String> stack) {
        if (stack.isEmpty()) {
            return "[]";
        }
        Iterator<String> itr = stack.iterator();
        StringBuilder sb = new StringBuilder();
        while (itr.hasNext()) {
            String cur = itr.next();
            sb.append("[").append(cur).append("]").append("<");
        }
        return sb.toString();
    }

    private LocalVariableNode getLocalVar(MethodNode methodNode, int index) {
        return methodNode.localVariables.get(index);
    }
}
