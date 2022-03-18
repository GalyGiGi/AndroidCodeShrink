package cn.com.lbb.log_gradle_plugin;

import org.objectweb.asm.Type;

import cn.com.lbb.log_gradle_plugin.utils.TypeUtil;

public class TypeWrapper {
    private final Type tp;

    public TypeWrapper(Type type) {
        tp = type;
    }

    public String getClassName() {
        switch (tp.getSort()) {
            case Type.ARRAY:
                return TypeUtil.ARRAY_REF;
            case Type.OBJECT:
                return TypeUtil.REF;
            case Type.BOOLEAN:
            case Type.BYTE:
            case Type.CHAR:
            case Type.INT:
            case Type.SHORT:
                return TypeUtil.SLOT;
            case Type.DOUBLE:
            case Type.LONG:
                return TypeUtil.SLOT_X2;
            default:
                return tp.getClassName();
        }

    }

    public Type rawType() {
        return tp;
    }
}
