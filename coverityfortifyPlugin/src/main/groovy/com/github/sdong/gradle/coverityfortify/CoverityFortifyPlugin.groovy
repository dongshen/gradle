package com.github.sdong.gradle.coverityfortify

import com.github.sdong.gradle.coverityfortify.model.CoverityFortifyExtension
import com.github.sdong.gradle.coverityfortify.model.CoverityExtension
import com.github.sdong.gradle.coverityfortify.model.FortifyExtension
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
        project.extensions.create(CoverityFortifyExtension.EXTENSION_NAME, CoverityFortifyExtension, project)

		project.coverity_fortify.extensions.create('fortify',FortifyExtension,project)
        project.coverity_fortify.extensions.create('coverity',CoverityExtension,project)
        

        project.task('covEmitJava', type: CovEmitJavaTask)
                
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
            childProject.extensions.create(CoverityFortifyExtension.EXTENSION_NAME, CoverityFortifyExtension,project)
            
            childProject.coverity_fortify.extensions.create('coverity',CoverityExtension,project)
            childProject.coverity_fortify.extensions.create('fortify',FortifyExtension,project)
           
            configureChildProjects(childProject)
        }
    }
}
