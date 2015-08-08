package com.sdong.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


class Code4ReferencePlugin implements Plugin<Project> {

	def void apply(Project project) {

		//c4rTask task has been defined below.

		project.task('c4rTask') << { println 'Hi from Code4Reference plugin!' }

	}

}
