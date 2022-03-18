package cn.com.lbb.my_gradle_plugin_sample;

import android.app.Activity;
import android.util.Log;

import java.util.Random;

import cn.com.lbb.util.LogUtil;

public class MainActivity extends Activity {
    final static int ID = 1;
    final static int MAX_ID = Integer.MAX_VALUE - 1;
    final static char NAME = '1';
    final static boolean DEBUG = true;
    final static long AGE = Integer.MAX_VALUE + 1;
    final static long SMALL_AGE = 2;
    private String nameStr;
    private int id;
    private char name;
    private boolean debug;
    private long age;
    private long smallAge;
    private static final String TAG = "MainActivity";
    private static final String SUB_TAG = "sub";


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Log.i("MainActivity", "-onCreate from logi");
//        Log.e("MainActivity", "-onCreate from loge");
//    }

    /**
     * 0 ldc #3 <tag>
     * 2 ldc #4 <msg>
     * 4 iconst_0
     * 5 anewarray #5 <java/lang/Object>
     * 8 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 11 return
     */
    private void invokeLog() {
        LogUtil.v("tag", "msg");
    }

    /**
     * 0 ldc #3 <tag>                                 入栈字符串tag
     * 2 ldc #4 <msg>                                 入栈字符串msg
     * 4 iconst_1                                     入栈int值1
     * 5 anewarray #5 <java/lang/Object>              出栈int类型的count,入栈arrayref
     * 8 dup                                          拷贝了一个栈顶元素，也就是arrayref
     * 9 iconst_0                                     入栈int
     * 10 iconst_2                                    入栈int
     * 11 invokestatic #7 <java/lang/Integer.valueOf : (I)Ljava/lang/Integer;>   出栈参数类型int，入栈返回值类型Integer
     * 14 aastore                                                                出栈引用类型，出栈int,出栈arrayref
     * 15 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V> 从右往左出栈参数类型
     * 18 return
     */
    private void invokeLog2() {
        LogUtil.v("tag", "msg", 2);
    }

    /**
     * 0 ldc2_w #8 <4>
     * 3 lstore_1
     * 4 iconst_3
     * 5 istore_3
     * 6 ldc #10 <123>
     * 8 astore 4
     * 10 ldc #11 <456>
     * 12 astore 5
     * 14 ldc #13 <MainActivity>
     * 16 new #14 <java/lang/StringBuilder>
     * 19 dup
     * 20 invokespecial #15 <java/lang/StringBuilder.<init> : ()V>
     * 23 ldc #16 <subGateway IP is >
     * 25 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 28 aload 5
     * 30 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 33 invokevirtual #18 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 36 iconst_0
     * 37 anewarray #5 <java/lang/Object>
     * 40 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 43 return
     */
    private void invokeLog3() {
        long id = 4;
        int age = 3;
        String ip = "123";
        String gateIp = "456";
        LogUtil.v(TAG, SUB_TAG + "Gateway IP is " + gateIp);
    }

    /**
     * 0 ldc2_w #8 <4>
     * 3 lstore_1
     * 4 iconst_3
     * 5 istore_3
     * 6 ldc #10 <123>
     * 8 astore 4
     * 10 ldc #13 <MainActivity>
     * 12 new #14 <java/lang/StringBuilder>
     * 15 dup
     * 16 invokespecial #15 <java/lang/StringBuilder.<init> : ()V>
     * 19 ldc #16 <subGateway IP is >
     * 21 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 24 ldc #19 <1>
     * 26 dup
     * 27 astore 5
     * 29 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 32 ldc #19 <1>
     * 34 dup
     * 35 astore 4
     * 37 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 40 iconst_4
     * 41 dup
     * 42 istore_3
     * 43 invokevirtual #20 <java/lang/StringBuilder.append : (I)Ljava/lang/StringBuilder;>
     * 46 invokevirtual #18 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 49 iconst_0
     * 50 anewarray #5 <java/lang/Object>
     * 53 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 56 return
     */
    private void astore() {
        long id = 4;
        int age = 3;
        String ip = "123";
        String gateIp;
        LogUtil.v(TAG, SUB_TAG + "Gateway IP is " + (gateIp = "1") + (ip = "1") + (age = 4));
    }

    /**
     * 验证通过
     *
     * 0 ldc #11 <456>
     * 2 astore_1
     * 3 ldc #13 <MainActivity>
     * 5 new #14 <java/lang/StringBuilder>
     * 8 dup
     * 9 invokespecial #15 <java/lang/StringBuilder.<init> : ()V>
     * 12 ldc #16 <subGateway IP is >
     * 14 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 17 aload_1
     * 18 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 21 invokevirtual #18 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 24 iconst_0
     * 25 anewarray #5 <java/lang/Object>
     * 28 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 31 return
     */
//    private void aload_1() {
//        String gateIp = "456";
//        LogUtil.v(TAG, SUB_TAG + "Gateway IP is " + gateIp);
//    }

    /**
     * 验证通过
     *
     * 0 iconst_2
     * 1 istore_1
     * 2 ldc #11 <456>
     * 4 astore_2
     * 5 ldc #13 <MainActivity>
     * 7 new #14 <java/lang/StringBuilder>
     * 10 dup
     * 11 invokespecial #15 <java/lang/StringBuilder.<init> : ()V>
     * 14 ldc #16 <subGateway IP is >
     * 16 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 19 aload_2
     * 20 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 23 invokevirtual #18 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 26 iconst_0
     * 27 anewarray #5 <java/lang/Object>
     * 30 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 33 return
     */
//    private void aload_2() {
//        int age = 2;
//        String gateIp = "456";
//        LogUtil.v(TAG, SUB_TAG + "Gateway IP is " + gateIp);
//    }

    /**
     *
     * 验证通过
     *
     * 0 ldc #19
     * 2 astore_1
     * 3 iconst_2
     * 4 istore_2
     * 5 ldc #11 <456>
     * 7 astore_3
     * 8 ldc #13 <MainActivity>
     * 10 new #14 <java/lang/StringBuilder>
     * 13 dup
     * 14 invokespecial #15 <java/lang/StringBuilder.<init> : ()V>
     * 17 ldc #16 <subGateway IP is >
     * 19 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 22 aload_3
     * 23 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 26 invokevirtual #18 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 29 iconst_0
     * 30 anewarray #5 <java/lang/Object>
     * 33 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 36 return
     */
//    private void aload_3() {
//        String ip = "";
//        int age = 2;
//        String gateIp = "456";
//        LogUtil.v(TAG, SUB_TAG + "Gateway IP is " + gateIp);
//    }

    /**
     * 验证通过
     *
     * 0 ldc2_w #20 <3>
     * 3 lstore_1
     * 4 ldc #19
     * 6 astore_3
     * 7 iconst_2
     * 8 istore 4
     * 10 ldc #11 <456>
     * 12 astore 5
     * 14 ldc #13 <MainActivity>
     * 16 new #14 <java/lang/StringBuilder>
     * 19 dup
     * 20 invokespecial #15 <java/lang/StringBuilder.<init> : ()V>
     * 23 ldc #16 <subGateway IP is >
     * 25 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 28 aload 5
     * 30 invokevirtual #17 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 33 invokevirtual #18 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 36 iconst_0
     * 37 anewarray #5 <java/lang/Object>
     * 40 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 43 return
     */
//    private void aload_4() {
//        long id = 3;
//        String ip = "";
//        int age = 2;
//        String gateIp = "456";
//        LogUtil.v(TAG, SUB_TAG + "Gateway IP is " + gateIp);
//    }


    /**
     * 0 aload_0
     * 1 aload_1
     * 2 aload_2
     * 3 invokestatic #6 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 6 return
     *
     * @param tag
     * @param message
     * @param args
     */
//    public static void v(String tag, String message, Object... args) {
//        LogUtil.v(tag, message, args);
//    }

    /**
     * 0 new #3 <java/lang/Object>
     * 3 dup
     * 4 invokespecial #4 <java/lang/Object.<init> : ()V>
     * 7 astore_1
     * 8 ldc #5 <tag>
     * 10 ldc #6 <message>
     * 12 iconst_1
     * 13 anewarray #3 <java/lang/Object>
     * 16 dup
     * 17 iconst_0
     * 18 aload_1
     * 19 aastore
     * 20 invokestatic #7 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 23 return
     */
//    private void idLog() {
//        Object ob = new Object();
//        LogUtil.v("tag", "message", ob);
//    }

    /**
     * 0 new #8 <java/lang/Throwable>
     * 3 dup
     * 4 invokespecial #9 <java/lang/Throwable.<init> : ()V>
     * 7 astore_1
     * 8 ldc #5 <tag>
     * 10 ldc #6 <message>
     * 12 aload_1
     * 13 invokestatic #10 <XXX/util/LogUtil.v : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V>
     * 16 return
     */
//    private void idLogV2() {
//        Throwable th = new Throwable();
//        LogUtil.v("tag", "message", th);
//    }

    /**
     * 0 new #8 <java/lang/Throwable>
     * 3 dup
     * 4 invokespecial #9 <java/lang/Throwable.<init> : ()V>
     * 7 astore_1
     * 8 new #3 <java/lang/Object>
     * 11 dup
     * 12 invokespecial #4 <java/lang/Object.<init> : ()V>
     * 15 astore_2
     * 16 aload_1
     * 17 ldc #5 <tag>
     * 19 ldc #6 <message>
     * 21 iconst_1
     * 22 anewarray #3 <java/lang/Object>
     * 25 dup
     * 26 iconst_0
     * 27 aload_2
     * 28 aastore
     * 29 invokestatic #11 <XXX/util/LogUtil.v : (Ljava/lang/Throwable;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V>
     * 32 return
     */
//    private void idLogV3() {
//        Throwable th = new Throwable();
//        Object ob = new Object();
//        LogUtil.v(th, "tag", "message", ob);
//    }

    /**
     * private void visivConstant() {
     * int id = 2147483647;
     * this.id = id;
     * char name = 49;
     * char name = (char)(name + 1);
     * this.name = name;
     * boolean debug = true;
     * this.debug = !debug;
     * long age = -2147483648L;
     * this.age = --age;
     * long smallAge = 2L;
     * this.smallAge = --smallAge;
     * Log.i("", "ID");
     * }
     * <p>
     * <p>
     * <p>
     * // javac编译时，把ID + MAX_ID的值（2147483647）存到了常量池表里
     * 0 ldc #16 <2147483647>     // 把常量池表的第16个常量值压入操作数栈
     * 2 istore_1                 //把操作数栈顶的元素存到栈帧的本地变量表index为1的位置（本地变量id被赋值为2147483647）
     * 3 aload_0                  //把本地变量表index为0的引用push到操作数栈（this引用）
     * 4 iload_1                  //把本地变量表index为1的int值push到操作数栈
     * 5 putfield #17 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.id : I>   //给成员变量赋值为操作数栈顶的数，并把该数出栈（成员变量的描述位于常量池第17个常量）
     * 8 bipush 49                //压入一个字节（49）到操作数栈
     * 10 istore_2                 //把操作数栈顶的元素存到栈帧的本地变量表index为2的位置（本地变量name被赋值为49）
     * 11 iload_2                  //把本地变量表index为2的int值push到操作数栈
     * 12 iconst_1                 //把int常量1压入操作数栈
     * 13 iadd                     //把栈顶两个int值出栈，把他们的和入栈
     * 14 i2c                      //把栈顶的int值出栈，转成char再入栈
     * 15 istore_2                 //把操作数栈顶的元素存到栈帧的本地变量表index为2的位置（本地变量name被赋值为'50'）
     * 16 aload_0                  //把本地变量表index为0的引用push到操作数栈（this引用入栈）
     * 17 iload_2                  //把本地变量表index为1的int值push到操作数栈(本地变量name入栈)
     * 18 putfield #18 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.name : C>
     * 21 iconst_1                 //把int常量1压入操作数栈
     * 22 istore_3                 //把操作数栈顶的元素存到栈帧的本地变量表index为3的位置（本地变量debug被赋值为1）
     * 23 aload_0                  //把本地变量表index为0的引用push到操作数栈（this引用）
     * 24 iload_3                  //把本地变量表index为3的int值push到操作数栈(本地变量debug入栈)
     * 25 ifne 32 (+7)             //把操作数栈顶元素出栈，和0比较，不等于0的话就成功，执行下一条指令，否则执行第32条指令
     * 28 iconst_1
     * 29 goto 33 (+4)
     * 32 iconst_0
     * 33 putfield #19 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.debug : Z>
     * 36 ldc2_w #20 <-2147483648> //把常量池中的第20个long或者double类型数据压入操作数栈
     * 39 lstore 4                 //把操作数栈顶的元素存到栈帧的本地变量表index为4的位置（本地变量age赋值为-2147483648）
     * 41 aload_0                  //把本地变量表index为0的引用push到操作数栈（this引用）
     * 42 lload 4                  //把本地变量表index为4的long型数据push到操作数栈
     * 44 lconst_1                 //把int常量1压入操作数栈
     * 45 lsub                     //把操作数栈顶两个元素出栈，把他们的差入栈
     * 46 dup2                     //复制栈顶元素，由于栈顶是long，需要2个单位，所以此处用dup2命令 todo 这为啥要dup呢？
     * 47 lstore 4
     * 49 putfield #22 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.age : J>
     * 52 ldc2_w #23 <2>
     * 55 lstore 6
     * 57 aload_0
     * 58 lload 6
     * 60 lconst_1
     * 61 lsub
     * 62 dup2
     * 63 lstore 6
     * 65 putfield #25 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.smallAge : J>
     * 68 ldc #26
     * 70 ldc #27 <ID>
     * 72 invokestatic #9 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 75 pop
     * 76 return
     */
    private void visivConstant() {
        int id = ID + MAX_ID;
        this.id = id;
        char name = NAME;
        name += 1;
        this.name = name;
        boolean debug = DEBUG;
        this.debug = !debug;
        long age = AGE;
        this.age = --age;
        long smallAge = SMALL_AGE;
        this.smallAge = --smallAge;
        Log.i("MainActivity", "visivConstant from i ");
        Log.e("MainActivity", "visivConstant from e ");
    }

    private void doNothing() {

    }

    private int someInt() {
        return 1;
    }

    private int swapInt(int i) {
        return i + 1;
    }

    /**
     * 0 new #16 <java/lang/StringBuilder>
     * 3 dup
     * 4 invokespecial #17 <java/lang/StringBuilder.<init> : ()V>
     * 7 ldc #41 <haha>
     * 9 invokevirtual #19 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 12 new #20 <java/util/Random>
     * 15 dup
     * 16 invokespecial #21 <java/util/Random.<init> : ()V>
     * 19 invokevirtual #22 <java/util/Random.nextInt : ()I>
     * 22 invokevirtual #23 <java/lang/StringBuilder.append : (I)Ljava/lang/StringBuilder;>
     * 25 invokevirtual #24 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 28 astore_1
     * 29 ldc #7 <MainActivity>
     * 31 new #16 <java/lang/StringBuilder>
     * 34 dup
     * 35 invokespecial #17 <java/lang/StringBuilder.<init> : ()V>
     * 38 ldc #42 <name is: >
     * 40 invokevirtual #19 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 43 aload_1
     * 44 invokevirtual #19 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 47 invokevirtual #24 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 50 invokestatic #9 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 53 pop
     * 54 return
     */
    private void complicatedLog() {
        String name = "haha" + new Random().nextInt();
        Log.i("MainActivity", "name is: " + name);
    }

//    private void complicatedLog2() {
//        Log.i("MainActivity", "name is: " + nameStr);
//    }
//
//    private void complicatedLog3() {
//        Person student = new Student();
//        Log.i("MainActivity", "student is " + student.eat());
//    }
//
//    private void complicatedLog4() {
//        Student student = new Student();
//        student.gotoSchool();// invokevirtual
//        Log.i("MainActivity", "student is " + new Student().eat());
//
//    }
//
//    private void complicatedLog5() {
//        Log.i("MainActivity", "student is " + new Student().toString());
//    }

    /**
     * 0 new #43 <cn/com/lbb/my_gradle_plugin_sample/MainActivity$Student>
     * 3 dup
     * 4 aload_0
     * 5 invokespecial #44 <cn/com/lbb/my_gradle_plugin_sample/MainActivity$Student.<init> : (Lcn/com/lbb/my_gradle_plugin_sample/MainActivity;)V>
     * 8 astore_1
     * 9 aload_1
     * 10 invokevirtual #48 <cn/com/lbb/my_gradle_plugin_sample/MainActivity$Student.shit : ()V>
     * 13 return
     */
//    private void complicatedLog6() {
//        Student student = new Student();
//        student.shit();
//    }

    /**
     * 0 aload_0
     * 1 invokespecial #49 <android/app/Activity.closeContextMenu : ()V>
     * 4 return
     */
//    private void invokeSuper() {
//        super.closeContextMenu();
//    }

    /**
     * 0 ldc #7 <MainActivity>
     * 2 new #16 <java/lang/StringBuilder>
     * 5 dup
     * 6 invokespecial #17 <java/lang/StringBuilder.<init> : ()V>
     * 9 ldc #52 <my class is >
     * 11 invokevirtual #19 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 14 aload_0
     * 15 invokespecial #49 <java/lang/Object.toString : ()Ljava/lang/String;>
     * 18 invokevirtual #19 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 21 invokevirtual #24 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 24 invokestatic #9 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 27 pop
     * 28 return
     */
    private void logWithInvokeSuper() {
        Log.i("MainActivity", "my class is " + super.toString());
    }

    /**
     * 0 aload_0
     * 1 invokespecial #50 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.doNothing : ()V>
     * 4 return
     */
//    private void invokePrivate() {
//        doNothing();
//    }

    /**
     * 验证通过，暂时注释掉
     * <p>
     * 0 ldc #7 <MainActivity>
     * 2 new #16 <java/lang/StringBuilder>
     * 5 dup
     * 6 invokespecial #17 <java/lang/StringBuilder.<init> : ()V>
     * 9 ldc #54 <my int is >
     * 11 invokevirtual #19 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 14 aload_0
     * 15 invokespecial #55 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.someInt : ()I>
     * 18 invokevirtual #23 <java/lang/StringBuilder.append : (I)Ljava/lang/StringBuilder;>
     * 21 invokevirtual #24 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 24 invokestatic #9 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 27 pop
     * 28 return
     */
    private void logWithInvokePrivate() {
        Log.i("MainActivity", "my int is " + someInt());
    }

    /**
     * 0 aload_0
     * 1 invokespecial #51 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.someInt : ()I> 这个指令把this出栈，把返回值int入栈，由于当前方法没有返回值，所以需要下面的pop指令把int舍弃
     * 4 pop
     * 5 return
     */
//    private void invokePrivate2() {
//        someInt();
//    }

    /**
     * 0 aload_0
     * 1 bipush 9
     * 3 invokespecial #52 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.swapInt : (I)I>
     * 6 pop
     * 7 return
     */
//    private void invokePrivate3() {
//        swapInt(9);
//    }

    /**
     * 解析成功，暂时注释掉
     * <p>
     * 0 ldc #7 <MainActivity>
     * 2 new #16 <java/lang/StringBuilder>
     * 5 dup
     * 6 invokespecial #17 <java/lang/StringBuilder.<init> : ()V>
     * 9 ldc #57 <my int swap >
     * 11 invokevirtual #19 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 14 aload_0
     * 15 iconst_3
     * 16 invokespecial #56 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.swapInt : (I)I>
     * 19 invokevirtual #23 <java/lang/StringBuilder.append : (I)Ljava/lang/StringBuilder;>
     * 22 invokevirtual #24 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 25 invokestatic #9 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 28 pop
     * 29 return
     */
//    private void logInvokePrivateWithArgs() {
//        Log.i("MainActivity", "my int swap " + swapInt(3));
//    }

    /**
     * 验证通过，暂时注释掉
     * <p>
     * 0 ldc #3 <MainActivity>
     * 2 new #4 <java/lang/StringBuilder>
     * 5 dup
     * 6 invokespecial #5 <java/lang/StringBuilder.<init> : ()V>
     * 9 ldc #6 <age is >
     * 11 invokevirtual #7 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 14 aload_0
     * 15 dup
     * 16 getfield #8 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.age : J>
     * 19 lconst_1
     * 20 ladd
     * 21 dup2_x1
     * 22 putfield #8 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.age : J>
     * 25 invokevirtual #9 <java/lang/StringBuilder.append : (J)Ljava/lang/StringBuilder;>
     * 28 invokevirtual #10 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 31 invokestatic #11 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 34 pop
     * 35 return
     */
    private void logWithDup2_x1() {
        Log.i("MainActivity", "age is " + (++age));
    }


    /**
     * 验证通过，暂时注释
     * <p>
     * 0 ldc #3 <MainActivity>
     * 2 new #4 <java/lang/StringBuilder>
     * 5 dup
     * 6 invokespecial #5 <java/lang/StringBuilder.<init> : ()V>
     * 9 ldc #6 <age is >
     * 11 invokevirtual #7 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 14 aload_0
     * 15 dup
     * 16 getfield #8 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.id : I>
     * 19 iconst_1
     * 20 iadd
     * 21 dup_x1
     * 22 putfield #8 <cn/com/lbb/my_gradle_plugin_sample/MainActivity.id : I>
     * 25 invokevirtual #9 <java/lang/StringBuilder.append : (I)Ljava/lang/StringBuilder;>
     * 28 invokevirtual #10 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 31 invokestatic #11 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 34 pop
     * 35 return
     */
    private void logWithDup_x1() {
        Log.i("MainActivity", "age is " + (++id));
    }


    /**
     * 解析成功，暂时注释掉
     * <p>
     * 0 lconst_1
     * 1 lstore_1
     * 2 ldc #3 <MainActivity>
     * 4 new #4 <java/lang/StringBuilder>
     * 7 dup
     * 8 invokespecial #5 <java/lang/StringBuilder.<init> : ()V>
     * 11 ldc #15 <num is >
     * 13 invokevirtual #7 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
     * 16 lload_1
     * 17 lconst_1
     * 18 ladd
     * 19 dup2
     * 20 lstore_1
     * 21 invokevirtual #14 <java/lang/StringBuilder.append : (J)Ljava/lang/StringBuilder;>
     * 24 invokevirtual #10 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
     * 27 invokestatic #11 <android/util/Log.i : (Ljava/lang/String;Ljava/lang/String;)I>
     * 30 pop
     * 31 return
     */
    private void logWithDup2() {
        long num = 1;
        Log.i("MainActivity", "num is " + (++num));
    }

    class Person {
        public String eat() {
            return "eating";
        }

        protected void shit() {

        }
    }

    class Student extends Person {
        public void gotoSchool() {
        }
    }


}
