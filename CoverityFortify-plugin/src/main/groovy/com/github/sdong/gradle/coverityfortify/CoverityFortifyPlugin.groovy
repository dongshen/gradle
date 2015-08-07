package com.github.sdong.gradle.coverityfortify

import com.github.sdong.gradle.coverityfortify.model.Extension
import com.github.sdong.gradle.coverityfortify.model.CoverityRootExtension
import com.github.sdong.gradle.coverityfortify.tasks.CovAnalyzeJavaTask
import com.github.sdong.gradle.coverityfortify.tasks.CovCommitDefectsTask
import com.github.sdong.gradle.coverityfortify.tasks.CovEmitJavaTask
import com.github.sdong.gradle.coverityfortify.tasks.ForTransJavaTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin for integrating with Coverity & Fortify.
 * For Coverity, adds tasks to the project that will emit, analyze and commit data.
 * For Fortify, adds tasks to the project that will transit java code.
 * <p/>
 * Supports analysis of Java code for Java and/or Android projects.
 *
 * @author sdong
 */
class CoverityFortifyPlugin implements Plugin<Project> {
    /**
     * For Coverity creates the root extension, child extensions, the covEmitJava task, the
     * covAnalyzeJava task, and the covCommitDefects task.
     * <p/>
     * For Fortify create the root extension, child extensions, the forTranJava task.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    void apply(Project project) {
        project.extensions.create(Extension.EXTENSION_NAME,
                CoverityRootExtension, project)

        project.task('covEmitJava', type: CovEmitJavaTask)
        project.task('covAnalyzeJava', type: CovAnalyzeJavaTask,
                dependsOn: project.tasks.covEmitJava)
        project.task('covCommitDefects', type: CovCommitDefectsTask,
                dependsOn: project.tasks.covAnalyzeJava)
                
        project.task('forTransJava', type: ForTransJavaTask)        

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
            childProject.extensions.create(Extension.EXTENSION_NAME,
                    CoverityExtension)

            configureChildProjects(childProject)
        }
    }
}
