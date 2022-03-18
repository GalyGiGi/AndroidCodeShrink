package cn.com.lbb.log_gradle_plugin.config;

import org.objectweb.asm.tree.MethodInsnNode;

public class Target {
    public final static Method[] METHOEDS = new Method[]{
            //cn.com.lbb.util.v
            new Method("cn/com/lbb/util/LogUtil", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V", "v"),
            new Method("cn/com/lbb/util/LogUtil", "(Ljava/lang/Throwable;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", "v"),
            new Method("cn/com/lbb/util/LogUtil", "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", "v")
    };

    public static boolean hit(MethodInsnNode target) {
        for (Method m : METHOEDS) {
            if (m.match(target)) {
                return true;
            }
        }
        return false;
    }

    static class Method {
        /**
         * The internal name of the method's owner class (see {@link
         * org.objectweb.asm.Type#getInternalName()}).
         *
         * <p>For methods of arrays, e.g., {@code clone()}, the array type descriptor.
         */
        String owner;
        /**
         * The method's descriptor (see {@link org.objectweb.asm.Type}).
         */
        String desc;
        /**
         * The method's name.
         */
        String name;

        public Method(String owner, String desc, String name) {
            this.owner = owner;
            this.desc = desc;
            this.name = name;
        }

        public boolean match(MethodInsnNode methodInsnNode) {
            return name.equals(methodInsnNode.name)
                    && owner.equals(methodInsnNode.owner)
                    && desc.equals(methodInsnNode.desc);
        }

        @Override
        public String toString() {
            return new StringBuilder(owner).append(".").append(name).toString();
        }
    }
}
