import java.io.File

import config.Settings.{PARSED_FILE_DIR, MERGED_FILE_DIR}
import utils.FileUtils.{readCSV,writeToMergedCSV,allFromDir}
import parser.Parser.CSV_HEADER

object MergeCsvs extends App {

  println("Merge Data!")

  val files : List[File] = allFromDir(PARSED_FILE_DIR)

  println(s"${files size} files found in parsed filed directory $PARSED_FILE_DIR")
  
  /** a list of the data from each file, which are each list of list of strings
    * The tail is returned to remove the first row which is assumed to be a header. */
  val data : List[List[List[String]]] = files.map(readCSV(_) tail)

  println(s"Read in ${data size} files successfully.")

  /** create merged csv list */
  val mergedData : List[List[String]] = CSV_HEADER :: ((data flatten) distinct)

  println(s"Created merged data, there are ${mergedData size} rows")
  writeToMergedCSV(mergedData)
  println(s"Data has been successfully written to file, look for it in $MERGED_FILE_DIR")

}
