import config.Settings
import Settings.{FB_APP_ID, FB_APP_SECRET}
import Settings.SCRAPER_PAGES_CONFIG.page_infos
import scraper.Scheduler
import scraper.JobTracker.thereAreJobsLeft

object ScrapeFacebook extends App {
  print(s"Initializing Facebook Scraper with page names: ${page_infos.map(_.page_name).mkString("; ")}")
  validateArgsAndSetup(args)
  kickOffFirstJob()

  while(thereAreJobsLeft) {
    print(".")
    Thread.sleep(10000)
  }

  private def validateArgsAndSetup(args : Seq[String]) : Unit = {
    if ((FB_APP_ID equals "[FACEBOOK APP ID]") || (FB_APP_SECRET equals "[FACEBOOK APP SECRET]"))
      throw new RuntimeException("Please specify facebook id and secret in local.properties")
  }

  private def kickOffFirstJob() : Unit = {
    println("Kick off the first job in 5 seconds.")
    Scheduler scheduleScrapeJob 5
  }

}
