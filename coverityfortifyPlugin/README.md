# Coverity &amp; Fortify for gradle plugin

<p>You need add following line in your root build.gradle file:</p>

  //plugin id	
  apply plugin: "com.github.sdong.gradle.coverityfortify"

  repositories {
  
        jcenter()
		//use local coverity & fortify plugin. 
		flatDir { dirs 'plugin' }	 
  }
  
  
  dependencies {

		classpath group: 'com.github.sdong.gradle', name: 'coverityfortifyPlugin', version: '1.2.1' 
  }


  //extension setting for coverity & fortify plugin	    
  coverity_fortify{
  
	coverity{	 
		//coverity home, default it will get base on local environment COVERITY_HOME
		coverityHome = "d:/coverity"
		//coverity intermeditate output directory
		intermediateDir = "d:/cov_tmp"
		//debug mode will not generate class
		debugMode = "false"
		//zip file name, this is optional parameter
		zipfile = "coverity.zip"
	}
	
	fortify{
		//Fortify home, default it will get base on local environment FORTIFY_HOME
		fortifyHome = "d:/fortify"
		//fortify output folder
		intermediateDir = "d:/for_tmp"
		//build id, default value is fortify_buidlId
		fortifyBuildId = "Fortify_build_id"
		//source version 1.4, 1.5, 1.6, 1.7,1.8 default value is 1.4
		sourceVersion = "1.4"
		//zip file name,this is optional parameter
		zipfile = "fortify.zip"
	}
	
}
	
