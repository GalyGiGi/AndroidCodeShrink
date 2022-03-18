package cn.com.lbb.log_gradle_plugin.process;

import org.objectweb.asm.ClassWriter;

public interface ITransformer {
    void transform();

    void accept(ClassWriter classWriter);
}
