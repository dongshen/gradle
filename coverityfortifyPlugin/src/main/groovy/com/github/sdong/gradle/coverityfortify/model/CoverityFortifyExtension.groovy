package com.github.sdong.gradle.coverityfortify.model

import org.gradle.api.Project
import com.github.sdong.gradle.coverityfortify.model.CoverityExtension
import com.github.sdong.gradle.coverityfortify.model.FortifyExtension

/**
 * The base extension that contains settings to be applied to all child projects
 * and will be inherited by {@link CoverityExtension}.
 *
 * @author sdong
 */
class CoverityFortifyExtension {
    /**
     * The name that should be given when creating this extension.
     */
    static String EXTENSION_NAME = 'coverity_fortify'

    /**
     * Specifies if the current project's child projects should be included.
     * If true, child projects will be included even if the current project is
     * skipped.
     * <p/>
     * Default value is true.
     */
    boolean includeChildProjects = true

    /**
     * Specifies if the current project should be skipped.  Does not also skip
     * child projects (this is defined by {@link #includeChildProjects}).
     * <p/>
     * Default value is false.
     */
    boolean skip = false
    
    /**
     * Nested CoverityExtension
     *
     */
     CoverityExtension covExtension
     
    /**
     * Nested FortifyExtension
     *
     */
     FortifyExtension forExtension
     
    CoverityFortifyExtension(Project project) {
    	covExtension = new CoverityExtension(project)
    	forExtension = new FortifyExtension(project)
    }   
}
