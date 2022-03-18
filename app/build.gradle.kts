plugins {
    id("com.android.application")
}

buildscript {
    repositories {
        mavenLocal()
        google()
//        maven { setUrl("https://dl.google.com/dl/android/maven2/") } // google()
        mavenCentral()
    }
    dependencies {
        //MyFirstPlugin
//        classpath("cn.com.lbb:library:1.1")//groupid:artifictId:version
        //MyLogPlugin
        classpath("cn.com.lbb:customplugin:1.1")//groupid:artifictId:version
//        classpath("com.getkeepsafe.dexcount:dexcount-gradle-plugin:3.1.0")
    }
}

//apply(plugin = "cn.com.lbb.myfirstplugin.simple-plugin") //plugin id
apply(plugin = "cn.com.lbb.log_gradle_plugin.asm-plugin")//plugin id


android {
    compileSdk = 31

    defaultConfig {
        applicationId = "cn.com.lbb.my_gradle_plugin_sample"
        versionCode = 1
        versionName = "1.0"
        minSdk = 23
        targetSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

repositories {
    google()
    mavenCentral()
}

the<cn.com.lbb.log_gradle_plugin.LogDeleteExtension>().enable(true)