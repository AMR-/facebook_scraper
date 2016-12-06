package utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

object GeneralUtils {

  private val toTheMinute : DateTimeFormatter = ofPattern("yyyy-MM-dd_kk-mm")

  private val toTheDay : DateTimeFormatter = ofPattern("yyyy-MM-dd")

  def now : ZonedDateTime = ZonedDateTime.now

  /** current time as a year-month-day_hour:minute string */
  def nowStringMinute : String = now format toTheMinute

  def nowStringDay : String = now format toTheDay

  /** sanitize a string for use as a filename */
  def filenameOf(string : String) : String = string.replaceAll("[^\\w\\d]","")

  def using[A, B <: {def close(): Unit}] (closeable: B) (f: B => A): A =
    try {
      f(closeable)
    } finally {
      if (closeable != null)
        closeable close()
    }

}
