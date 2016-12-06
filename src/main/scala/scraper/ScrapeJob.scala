package scraper

import model.{ExplicitPageInfo, FacebookPageComments}
import utils.FileUtils.writeStringToFile
import utils.GeneralUtils.nowStringMinute
import utils.JsonParser.toFacebookPageComment

import scala.util.{Failure, Success, Try}

class ScrapeJob extends Runnable {

  val jobInfo : (ExplicitPageInfo, Int, Option[String]) = JobTracker nextJobInfo
  val pageInfo : ExplicitPageInfo = jobInfo._1
  val pageName : String = pageInfo page_name
  /** for a given pagename, what are the max number of pages to page through */
  val maxPageToQuery : Int = pageInfo max_pages_to_query
  val pageNo : Int = jobInfo._2
  val nextPage : Option[String] = jobInfo._3

  override def run(): Unit = {
    println(s"Scrape Job started at $nowStringMinute.\n" +
      s"Page Name: $pageName,\n" +
      s"Messages Per Page: ${pageInfo messages_per_page}," +
      s"Comments Per Message: ${pageInfo max_number_comments_per_message}," +
      s"Max Number of Pages to Query: $maxPageToQuery\n" +
      s"Page Name: $pageName, Page #: $pageNo, next page: ${nextPage.getOrElse("[none]")}")
    val json : String = runScrapeJob()
    setupNextJobInfo(json)
    JobTracker doneJob s"${pageName}_$pageNo"
    println(s"Scrape Job completed at $nowStringMinute")
  }

  private def runScrapeJob() : String = {
    println(s"Get information for page $pageName page $pageNo")
    val rawJsonString =
    nextPage match {
      case Some(nextPageUrl) => FB_Http requestRaw nextPageUrl
      case None => FB_Http request pageInfo
    }
    println(s"Raw json string retrieved at $nowStringMinute. Writing to file.")
    writeStringToFile(rawJsonString, pageName, pageNo)
    println(s"Wrote to file.")
    rawJsonString
  }

  private def setupNextJobInfo(json : String) : Unit = {
    if (json.startsWith("{\"error\"")) {
      println("There is an error, so moving on to next page name.")
      JobTracker nextTimeDoNewPageName()
    } else if (pageNo >= maxPageToQuery) {
      println(s"Current page number $pageNo is at or exceeds $maxPageToQuery, so moving on to next page name.")
      JobTracker nextTimeDoNewPageName()
    } else {
      println("Attempting to get the url for the next page.")
      getNextUrl(json) match {
        case Success(url) =>
          println(s"The url for the next page is $url, setting it as the next job.")
          JobTracker nextTimeDoNextPageForPageName url
        case Failure(e) =>
          println(s"There was a failure getting next url (${e.getMessage}), moving on" +
            s" to the next page.  See below for full error.")
          println(e)
          JobTracker nextTimeDoNewPageName()
      }
    }
  }

  /** try to get the next url. */
  private def getNextUrl(json : String) : Try[String] = {
    try {
      val fpc: FacebookPageComments = toFacebookPageComment(json)
      Success(fpc.paging.next)
    } catch {
      case e : Exception=> Failure(e)
    }
  }

}
