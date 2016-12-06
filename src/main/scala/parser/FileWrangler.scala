package parser

import model.{NullFacebookPageCommentsWithPageName, FacebookPageCommentsWithPageName}
import utils.FileUtils.allFromDir
import utils.JsonParser.toFacebookPageComment
import java.io.File
import config.Settings.pageNameAndNumberFrom

object FileWrangler {

  final def readAllFBPCommentsFrom(directory : String) : List[FacebookPageCommentsWithPageName] = {
    println(s"Count of files in directory: ${allFromDir(directory) size}")
    allFromDir(directory).map(toFacebookPageCommentWithName)
      .filterNot(_ equals NullFacebookPageCommentsWithPageName)
  }

  private def toFacebookPageCommentWithName(file: File) : FacebookPageCommentsWithPageName = {
    try {
      val pageNameAndNumber : (String, String) = pageNameAndNumberFrom(file getName)
      FacebookPageCommentsWithPageName(
        toFacebookPageComment(file), pageNameAndNumber._1, pageNameAndNumber._2
      )
    } catch {
      case e : Exception =>
        println(s"There was an exception processing page ${file getName}, assuming it is" +
          s" error file, skipping")
        NullFacebookPageCommentsWithPageName
    }
  }

}
