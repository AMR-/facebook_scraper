package utils

import java.io.File

import model.{ScrapeConfig, FacebookPageComments}
import net.liftweb.json.{DefaultFormats,parse}
import utils.Enrichment.EnrichedFile

object JsonParser {

  def toFacebookPageComment(file : File) : FacebookPageComments =
    toFacebookPageComment(file asString)

  def toFacebookPageComment(jsonString : String) : FacebookPageComments = {
    implicit val formats = DefaultFormats
    val fpcJson = parse(jsonString)
    fpcJson.extract[FacebookPageComments]
  }

  def toScrapeConfig(jsonString : String) : ScrapeConfig = {
    implicit val formats = DefaultFormats
    val scJson = parse(jsonString)
    scJson.extract[ScrapeConfig]
  }

}
