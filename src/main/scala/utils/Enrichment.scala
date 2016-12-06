package utils

import java.io.File

object Enrichment {

  implicit class EnrichedFile(file : File) {
    def asString: String = FileUtils asString file
  }

  implicit class EnrichedString(string : String) {
    def withCondensedSpaces : String = string.replaceAll("[\\s]+", " ")

    def withStrippedUrls : String = string.replaceAll("http[s]?:\\S*\\s*", "")
  }

}
