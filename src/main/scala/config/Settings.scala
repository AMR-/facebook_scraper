package config

import model.ExplicitScrapeConfig
import utils.GeneralUtils.{filenameOf, nowStringDay, nowStringMinute}
import utils.JsonParser.toScrapeConfig
import utils.Props
import scala.io.Source.fromFile

object Settings {

  val FB_APP_ID : String = Props("fb_app_id") get
  val FB_APP_SECRET : String = Props("fb_app_secret") get

  private val SCRAPER_JSON_CONFIG_LOCATION : String = Props("scraper_pages_json_config_location") get
  val SCRAPER_PAGES_CONFIG : ExplicitScrapeConfig = {
    toScrapeConfig(fromFile(SCRAPER_JSON_CONFIG_LOCATION) mkString) asExplicitScrapeConfig
  }

  val OUT_FILE_DIR = "out_files"
  val PARSED_FILE_DIR = s"$OUT_FILE_DIR/parsed"
  val MERGED_FILE_DIR = s"$OUT_FILE_DIR/merged"

  private val raw_dir_prefix = "raw"
  def rawJsonFileDir : String = s"$OUT_FILE_DIR/${raw_dir_prefix}_$nowStringDay"
  private val raw_file_prefix = "rawFB"
  def rawJsonFile(pageName : String, i : Int) : String =
    s"$rawJsonFileDir/${raw_file_prefix}_${filenameOf(pageName)}_$i.json"

  def parsedOutputFile : String = s"$PARSED_FILE_DIR/comments_$nowStringMinute.csv"
  def mergedOutputFile : String = s"$MERGED_FILE_DIR/merged_comments_$nowStringMinute.csv"

  /** returns (pageName, pageNo) */
  def pageNameAndNumberFrom(pageFileName : String) : (String, String) = {
    val regex = s"${raw_file_prefix}_(.*)_([0-9])+\\.json".r
    pageFileName match {
      case regex(pageName, number) => (pageName, number)
      case _ => println(s"ERROR: json filename in unexpected format $pageFileName"); (pageFileName, "")
    }
  }

}
