package com.github.sdong.gradle

import com.github.sdong.gradle.coverity.model.CoverityExtension
import com.github.sdong.gradle.coverity.model.CoverityRootExtension
import com.github.sdong.gradle.coverity.tasks.CovAnalyzeJavaTask
import com.github.sdong.gradle.coverity.tasks.CovCommitDefectsTask
import com.github.sdong.gradle.coverity.tasks.CovEmitJavaTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin for integrating with <a href="http://www.coverity.com">Coverity</a>,
 * a static analysis platform.  Adds tasks to the project that will emit,
 * analyze, and commit data using Coverity analysis tools that are installed on
 * the user's machine.
 * <p/>
 * Supports analysis of Java code for Java and/or Android projects.
 *
 * @author sdong
 */
class CoverityPlugin implements Plugin<Project> {
    /**
     * Creates the root extension, child extensions, the covEmitJava task, the
     * covAnalyzeJava task, and the covCommitDefects task.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    void apply(Project project) {
        project.extensions.create(CoverityExtension.EXTENSION_NAME,
                CoverityRootExtension, project)

        project.task('covEmitJava', type: CovEmitJavaTask)
        project.task('covAnalyzeJava', type: CovAnalyzeJavaTask,
                dependsOn: project.tasks.covEmitJava)
        project.task('covCommitDefects', type: CovCommitDefectsTask,
                dependsOn: project.tasks.covAnalyzeJava)

        configureChildProjects(project)
    }

    /**
     * Recurvisely configures the given project's child projects with the
     * {@link CoverityExtension}.  This is run during {@link #apply}.
     *
     * @param project project to recurse and configure
     */
    void configureChildProjects(Project project) {
        for (Project childProject : project.childProjects.values()) {
            childProject.extensions.create(CoverityExtension.EXTENSION_NAME,
                    CoverityExtension)

            configureChildProjects(childProject)
        }
    }
}
