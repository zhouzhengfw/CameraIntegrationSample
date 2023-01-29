package com.loopnow.gradle

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginAware

class CameraInitPlugin implements Plugin<PluginAware> {
    @Override
    void apply(PluginAware target) {

         if (target instanceof Project) {
            println "初始化FW Camera"
            def fwLibraryVersion = "1.4.3-test"
            if (target.getPlugins().hasPlugin(AppPlugin.class)){
                BaseExtension android = target.extensions.getByName("android") as BaseExtension
                android.compileOptions.sourceCompatibility = JavaVersion.VERSION_1_8
                android.compileOptions.targetCompatibility = JavaVersion.VERSION_1_8
                android.defaultConfig.javaCompileOptions.annotationProcessorOptions.arguments.put "AROUTER_MODULE_NAME", target.getName()
                target.dependencies.add('implementation', "com.loopnow.library:camera:$fwLibraryVersion")
                target.dependencies.add('implementation', "com.loopnow.library:auth:$fwLibraryVersion")
                target.dependencies.add('implementation', "com.loopnow.library.base:base-ui:$fwLibraryVersion")
                target.dependencies.add('implementation', "com.loopnow.library.base:network:$fwLibraryVersion")
                target.dependencies.add('implementation', "com.loopnow.library.base:util:$fwLibraryVersion")
                target.dependencies.add('implementation', "com.loopnow.library:camera-framework:$fwLibraryVersion")
                target.dependencies.add('implementation', "com.loopnow.library:content-management:$fwLibraryVersion")
                target.dependencies.add('implementation', "com.loopnow.library:live-stream-kit:$fwLibraryVersion")
                println "Init FW Camera Success"
            }
        }
    }
}