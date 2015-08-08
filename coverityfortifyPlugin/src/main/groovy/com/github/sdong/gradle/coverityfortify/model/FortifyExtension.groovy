package com.github.sdong.gradle.coverityfortify.model

import org.gradle.api.Project

/**
 * The extension that is applied to the project that applies the
 * {@link com.github.sdong.gradle.CoverityFortifyPlugin}.
 *
 * @author sdong
 */
class FortifyExtension  {
    /**
     * Specifies the directory Fortify uses during translate.
     * <p/>
     * Default value is
     * <code>"${project.buildDir}/fortify/intermediate"</code>.
     */
    String intermediateDir

    /**
     * Specifies the path to your Fortify Analysis Tools root directory (leave
     * "bin" off the end).
     * <p/>
     * Null or empty values causes this plugin to assume the tools are on your
     * <code>PATH</code>.
     * <p/>
     * Default value is the <code>FORTIFY_HOME</code> environment variable
     * (or null if the environment variable is unset).
     */
    String fortifyHome

    /**
     * Specifies the Fortify Build ID used during the translate phase.
     * <p/>
     * Default value is the <code>FORTIFY_BUILD_ID</code> environment variable
     * (or null if the environment variable is unset).
     */
    String buildID


    /**
     * Instantiates a new instance of this extension.
     *
     * @param project project this extension is applied to, which is used to
     *                default the {@link #intermediateDir}.
     */
    FortifyExtension(Project project) {
        intermediateDir = "${project.buildDir}/fortify/intermediate"
        fortifyHome = System.getenv('FORTIFY_HOME')
        buildID = System.getenv('FORTIFY_BUILD_ID')
    }
}
