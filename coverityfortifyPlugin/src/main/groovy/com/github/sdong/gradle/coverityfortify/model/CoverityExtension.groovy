package com.github.sdong.gradle.coverityfortify.model

import org.gradle.api.Project

/**
 * The extension that is applied to the project that applies the
 * {@link com.github.sdong.gradle.CoverityForfiyPlugin}.
 *
 * @author sdong
 */
class CoverityExtension  {
    /**
     * Specifies the directory Coverity uses during emit/analyze/commit.
     * <p/>
     * Default value is
     * <code>"${project.buildDir}/coverity/intermediate"</code>.
     */
    String intermediateDir

    /**
     * Specifies the path to your Coverity Analysis Tools root directory (leave
     * "bin" off the end).
     * <p/>
     * Null or empty values causes this plugin to assume the tools are on your
     * <code>PATH</code>.
     * <p/>
     * Default value is the <code>COVERITY_HOME</code> environment variable
     * (or null if the environment variable is unset).
     */
    String coverityHome

    /**
     * Specifies the Coverity Connect stream name used during the commit phase.
     * <p/>
     * Default value is the <code>COVERITY_STREAM</code> environment variable
     * (or null if the environment variable is unset).
     */
    String stream

    /**
     * Specifies the Coverity Connect commit host name.
     * <p/>
     * Default value is the <code>COVERITY_HOST</code> environment variable
     * (or null if the environment variable is unset).
     */
    String host

    /**
     * Specifies the Coverity Connect commit port number.
     * <p/>
     * Default value is the <code>COVERITY_PORT</code> environment variable
     * (or null if the environment variable is unset).
     */
    String port

    /**
     * Specifies the Coverity Connect commit dataport number.
     * <p/>
     * Default value is the <code>COVERITY_DATAPORT</code> environment variable
     * (or null if the environment variable is unset).
     */
    String dataport

    /**
     * Specifies the Coverity Connect commit username.
     * <p/>
     * Default value is the <code>COVERITY_USER</code> environment variable
     * (or null if the environment variable is unset).
     */
    String user

    /**
     * Specifies the Coverity Connect commit password.
     * <p/>
     * Default value is the <code>COVERITY_PASS</code> environment variable
     * (or null if the environment variable is not set).
     */
    String pass
	
	/**
	 * Specifies the source java version used during the translate phase.
	 * <p/>
	 * Indicates which version of the JDK the Java code is written for. Valid values
	 * for version are 1.3, 1.4, 1.5, 1.6, 1.7 and 1.8. The default is 1.4.
	 */
	String sourceVersion
    
    /**
     * Specifies the zip file after build.
     * <p/>
     * Default value is null if the variable is not set.
     */
    String zipfile
    
    /**
     * Specifies the gradle debug mode.
     * <p/>
     * Default value is null if the variable is not set.
     */
    String debugMode

    /**
     * Instantiates a new instance of this extension.
     *
     * @param project project this extension is applied to, which is used to
     *                default the {@link #intermediateDir}.
     */
    CoverityExtension(Project project) {
        intermediateDir = "${project.buildDir}/coverity/intermediate"
		sourceVersion = "1.4"
        coverityHome = System.getenv('COVERITY_HOME')
        stream = System.getenv('COVERITY_STREAM')
        host = System.getenv('COVERITY_HOST')
        port = System.getenv('COVERITY_PORT')
        dataport = System.getenv('COVERITY_DATAPORT')
        user = System.getenv('COVERITY_USER')
        pass = System.getenv('COVERITY_PASS')
        zipfile = ""
        debugMode = ""
    }
}
