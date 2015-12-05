package com.github.sdong.gradle.coverityfortify.util

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Utility class containing static methods.
 *
 * @author sdong
 */
class Utils {
    /**
     * Returns the path to an executable given a home directory and file name.
     *
     * @param homeDir the executable's home directory
     * @param executableFileName the executable's file name
     * @return "${homeDir}/bin/${executableFileName}" if homeDir is not null or
     *         empty, otherwise <code>executableFileName</code>
     */
    static String getExePath(String homeDir, String executableFileName) {
        if (homeDir != null && !homeDir.isEmpty()
                && !homeDir.isAllWhitespace()) {
            return homeDir+File.separator+"bin"+File.separator+executableFileName
        }

        return executableFileName
    }
    
    /**
     * zip all files in directory and sub-directory.
     *
     * @param zipFileName zip file name include file path
     * @param inputDir zip files initial folder
     * @return
     *
     */
    static void zipFolder(String zipFileName, String inputDir) {

        println("Start to zip")
        def baseFolder = new File(inputDir).getAbsolutePath()
        def baseFolderLength = baseFolder.length() + 1

        println("Base folder: "+ baseFolder)
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))

        zipFiles(zipFile,inputDir,baseFolderLength)

        zipFile.close()
        println("End of zip")
    }

    static void zipFiles(ZipOutputStream zipFile,String inputDir,int baseFolderLength) {

        new File(inputDir).eachFile() { file ->

            if(file.isDirectory()){
                zipFiles(zipFile,file.getAbsolutePath(),baseFolderLength)
            }else{
                println("Zip "+file.getAbsolutePath())
                zipFile.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(baseFolderLength)))
                zipFile << new FileInputStream(file)
                zipFile.closeEntry()
            }
        }
    }
}
