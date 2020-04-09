package me.aolphn.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class SelfLifeCyclePlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        System.out.println("this is gradle plugin")
        def android = project.extensions.getByType(AppExtension)
        ChangeActivityAttribute.change(android)
//        android.registerTransform(new SelfTransform())

    }
}