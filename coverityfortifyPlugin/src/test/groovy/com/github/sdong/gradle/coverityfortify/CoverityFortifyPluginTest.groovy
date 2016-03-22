package com.github.sdong.gradle.coverityfortify

import com.github.sdong.gradle.coverityfortify.model.CoverityFortifyExtension
import com.github.sdong.gradle.coverityfortify.model.CoverityExtension
import com.github.sdong.gradle.coverityfortify.tasks.CovAnalyzeJavaTask
import com.github.sdong.gradle.coverityfortify.tasks.CovCommitDefectsTask
import com.github.sdong.gradle.coverityfortify.tasks.CovEmitJavaTask
import com.github.sdong.gradle.coverityfortify.tasks.ForTransJavaTask
import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.testfixtures.ProjectBuilder
import org.testng.annotations.Test
import static org.testng.Assert.* 

class CoverityFortifyPluginTest {

    @Test
    public void coverityFortifyPluginAddsTaskToProject() {
       Project project = ProjectBuilder.builder().build()
       project.pluginManager.apply 'com.github.sdong.gradle.coverityfortify'
		
	   println(project)
	   def task = project.task('c4rtakstest', type: CovEmitJavaTask)
	   assertTrue(task instanceof CovEmitJavaTask)
	   println(task) 
	   println 'hello!'
       assertTrue(project.tasks.covEmitJava instanceof CovEmitJavaTask)
    }

	@Test
	public void testSourceDirs(){
		Set<File> srcDirs = ['D:\\git\\sdong\\gradle\\gradletest\\src\\main\\java']
		def src=''
		srcDirs.each{
			println 'srcDirs:'+it
			src= src+ it + File.separator+'**/*.java'
		}
		println 'src='+src
		println srcDirs.join(File.pathSeparator)
		
	}
}
