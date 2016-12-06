package scraper

import model.ExplicitPageInfo
import utils.GeneralUtils._
import utils.Props

import scala.collection.mutable

import config.Settings.SCRAPER_PAGES_CONFIG.page_infos

/**
 * Tracks info on current job, past jobs, future jobs.
 */
object JobTracker {

  val SECONDS_TO_WAIT_BETWEEN_REQUESTS : Int = Props("time_to_wait_between_requests").get toInt

  /** ---- History ---- */
  val completedJobs : mutable.MutableList[String] = mutable.MutableList()

  /** ---- Next (or current) job info ---- */
  /** which number of the PAGE_NAMEs array are we on */
  private var currentPageNameNumber : Int = 0
  /** which number of the specific page's pages we are on as we page through */
  private var currentPagingPageNumber : Int = 0
  /** url for the next page, as retrieved from the first page */
  private var nextPage : Option[String] = None

  private var jobsLeft : Boolean = true

  /** returns pageInfo, pageNumber, nextPage */
  final def nextJobInfo : (ExplicitPageInfo, Int, Option[String]) =
    (page_infos(currentPageNameNumber), currentPagingPageNumber, nextPage)

  final def doneJob(jobName : String) : Unit = completedJobs += jobName

  private def hasNextPageName : Boolean = currentPageNameNumber < page_infos.length - 1

  final def nextTimeDoNextPageForPageName(nxtPage : String) : Unit = {
    println(s"Next time do next page for page name.\n" +
      s"Updating job info from $nextJobInfo to...")
    currentPagingPageNumber += 1
    nextPage = Some(nxtPage)
    println(s"...$nextJobInfo")
    scheduleNextJob()
  }

  final def nextTimeDoNewPageName() : Unit = {
    if (hasNextPageName) {
      println(s"Next time do new page name.\n" +
        s"Updating job info from $nextJobInfo to...")
      currentPageNameNumber += 1
      currentPagingPageNumber = 0
      nextPage = None
      println(s"...$nextJobInfo")
      scheduleNextJob()
    } else {
      println("DONE WITH ALL JOBS.  Not scheduling any more.")
      jobsLeft = false
    }
  }

  final def thereAreJobsLeft = jobsLeft

  private def scheduleNextJob(): Unit = {
    println(s"At $nowStringMinute, scheduling the next Scrape Job.")
    Scheduler scheduleScrapeJob SECONDS_TO_WAIT_BETWEEN_REQUESTS
    println(s"At $nowStringMinute, next scrape job scheduled.")
  }

}
