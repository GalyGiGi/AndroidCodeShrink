plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    `maven-publish`
}


allprojects {    // allprojects是配置这个工程所需的依赖，这里是工程的代码需要AGP和asm的库，所以配到这里
    repositories {
        google()
        mavenCentral()
    }
}
//
dependencies {
    // 自定义插件需要调用AGP里面的api，所以需要依赖AGP库
    compileOnly("com.android.tools.build:gradle:3.4.1")//如果用implementation，会导致引用这个插件的脚本编译不通过
    // ASM库依赖，用于修改字节码
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.ow2.asm:asm-commons:9.1")
}


gradlePlugin {
    plugins {
        create("ASMPlugin") {
            id = "cn.com.lbb.log_gradle_plugin.asm-plugin"
            implementationClass = "cn.com.lbb.log_gradle_plugin.MyLogPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "cn.com.lbb"
            version = "1.1"
            artifactId = "customplugin"

            from(components["java"])
        }
    }
    repositories {
        maven {
            // change to point to your repo, e.g. http://my.org/repo
            /**
             * 执行publishToMavenLocal后，
             * 插件目录为：/Users/xxx/.m2/repository/cn/com/lbb/log_gradle_plugin/asm-plugin/cn.com.lbb.log_gradle_plugin.asm-plugin.gradle.plugin
             *
             *这里的uri对于maven local 无效
             */
            url = uri("/local_maven/repos")
        }
    }
}