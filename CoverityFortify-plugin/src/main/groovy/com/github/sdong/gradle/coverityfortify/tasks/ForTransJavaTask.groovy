package com.github.sdong.gradle.coverityfortify.tasks

import com.github.sdong.gradle.coverityfortify.config.CovEmitConfig
import com.github.sdong.gradle.coverityfortify.config.CovEmitConfigSet
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
     * Creates a new {@link CovEmitJavaTask}, setting its description and placing
     * it under the 'Coverity' group.
     */
    ForTransJavaTask() {
        group = 'Fortify'
        description = 'Runs sourceanalyzer (once per source set for Java ' +
                'projects, once per variant for Android projects), which ' +
                'parses Java source code and bytecode, and saves javac ' +
                'outputs. It stores these outputs to an emit repository for ' +
                'subsequent static analysis and outputs it into a directory ' +
                '(emit repository) that can later be analyzed with ' +
                'cov-analyze-java.'
    }

    /**
     * Task action that builds an {@link CovEmitConfigSet} for this task's project,
     * then executes <code>cov-emit-java</code> for each {@link CovEmitConfig}.
     */
    @SuppressWarnings('GroovyUnusedDeclaration')
    @TaskAction
    void translation() {
        for (CovEmitConfig emitConfig : new CovEmitConfigSet(project).emitConfigs) {
            // Remove source dirs that do not exist, otherwise cov-emit-java will throw an error
            for (Iterator<File> i = emitConfig.sourceDirs.iterator(); i.hasNext();) {
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

            project.exec {
                executable Utils.getExePath((String) project.coverity.coverityHome, 'cov-emit-java')
                args '--dir', project.file((String) project.coverity.intermediateDir).absolutePath
                args '--findsource', emitConfig.sourceDirs.join(File.pathSeparator)
                args '--compiler-outputs', emitConfig.compilerOutputDirs.join(File.pathSeparator)
                args '--classpath', emitConfig.classpath.asPath
                if (additionalArgs) {
                    args additionalArgs
                }
            }
        }
    }
}
