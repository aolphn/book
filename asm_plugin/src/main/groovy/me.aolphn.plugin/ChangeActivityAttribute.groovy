package me.aolphn.plugin

import com.android.build.gradle.AppExtension
import groovy.xml.XmlUtil

class ChangeActivityAttribute{

    static void change(AppExtension android) {
        def variants = null
        try {
            variants = android.applicationVariants
        } catch (Throwable t) {
            try {
                variants = android.libraryVariants
            } catch (Throwable tt) {}
        }
        if (variants != null) {
            variants.all { variant ->
                variant.outputs.each { output ->
                    def task = output.processManifestProvider.get()
                    if (task == null) {
                        return
                    }
                    task.doLast {
                        def manifestFile = new File(task.manifestOutputDirectory.get().getAsFile(),"AndroidManifest.xml")
                        if (manifestFile == null || !manifestFile.exists()) {
                            return
                        }
                        def parser = new XmlSlurper(false, true)
                        def manifest = parser.parse(manifestFile)
                        def app = manifest.'application'[0]
                        app.'activity'.each{act->
                            String value = act.attributes()['android:configChanges']
                            println("check value:$value")
                            if (value == null || value.isEmpty()) {
                                value = "keyboardHidden|orientation|screenSize"
                                act.@"androidconfigChanges" = value

                            } else {
                                String[] valueSplit = value.split("\\|")
                                println("check configs:$value,len:${valueSplit.length}")
                                valueSplit.each {str->
                                    println("check config:$str")
                                }
                                if (!valueSplit.contains("keyboardHidden")) {
                                    value+="|keyboardHidden"
                                }
                                if (!valueSplit.contains("orientation")) {
                                    value+="|orientation"
                                }
                                if (!valueSplit.contains("screenSize")) {
                                    value+="|screenSize"
                                }
                                println("check final value:$value")
                                act.@"android:configChanges" = value
                            }
                        }
                        def tmp = XmlUtil.serialize(manifest).replaceAll("androidconfigChanges","android:configChanges")
                        manifest = parser.parseText(tmp)
                        manifestFile.setText(XmlUtil.serialize(manifest), "utf-8")
                    }
                }
            }
        }
    }
}