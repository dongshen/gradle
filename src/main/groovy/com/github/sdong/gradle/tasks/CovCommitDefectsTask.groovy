package com.github.sdong.gradle.tasks

import com.github.sdong.gradle.util.Utils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Runs the <code>cov-commit-defects</code> command, which reads defects and
 * source data stored in an intermediate directory and writes them to a Coverity
 * Connect database in a stream that you specify. The data are written as a unit
 * to the database; this unit is a snapshot.
 *
 * @author sdong
 */
class CovCommitDefectsTask extends DefaultTask {
    /**
     * Specifies any additonal arguments to be passed to the external
     * <code>cov-commit-defects</code> execution.
     */
    List<String> additionalArgs

    /**
     * Creates a new {@link CovCommitDefectsTask}, setting its description and
     * placing it under the 'Coverity' group.
     */
    CovCommitDefectsTask() {
        group = 'Coverity'
        description = 'Runs the cov-commit-defects command, which reads ' +
                'defects and source data stored in an intermediate ' +
                'directory and writes them to a Coverity Connect database ' +
                'in a stream that you specify. The data are written as a ' +
                'unit to the database; this unit is a snapshot.'
    }

    /**
     * Task action that executes <code>cov-commit-defects</code>.
     */
    @SuppressWarnings('GroovyUnusedDeclaration')
    @TaskAction
    void commit() {
        project.exec {
            executable Utils.getExePath((String) project.coverity.coverityHome, 'cov-commit-defects')
            args '--dir', project.file((String) project.coverity.intermediateDir).absolutePath
            args '--stream', project.coverity.stream
            args '--host', project.coverity.host
            if (project.coverity.dataport) {
                args '--dataport', project.coverity.dataport
            } else {
                args '--port', project.coverity.port
            }
            args '--user', project.coverity.user
            args '--password', project.coverity.pass
            if (additionalArgs) {
                args additionalArgs
            }
        }
    }
}
