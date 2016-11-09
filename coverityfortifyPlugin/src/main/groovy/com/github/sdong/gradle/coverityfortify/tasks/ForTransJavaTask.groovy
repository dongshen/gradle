package com.github.sdong.gradle.coverityfortify.tasks

import com.github.sdong.gradle.coverityfortify.config.ProjectBuildConfig
import com.github.sdong.gradle.coverityfortify.config.ProjectBuildConfigSet
import com.github.sdong.gradle.coverityfortify.util.Utils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Runs sourceanalyzer (once per source set for Java projects, once per variant
 * for Android projects), which parses Java source code and bytecode, and saves
 * javac outputs. It stores these outputs into a directory (build id) that can
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
        description = 'Runs Fortify command sourceanalyzer for Java ' +
                'projects or Android projects, which ' +
                'parses Java source code and bytecode, and saves javac ' +
                'outputs. It stores these intermediate outputs into a directory ' +
                'that can be analyzed with sourceanalyzer -scan.'
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
					           
	            //filter out folder without java file in soureDirs
                def hasJavaFile = false
                def sourceList = []
                 for (Iterator<File> i = projectBuildConfig.sourceDirs.iterator(); i.hasNext();) {
                        File f = i.next();
                        hasJavaFile = Utils.checkJavaFile(f)
                        if(hasJavaFile){
                            sourceList.add(f.absolutePath)
                        }
                 }  
                            					              
				println '-----fortifyHome---'
				println 'fortifyHome: ' + project.coverity_fortify.fortify.fortifyHome
				
				println '------fortifyBuildId------'
				println 'fortifyBuildId: ' + project.coverity_fortify.fortify.fortifyBuildId

				println '------sourceVersion------'
				println 'sourceVersion: ' + project.coverity_fortify.fortify.sourceVersion
			
			    println 'sourceDirs with java: ' + sourceList    
                if(!sourceList){
                    println 'source folder is empty for Java files, skip it'
                    continue
                }
                
                println 'Run Fortify Command:'+ Utils.getExePath((String) project.coverity_fortify.fortify.fortifyHome, 'sourceanalyzer') +' -b '+(String) project.coverity_fortify.fortify.fortifyBuildId +' -encoding UTF-8 --machine-output' + ' -cp ' + projectBuildConfig.classpath.asPath +' -source ' + (String) project.coverity_fortify.fortify.sourceVersion+' '+sourceList
                
            project.exec {            	
                executable Utils.getExePath((String) project.coverity_fortify.fortify.fortifyHome, 'sourceanalyzer')
                args '-b', (String) project.coverity_fortify.fortify.fortifyBuildId
				args '-Dcom.fortify.sca.ProjectRoot='+(String) project.coverity_fortify.fortify.intermediateDir
				args '-encoding','UTF-8'
                args '--machine-output'
                args '-cp', projectBuildConfig.classpath.asPath
                args '-source', (String) project.coverity_fortify.fortify.sourceVersion
                args sourceList 
                
                if (additionalArgs) {
                    args additionalArgs
                }
            }
            
        }
        
        //zip file
        def zipFileName = (String) project.coverity_fortify.fortify.zipfile
        if (zipFileName != null && !zipFileName.isEmpty()){
            Utils.zipFolder(zipFileName, (String) project.coverity_fortify.fortify.intermediateDir)
        }
    }
}
