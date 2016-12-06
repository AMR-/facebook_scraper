package scraper

import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit.SECONDS

object Scheduler {

  val actorSystem = akka.actor.ActorSystem()
  val scheduler = actorSystem.scheduler
  implicit val executor = actorSystem.dispatcher

  def scheduleScrapeJob(seconds : Int): Unit =
    scheduler.scheduleOnce(Duration(seconds, SECONDS), new ScrapeJob)

}
