package com.github.sdong.gradle.coverityfortify.config

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.variant.BaseVariantData
import com.android.build.gradle.internal.variant.TestVariantData
import com.android.builder.core.BuilderConstants
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

/**
 * A set of {@link ProjectBuildConfig}s for a given project that is populated when
 * constructing this type.
 *
 * @author sdong
 */
class ProjectBuildConfigSet {
    /**
     * The base project for this set.
     */
    Project baseProject

    /**
     * Projects that have already been populated for this config set.
     */
    Set<Project> populatedProjects

    /**
     * Project build configs for base project and all its child projects (recursively),
     * which are created during population.
     */
    Set<ProjectBuildConfig> projectBuildConfigs

    /**
     * Initializes and populates a new set of {@link ProjectBuildConfig}s for the given
     * {@link Project}.
     *
     * @param baseProject project to get a set of Project build configs for
     */
    ProjectBuildConfigSet(Project baseProject) {
        this.baseProject = baseProject
        populatedProjects = []
        projectBuildConfigs = []

        populate(baseProject)
    }

    /**
     * Recursively populates {@link ProjectBuildConfig}s for the given project, skipping
     * those specified in their extension.  When skipped, child projects are
     * still evaluated unless it is also specified that they should not be
     * included.
     *
     * @param project (sub)project to populate Project build configs for
     */
    void populate(Project project) {
        if (populatedProjects.contains(project)) {
            return
        }

        if (!project.coverity_fortify.skip) {
            if (checkIsJavaProject(project)) {
                populateForJava(project)
            } else if (checkIsAndroidProject(project)) {
                populateForAndroid(project)
            }
        }

        /*
         * Mark as populated even if skipped so we don't attempt to iterate
         * child projects again.  Mark before iterating child projects so we
         * don't end up with an infinite recursive loop.
         */
        populatedProjects += project

        // Iterate child projects even if skipped
        if (baseProject.coverity_fortify.includeChildProjects) {
            for (Project childProject : project.childProjects.values()) {
                populate(childProject)
            }
        }

    }

    /**
     * Populates the {@link ProjectBuildConfig}s for a Java project (no child projects).
     * One config is created per {@link org.gradle.api.tasks.SourceSet} that has
     * a Java compilation task that is part of the <code>assemble</code> task.
     *
     * @param project project to populate Project build configs for
     */
    void populateForJava(Project project) {
        project.convention.getPlugin(JavaPluginConvention).sourceSets.each { sourceSet ->
            Task compileJavaTask = project.tasks.getByName(sourceSet.compileJavaTaskName)

            boolean inAssemble = taskDependenciesContainsTask(project.tasks.assemble, compileJavaTask, true)

            if (inAssemble) {
                projectBuildConfigs += new ProjectBuildConfig().with {
                    it.sourceDirs += sourceSet.allSource.srcDirs
                    it.compilerOutputDirs += sourceSet.output.files
                    it.classpath += sourceSet.compileClasspath
                    it.classpath += sourceSet.runtimeClasspath
                    return it
                }
            }
        }
    }

    /**
     * Populates the {@link ProjectBuildConfig}s for an Android project (no child
     * projects).  One config is created per {@link BaseVariantData} that is of
     * the <code>debug</code> build type and not a test variant.
     * <p/>
     * This logic should be skipped entirely if not an Android project in order
     * to avoid {@link ClassNotFoundException}s and
     * {@link NoClassDefFoundError}s.
     *
     * @param project project to populate Project build configs for
     */
    void populateForAndroid(Project project) {
        BasePlugin plugin

        if (project.plugins.hasPlugin(AppPlugin)) {
            plugin = project.plugins.getPlugin(AppPlugin)
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            plugin = project.plugins.getPlugin(LibraryPlugin)
        } else {
            throw new GradleException(
                    'Attempted to populate for Android when project does not apply Android plugin')
        }

        // Variant = Product Flavor + Build Type
        // This loop should hit all possible versions of the app
        for (BaseVariantData variantData : plugin.variantManager.variantDataList) {
            // Only use the debug build type (Coverity requires debug build)
            // Exclude test variants
            if (!variantData.variantConfiguration.buildType.name.equals(
                    BuilderConstants.DEBUG)
                    || variantData instanceof TestVariantData) {
                continue
            }

            ProjectBuildConfig projectBuildConfig = new ProjectBuildConfig()

            variantData.variantConfiguration.sortedSourceProviders.each { sourceProvider ->
                projectBuildConfig.sourceDirs += sourceProvider.javaDirectories
            }

            projectBuildConfig.sourceDirs +=
                    variantData.generateRClassTask.sourceOutputDir
            projectBuildConfig.sourceDirs +=
                    variantData.generateBuildConfigTask.sourceOutputDir
            projectBuildConfig.sourceDirs +=
                    variantData.aidlCompileTask.sourceOutputDir

            if (!variantData.variantConfiguration.renderscriptNdkModeEnabled) {
                projectBuildConfig.sourceDirs +=
                        variantData.renderscriptCompileTask.sourceOutputDir
            }

			if (variantData.hasProperty('javacTask')) {
                // Android plugin 1.3.x
                projectBuildConfig.compilerOutputDirs +=
                        variantData.javacTask.outputs.files.files

                projectBuildConfig.classpath +=
                        variantData.javacTask.classpath
            } else {
                projectBuildConfig.compilerOutputDirs +=
                        variantData.javaCompilerTask.outputs.files.files

                projectBuildConfig.classpath +=
                        variantData.javaCompilerTask.classpath
            }
            
            projectBuildConfig.classpath +=
                    project.files(plugin.androidBuilder.bootClasspath)

            projectBuildConfigs += projectBuildConfig
        }
    }

    /**
     * Checks if the given project applies the {@link JavaPlugin}.
     *
     * @param project project to check
     * @return true if Java project, otherwise false
     */
    static boolean checkIsJavaProject(Project project) {
        return project.plugins.hasPlugin(JavaPlugin)
    }

    /**
     * Checks if the given project applies the {@link AppPlugin} or
     * {@link LibraryPlugin}.  Will handle {@link ClassNotFoundException}s and
     * {@link NoClassDefFoundError}s in the event that the Android plugin is not
     * part of the build script.
     *
     * @param project project to check
     * @return true if Android project, otherwise false
     */
    static boolean checkIsAndroidProject(Project project) {
        try {
            return (project.plugins.hasPlugin(AppPlugin)
                    || project.plugins.hasPlugin(LibraryPlugin))
        } catch (ClassNotFoundException e) {
            project.logger.debug(
                    "Assuming ${project.name} is not an Android project.", e)
        } catch (NoClassDefFoundError e) {
            project.logger.debug(
                    "Assuming ${project.name} is not an Android project.", e)
        }
        false
    }

    /**
     * Checks if a given parent task's dependencies includes the given child,
     * optionally recursive.
     *
     * @param parent parent task
     * @param child child task
     * @param recursive defaults to false
     * @return true if child is a dependency of the parent, otherwise false
     */
    static boolean taskDependenciesContainsTask(Task parent, Task child,
                boolean recursive = false) {
        Set<Task> parentDependencies =
                parent.taskDependencies.getDependencies(parent)

        if (parentDependencies.contains(child)) {
            return true
        }

        if (!recursive) {
            return false
        }

        for (Task dependency : parentDependencies) {
            if (taskDependenciesContainsTask(dependency, child, true)) {
                return true
            }
        }

        return false
    }
}
