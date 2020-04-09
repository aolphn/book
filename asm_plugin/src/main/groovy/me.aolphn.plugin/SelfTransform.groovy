package me.aolphn.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.io.FileType
import me.aolphn.asm_plugin.LifeCycleClassVisitor
import me.aolphn.plugin.utils.LogUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
class SelfTransform extends Transform{

    /**
     * Transform task name
     */
    @Override
    String getName() {
        return "SelfTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        Collection<TransformInput> transformInputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (outputProvider != null) {
            outputProvider.deleteAll()
        }
        transformInputs.each { TransformInput transformInput->
            transformInput.directoryInputs.each {DirectoryInput directoryInput->
               File dir = directoryInput.file
               if (dir) {
                   dir.eachFileRecurse {File file->
                       def name = file.name
                       if (name.endsWith(".class") && !name.startsWith("R\$")) {

//                       System.out.println("find class:${file.name}")
                           ClassReader classReader = new ClassReader(file.bytes)
                           ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                           LifeCycleClassVisitor classVisitor = new LifeCycleClassVisitor(classWriter)
                           classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES)
                           if (classVisitor.isClassChanged()) {
                               byte[] bytes = classWriter.toByteArray()
                               FileOutputStream outputStream    = new FileOutputStream(file.path)
                               outputStream.write(bytes)
                               outputStream.close()
                               LogUtils.print("${file.name} was changed")
                           } else {
//                           LogUtils.print("${file.name} does't changed")
                           }
                       }
                   }
               }
                def dest = outputProvider.getContentLocation(directoryInput.name,directoryInput.contentTypes,directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file,dest)
            }

        }
    }
}