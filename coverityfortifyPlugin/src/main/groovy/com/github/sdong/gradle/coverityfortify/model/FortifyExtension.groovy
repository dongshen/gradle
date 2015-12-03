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
     * Specifies the directory Fortify uses during sourceanalyzer generate intermediate.
     * <p/>
     * Default value is
     * <code>"${project.buildDir}/fortify/intermediate"</code>.
     */
    String intermediateDir
    
    /**
     * Specifies the Fortify Build ID used during the translate phase.
     * <p/>
     * Default value is the <code>fortify_buidlId</code> environment variable
     * (or null if the environment variable is unset).
     */
    String fortifyBuildId


    /**
     * Specifies the source java version used during the translate phase.
     * <p/>
     * Indicates which version of the JDK the Java code is written for. Valid values 
     * for version are 1.3, 1.4, 1.5, 1.6 and 1.7. The default is 1.4. 
     */
    String sourceVersion

    /**
     * Specifies the zip file after build.
     * <p/>
     * Default value is null if the variable is not set.
     */
    String zipfile
    
    /**
     * Instantiates a new instance of this extension.
     *
     * @param project project this extension is applied to, which is used to
     *                default the {@link #intermediateDir}.
     */
    FortifyExtension(Project project) {
        intermediateDir = "${project.buildDir}/fortify/intermediate"
        fortifyHome = System.getenv('FORTIFY_HOME')   
        sourceVersion = "1.4"
        fortifyBuildId = "fortify_buidlId"
        zipfile = ""
    }
}
