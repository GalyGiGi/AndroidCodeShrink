package cn.com.lbb.log_gradle_plugin;


import com.android.annotations.NonNull;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import cn.com.lbb.log_gradle_plugin.process.DeleteMethodTransformer;
import cn.com.lbb.log_gradle_plugin.process.ITransformer;
import cn.com.lbb.log_gradle_plugin.utils.LogUtil;

public class ZCTransform extends Transform {
    private Project project;
    private LogDeleteExtension extension;

    public ZCTransform(Project project, LogDeleteExtension extension) {
        this.project = project;
        this.extension = extension;
    }

    @Override
    public String getName() {
        return "ZCTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;//我们需要修改的是class文件，所以这里指定为CONTENT_CLAS
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;//指定我们这个自定义的Transform可使用的范围
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(@NonNull TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        if (!extension.isEnable()) {
            LogUtil.print("ZCTransform not enabled --- return");
            return;
        }
        TransformOutputProvider transformOutputProvider = transformInvocation.getOutputProvider();
        printCopyRight();
        visitInputs(transformInvocation);
        for (TransformInput input : transformInvocation.getInputs()) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                processDirectoryInputs(directoryInput, transformOutputProvider);
            }
            for (JarInput jarInput : input.getJarInputs()) {
                processJarInput(jarInput, transformOutputProvider);
            }
        }
    }

    private void visitInputs(TransformInvocation transformInvocation) {
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        for (TransformInput input : inputs) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
//                System.out.println("directoryInput: " + directoryInput);
                printFile(directoryInput.getFile());
            }

        }
    }

    static void printFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    printFile(file1);
                } else {
                    System.out.println("File = " + file1.getName());
                }
            }
        } else {
            System.out.println("File = " + file.getName());
        }
    }

    static void processJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        File dest = outputProvider.getContentLocation(
                jarInput.getFile().getAbsolutePath(),
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR);
        println("processJarInput " + dest);

        // to do some transform

        // 将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
        try {
            FileUtils.copyFile(jarInput.getFile(), dest);
        } catch (IOException e) {
            println("processJarInput io 异常");
        }
    }

    static void processDirectoryInputs(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        println("processDirectoryInputs start");
        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(),
                Format.DIRECTORY);

        // 建立文件夹
        FileUtils.mkdirs(dest);
        println("创建文件夹：" + dest);//创建文件夹：/Users/zengcheng/Documents/workspace_local/MyGradlePluginSample/app/build/intermediates/transforms/ZCTransform/release/0

        // to do some transform
        try {
            tranfromFiles(directoryInput.getFile());
        } catch (Exception e) {
            LogUtil.print("transform出错：" + e.toString());
            e.printStackTrace();
        }

        // 将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
        try {
            FileUtils.copyDirectory(directoryInput.getFile(), dest);
        } catch (IOException e) {
            e.printStackTrace();
            println("processDirectoryInputs io 异常");
        }
        println("processDirectoryInputs end");
    }

    static void transformClassFile(File file) {
        if (!file.isFile()) throw new IllegalArgumentException("only file support");
        if (file.getName().endsWith(".class")) {
            println("发现class文件 " + file.getName() + ",path=" + file.getPath() + ",准备transform：");

            // ClassReader读取class文件的内容
            ClassReader classReader = null;
            try {
                classReader = new ClassReader(new FileInputStream(file.getPath()));
            } catch (IOException e) {
                println("创建ClassReader异常：" + e.getMessage());
            }
            if (classReader == null) {
                return;
            }

            ITransformer transformer = new DeleteMethodTransformer(classReader);
            transformer.transform();

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            transformer.accept(classWriter);
            byte[] bytes = classWriter.toByteArray();
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file.getPath());
                outputStream.write(bytes);
            } catch (Exception e) {
                println("写文件异常：" + e.getMessage());
            } finally {
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            // ClassWriter用于回写修改后的class内容
//            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//            // ClassWriter其实也是ClassVisitor的子类，这样就可构造出一个自定义的ClassVisitor
//            LogClassVisitor classVisitor = new LogClassVisitor(Opcodes.ASM5, classWriter);
//            // ClassVisitor处理class
//            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
//            // 处理后的数据写回文件
//            byte[] bytes = classWriter.toByteArray();
//            FileOutputStream outputStream = null;
//            try {
//                outputStream = new FileOutputStream(file.getPath());
//                outputStream.write(bytes);
//            } catch (Exception e) {
//                println("写文件异常：" + e.getMessage());
//            } finally {
//                if (null != outputStream) {
//                    try {
//                        outputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

        }
    }

    /**
     * 处理class文件
     */
    static void tranfromFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    tranfromFiles(file1);
                } else {
                    transformClassFile(file1);
                }
            }
        } else {
            transformClassFile(file);
        }
    }

    static void println(String msg) {
        System.out.println(msg);
    }

    static void printCopyRight() {
        System.out.println();
        System.out.println("******************************************************************************");
        System.out.println("******                                                                  ******");
        System.out.println("******                欢迎使用 ZcTransform 编译插件                    ******");
        System.out.println("******                                                                  ******");
        System.out.println("******************************************************************************");
        System.out.println();
    }
}
