# gradle
Coverity &amp; Fortify gradle plugin

<p>You need add following line in your root build.gradle file:</p>


#  apply plugin: "com.github.sdong.gradle.coverityfortify"

#  repositories {
 		jcenter()
 	
		//use local coverity & fortify plugin: coverityfortifyPlugin-1.0.0.jar
		flatDir { dirs 'plugin' }	 
  }
  
#  dependencies {
		classpath group: 'com.github.sdong.gradle', name: 'coverityfortifyPlugin', version: '1.0.0' 
  }

  //extension setting for coverity & fortify plugin	  
  
  coverity_fortify{
	coverity{ 
		//coverity home, default it will get base on local environment COVERITY_HOME
		coverityHome = "d:/coverity"
		//coverity output folder
		intermediateDir = "d:/temp/coverity/intermediate"
	}
	
	fortify{
		//Fortify home, default it will get base on local environment FORTIFY_HOME
		fortifyHome = "d:/fortify"
		//fortify output folder
		intermediateDir = "d:/temp/fortify/output"
		//build id, default value is fortify_buidlId
		fortifyBuildId = "Fortify_build_id"
		//source version 1.4, 1.5, 1.6, 1.7, default value is 1.4
		sourceVersion = "1.4"
	}

	
