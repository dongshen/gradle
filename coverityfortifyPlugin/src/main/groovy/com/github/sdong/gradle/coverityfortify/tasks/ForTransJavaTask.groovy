package com.github.sdong.gradle.coverityfortify.tasks

import com.github.sdong.gradle.coverityfortify.config.ProjectBuildConfig
import com.github.sdong.gradle.coverityfortify.config.ProjectBuildConfigSet
import com.github.sdong.gradle.coverityfortify.util.Utils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Runs sourceanalyzer (once per source set for Java projects, once per variant
 * for Android projects), which parses Java source code and bytecode, and saves
 * javac outputs. It stores these outputs to an emit repository for subsequent
 * static analysis and outputs it into a directory (build id) that can
 * later be analyzed with <code>-scan</code>.
 *
 * @author sdong
 */
class ForTransJavaTask extends DefaultTask {
    /**
     * Specifies any additonal arguments to be passed to <i>each</i> external
     * <code>sourceanalyzer</code> execution.
     */
    List<String> additionalArgs

    /**
     * Creates a new {@link ForTransJavaTask}, setting its description and placing
     * it under the 'Fortify' group.
     */
    ForTransJavaTask() {
        group = 'Fortify'
        description = 'Runs sourceanalyzer (once per source set for Java ' +
                'projects, once per variant for Android projects), which ' +
                'parses Java source code and bytecode, and saves javac ' +
                'outputs. It stores these outputs to an emit repository for ' +
                'subsequent static analysis and outputs it into a directory ' +
                'that can later be analyzed with sourceanalyzer -scan.'
    }

    /**
     * Task action that builds an {@link ProjectBuildConfigSet} for this task's project,
     * then executes <code>sourceanalyzer</code> for each {@link ProjectBuildConfig}.
     */
    @SuppressWarnings('GroovyUnusedDeclaration')
    @TaskAction
    void translation() {
        for (ProjectBuildConfig projectBuildConfig : new ProjectBuildConfigSet(project).projectBuildConfigs) {
            // Remove source dirs that do not exist, otherwise sourceanalyzer will throw an error
            for (Iterator<File> i = projectBuildConfig.sourceDirs.iterator(); i.hasNext();) {
                File f = i.next();
                if (!f.exists()) {
                    i.remove();
                }
            }

            /*
             * Note:
             * Not removing non-existent compiler outputs.  If none in the list
             * we provide exist, sourceanalyzer will fail.  We want that to
             * happen because that case probably represents a misconfiguration.
             */

				println 'forTransJava plugin!' 
				println '----------------------'
				println 'classpath:'+ projectBuildConfig.classpath.asPath
				
				println '----------------------'
				println 'compiler-outputs:'+ projectBuildConfig.compilerOutputDirs.join(File.pathSeparator)
				
				println '----------------------'
				println 'sourceDirs:'+ projectBuildConfig.sourceDirs.join(File.pathSeparator)
				def src=''
					srcDirs.each{
						println 'srcDirs:'+it
						src= src+ it + File.separator+'**'+File.separator+'*'+File.pathSeparator
					}
				
				println '-----intermediateDir------'
				println project.file((String) project.coverity_fortify.fortify.intermediateDir).absolutePath				
				
				println '-----fortifyHome---'
				println System.getenv('FORTIFY_HOME')
				println project.coverity_fortify.fortify.fortifyHome
				
				println '------fortifyBuildId------'
				println project.coverity_fortify.fortify.fortifyBuildId

			
            project.exec {            	
                executable Utils.getExePath((String) project.coverity_fortify.fortify.fortifyHome, 'sourceanalyzer ')
               // args '-b', (String) project.coverity_fortify.fortify.fortifyBuildId
                //args '--dir', project.file((String) project.coverity_fortify.fortify.intermediateDir).absolutePath
                
                //args '--compiler-outputs', projectBuildConfig.compilerOutputDirs.join(File.pathSeparator)
                args '-cp', projectBuildConfig.classpath.asPath
                args src
                //args '-f', projectBuildConfig.sourceDirs.join(File.pathSeparator)
                if (additionalArgs) {
                    args additionalArgs
                }
            }
            
        }
    }
}
