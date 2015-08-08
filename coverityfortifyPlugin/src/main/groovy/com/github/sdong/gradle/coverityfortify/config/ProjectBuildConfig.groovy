package com.github.sdong.gradle.coverityfortify.config

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.file.UnionFileCollection

/**
 * Configuration for a single execution of project build info that holds
 * the files to be specified for the <code>project_source</code>,
 * <code>project_compiler_output</code>, and <code>project_classpath</code> arguments.
 *
 * @author sdong
 */
class ProjectBuildConfig {
    /**
     * Directories to be passed to the <code>project_source</code> argument.
     */
    Set<File> sourceDirs = []

    /**
     * Directories to be passed to the <code>project_compiler_output</code> argument.
     */
    Set<File> compilerOutputDirs = []

    /**
     * The classpath to be passed to the <code>project_classpath</code> argument.
     */
    FileCollection classpath = new UnionFileCollection()
}
