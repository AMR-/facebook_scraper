import config.Settings.{OUT_FILE_DIR,PARSED_FILE_DIR}
import parser.Parser.CSV_HEADER
import model.FacebookPageCommentsWithPageName
import utils.FileUtils.{exists,isDirectory,writeToParsedCSV}
import parser.FileWrangler.readAllFBPCommentsFrom
import parser.Parser.extractCommentData

object ParseData extends App {

  println("Parse Data!")

  val dir = validateArgs(args)
  println(s"The requested directory is: $dir")

  val fbPageComments : List[FacebookPageCommentsWithPageName] = readAllFBPCommentsFrom(dir)
  println(s"There are ${fbPageComments.length} FacebookPage pages successfully retrieved " +
    s"from the same number of files.")

  val selectedData : List[List[String]] =
    CSV_HEADER :: extractCommentData(fbPageComments)
  println(s"the selected data comprises ${selectedData.length - 1} rows of data.")

  writeToParsedCSV(selectedData)
  println("Data has been successfully written to file.")

  def validateArgs(args : Array[String]) : String = {
    if (args isEmpty)
      throw new RuntimeException("No folder specified!  Specify the name of the folder in out_files to parse.")
    val dir = s"$OUT_FILE_DIR/${args(0)}"
    if (!exists(dir))
      throw new RuntimeException(s"The directory '$dir' does not exist.")
    if (!isDirectory(dir))
      throw new RuntimeException(s"'$dir' is not a directory.")
    if (!exists(PARSED_FILE_DIR))
      throw new RuntimeException(s"Please create a directory at $PARSED_FILE_DIR")
    dir
  }

}
