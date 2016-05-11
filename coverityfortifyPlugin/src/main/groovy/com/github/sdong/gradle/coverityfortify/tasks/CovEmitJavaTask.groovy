package com.github.sdong.gradle.coverityfortify.tasks

import com.github.sdong.gradle.coverityfortify.config.ProjectBuildConfig
import com.github.sdong.gradle.coverityfortify.config.ProjectBuildConfigSet
import com.github.sdong.gradle.coverityfortify.util.Utils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Runs cov-emit-java (once per source set for Java projects, once per variant
 * for Android projects), which parses Java source code and bytecode, and saves
 * javac outputs. It stores these outputs to an emit repository for subsequent
 * static analysis and outputs it into a directory (emit repository) that can
 * later be analyzed with <code>cov-analyze-java</code>.
 *
 * @author sdong
 */
class CovEmitJavaTask extends DefaultTask {
    /**
     * Specifies any additonal arguments to be passed to <i>each</i> external
     * <code>cov-emit-java</code> execution.
     */
    List<String> additionalArgs

    /**
     * Creates a new {@link CovEmitJavaTask}, setting its description and placing
     * it under the 'Coverity' group.
     */
    CovEmitJavaTask() {
        group = 'Coverity'
        description = 'Runs cov-emit-java (once per source set for Java ' +
                'projects, once per variant for Android projects), which ' +
                'parses Java source code and bytecode, and saves javac ' +
                'outputs. It stores these outputs to an emit repository for ' +
                'subsequent static analysis and outputs it into a directory ' +
                '(emit repository) that can later be analyzed with ' +
                'cov-analyze-java.'
    }

    /**
     * Task action that builds an {@link ProjectBuildConfigSet} for this task's project,
     * then executes <code>cov-emit-java</code> for each {@link ProjectBuildConfig}.
     */
    @SuppressWarnings('GroovyUnusedDeclaration')
    @TaskAction
    void emit() {
        for (ProjectBuildConfig projectBuildConfig : new ProjectBuildConfigSet(project).projectBuildConfigs) {
            // Remove source dirs that do not exist, otherwise cov-emit-java will throw an error
            for (Iterator<File> i = projectBuildConfig.sourceDirs.iterator(); i.hasNext();) {
                File f = i.next();
                if (!f.exists()) {
                    i.remove();
                }
            }

            /*
             * Note:
             * Not removing non-existent compiler outputs.  If none in the list
             * we provide exist, cov-emit-java will fail.  We want that to
             * happen because that case probably represents a misconfiguration.
             */


				println 'covEmitJava plugin!' 
				println '----------------------'
				println 'classpath:'+ projectBuildConfig.classpath.asPath
				println '----------------------'
				println 'compiler-outputs:'+ projectBuildConfig.compilerOutputDirs.join(File.pathSeparator)
				println '----------------------'
				println 'sourceDirs:'+ projectBuildConfig.sourceDirs.join(File.pathSeparator)
				println '----------------------'
				println 'IntermediateDir = ' + project.file((String) project.coverity_fortify.coverity.intermediateDir).absolutePath
				println 'COVERITY_HOME = ' +System.getenv('COVERITY_HOME')
				
			//check java file in soureDirs
			def hasJavaFile = false
			 for (Iterator<File> i = projectBuildConfig.sourceDirs.iterator(); i.hasNext();) {
                    File f = i.next();
                    hasJavaFile = Utils.checkJavaFile(f)
                    if(hasJavaFile){
                        break;
                    }
             }	
             
             if (hasJavaFile == false){
                println 'source folder is empty for Java files, skip it'
                continue
             }		
			
			//check debugMode
    			def debugMode = (String) project.coverity_fortify.coverity.debugMode
                if (debugMode != null && debugMode.compareToIgnoreCase('true') == 0 ){
                    println = 'Run Coverity Command:'+Utils.getExePath((String) project.coverity_fortify.coverity.coverityHome, 'cov-emit-java')+' --dir '+ project.file((String) project.coverity_fortify.coverity.intermediateDir).absolutePath+' --findsource ' + projectBuildConfig.sourceDirs.join(File.pathSeparator) +' --no-compiler-outputs '+' --classpath '+ projectBuildConfig.classpath.asPath
                
                    project.exec {
                        executable Utils.getExePath((String) project.coverity_fortify.coverity.coverityHome, 'cov-emit-java')
                        args '--dir', project.file((String) project.coverity_fortify.coverity.intermediateDir).absolutePath
                        args '--findsource', projectBuildConfig.sourceDirs.join(File.pathSeparator)
                        args '--no-compiler-outputs'
                        args '--classpath', projectBuildConfig.classpath.asPath
                        if (additionalArgs) {
                            args additionalArgs
                        }
                    }
                 } else {
                    println = 'Run Coverity Command:'+Utils.getExePath((String) project.coverity_fortify.coverity.coverityHome, 'cov-emit-java')+' --dir '+ project.file((String) project.coverity_fortify.coverity.intermediateDir).absolutePath+' --findsource ' + projectBuildConfig.sourceDirs.join(File.pathSeparator) +' --compiler-outputs '+ projectBuildConfig.compilerOutputDirs.join(File.pathSeparator)+' --classpath '+ projectBuildConfig.classpath.asPath
                    project.exec {
                        executable Utils.getExePath((String) project.coverity_fortify.coverity.coverityHome, 'cov-emit-java')
                        args '--dir', project.file((String) project.coverity_fortify.coverity.intermediateDir).absolutePath
                        args '--findsource', projectBuildConfig.sourceDirs.join(File.pathSeparator)
                        args '--compiler-outputs', projectBuildConfig.compilerOutputDirs.join(File.pathSeparator)
                        args '--classpath', projectBuildConfig.classpath.asPath
                        if (additionalArgs) {
                            args additionalArgs
                        }
                 }
            } 
                       
        }
        //zip file
        def zipFileName = (String) project.coverity_fortify.coverity.zipfile
        if (zipFileName != null && !zipFileName.isEmpty()){
            Utils.zipFolder(zipFileName, (String) project.coverity_fortify.coverity.intermediateDir)
        }
    }
}
