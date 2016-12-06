package scraper

import config.Settings.{FB_APP_ID, FB_APP_SECRET}
import model.ExplicitPageInfo
import utils.Props

import scalaj.http.Http
import scalaj.http.HttpOptions.connTimeout

object FB_Http {

  val ACCESS_TOKEN : String = s"$FB_APP_ID|$FB_APP_SECRET"

  val facebookQueryBase : String = Props("facebook_query_base") get
  val queryTimeout : Int = Integer.valueOf(Props("query_timeout") get)

  def request(pg : ExplicitPageInfo) : String = {
    Http(s"$facebookQueryBase/${pg page_name}/feed/").params(
      "fields" -> s"message,comments.limit(${pg max_number_comments_per_message}}).summary(true),shares",
      "limit" -> s"${pg messages_per_page}",
      "access_token" -> ACCESS_TOKEN
    ).option(connTimeout(queryTimeout)).asString body
  }

  def requestRaw(rawString : String) : String = {
    Http(rawString).option(connTimeout(queryTimeout)).asString body
  }

}
