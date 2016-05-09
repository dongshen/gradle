package com.github.sdong.gradle.coverityfortify.util;

import static org.junit.Assert.*

import com.github.sdong.gradle.coverityfortify.util.Utils

import org.junit.Test
import org.testng.annotations.Test


public class UtilsTest {

	@Test
	public void testCheckJavaFile() {
		def FileName = 'D:/temp/backup/src/main/java/'

		def hasJavaFile = false 
		
		hasJavaFile = Utils.checkJavaFile(new File(FileName))
		println 'hasJavaFile='+hasJavaFile
		
		FileName = 'D:/temp/backup/src/main/resources'
		hasJavaFile = Utils.checkJavaFile(new File(FileName))
        println 'hasJavaFile='+hasJavaFile
		
		//fail("Not yet implemented");
		
	}
	
	@Test
    public void testZipFolder() {
        def zipFileName = "D:/temp/logs.zip"
        def inputDir = "D:/temp/logs"
        Utils.zipFolder( zipFileName,  inputDir)
        //fail("Not yet implemented");
    }
	
	@Test
	public void testCheckFolderEmpty(){
	   File f = new File('D:/temp/logs/emptyfolder')
       if(!f.exists()){
            println 'folder:'+ f + ' is not exist.'
            return
       }
       
       
       if (!f.directory){
            println 'folder:'+ f + ' is not folder.'
            return
       }
       
       if(!(f.list() as List).empty){
            println 'folder:'+ f + ' is not empty.'
       }else{
            println 'folder:'+ f + ' is empty.'
       }
	}


    /*
    @Test
    public void testZip(){
        def zipFileName = "D:/temp/logs.zip"
        def inputDir = "D:/temp/logs"
        Utils.zip(zipFileName, inputDir,null)

    }
    
    @Test
    public void test7z(){
        def zipFileName = "D:/temp/logs.zip"
        def inputDir = "D:/temp/logs"
        Utils.zipWith7z(zipFileName,  inputDir)

    }
    */
}
