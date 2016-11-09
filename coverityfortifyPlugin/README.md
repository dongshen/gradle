# gradle plugin for Coverity &amp; Fortify 

This gradle plugin will help you to generate  intermediate file for static scan tools `Coverity` and `Fortify`. 

You need follow the guide to setup the plugin in root build.gradle file and set related parameter which will be used for `Coverity` and `Fortify` to generate intermediate file.

After setup this plugin, you can use command `gradle task` to check whether the plugin is setup properly. You will find 2 new task in task list, `covEmitJava` is the task for `Coverity` and `forTransJava` is the task for `Fortify`.  

![GitHub](https://github.com/dongshen/gradle/blob/master/coverityfortifyPlugin/release/task.jpg?raw=true) 

You can use following command to generate intermediate file for Coverity and Fortify both or individually: <br>
* Both for `Coverity` and `Fortify`: `gradle clean assemble covEmitJava forTransJava` <br>
* Only for `Coverity`: `gradle clean assemble covEmitJava` <br>
* Only for `Fortify`: `gradle clean assemble forTransJava` <br>

#Build
gradle clean assemble


# Plugin Setting in build.gradle

  //plugin id <br>	
  apply plugin: "com.github.sdong.gradle.coverityfortify"

  repositories {
  
        jcenter()
		//use local coverity & fortify plugin. 
		flatDir { dirs 'plugin' }	 
  }
  
  
  dependencies {

		classpath group: 'com.github.sdong.gradle', name: 'coverityfortifyPlugin', version: '1.2.3' 
  }


  //extension setting for coverity & fortify plugin	    
  coverity_fortify{
  
	coverity{	 
		//coverity home, default it will get base on local environment COVERITY_HOME
		coverityHome = "d:/coverity"
		//coverity intermeditate output directory
		intermediateDir = "d:/cov_tmp"
        //source version 1.4, 1.5, 1.6, 1.7,1.8 default value is 1.4
        sourceVersion = "1.4"		
		//debug mode will not generate class
		debugMode = "false"
		//zip file name, this is optional parameter
		zipfile = "coverity.zip"
	}
	
	fortify{
		//Fortify home, default it will get base on local environment FORTIFY_HOME
		fortifyHome = "d:/fortify"
		//fortify output folder, it should be the same with fortify-sca.properites. This setting is for zip purpose.
		intermediateDir = "d:/for_tmp"
		//build id, default value is fortify_buidlId
		fortifyBuildId = "Fortify_build_id"
		//source version 1.4, 1.5, 1.6, 1.7,1.8 default value is 1.4
		sourceVersion = "1.4"
		//zip file name,this is optional parameter
		zipfile = "fortify.zip"
	}
	
}
	
