package com.github.sdong.gradle.coverityfortify

import com.github.sdong.gradle.coverityfortify.model.Extension
import com.github.sdong.gradle.coverityfortify.model.CoverityRootExtension
import com.github.sdong.gradle.coverityfortify.tasks.CovAnalyzeJavaTask
import com.github.sdong.gradle.coverityfortify.tasks.CovCommitDefectsTask
import com.github.sdong.gradle.coverityfortify.tasks.CovEmitJavaTask
import com.github.sdong.gradle.coverityfortify.tasks.ForTransJavaTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.testng.annotations.Test
import static org.testng.Assert.assertEquals 

class CoverityFortifyPluginTest {

    @Test
    public void coverityFortifyPluginAddsTaskToProject() {
        Project project = ProjectBuilder.builder().build()
       // project.pluginManager.apply 'com.github.sdong.gradle.coverityfortify'

       // assertTrue(project.tasks.covEmitJava instanceof CovEmitJavaTask)
    }

}
