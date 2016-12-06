package utils

import java.io.File

import config.Settings.{rawJsonFileDir,rawJsonFile,parsedOutputFile,mergedOutputFile}
import com.github.tototoshi.csv.{CSVReader, CSVWriter}

object FileUtils {

  final def asString(file : File) : String = {
    scala.io.Source fromFile file mkString
  }

  final def exists(filePath : String): Boolean = scala.tools.nsc.io.File(filePath).exists

  final def isDirectory(dirPath : String) : Boolean = scala.tools.nsc.io.File(dirPath).isDirectory

  final def writeStringToFile(string: String, pageName : String, i : Int): Unit = {
    new File(rawJsonFileDir) mkdirs()
    scala.tools.nsc.io.File(rawJsonFile(pageName, i)).writeAll(string)
  }

  final def allFromDir(fullDir : String) : List[File] = {
    new java.io.File(fullDir).listFiles toList
//    java.nio.file.Files.newDirectoryStream(s"$OUT_FILE_DIR/$dirBaseName")
  }

  final def readCSV(file: File) : List[List[String]] = {
    val reader = CSVReader.open(file)
    try
      reader all()
    finally
      reader close()
  }

  final def writeToParsedCSV(listOfLists : List[List[String]]) : Unit = {
    writeToCSV(listOfLists, parsedOutputFile)
  }

  final def writeToMergedCSV(listOfLists : List[List[String]]) : Unit = {
    writeToCSV(listOfLists, mergedOutputFile)
  }

  private final def writeToCSV(listOfLists : List[List[String]], filePath : String) : Unit = {
    val file = new File(filePath)
    val writer = CSVWriter.open(file)
    writer.writeAll(listOfLists)
    writer.close()
  }

}
