package model

case class ScrapeConfig(
                       default_max_number_comments_per_message : Int,
                       default_messages_per_page : Int,
                       default_max_pages_to_query_per_pageName : Int,
                       page_infos : List[PageInfo]
                         ) {
  def asExplicitScrapeConfig : ExplicitScrapeConfig = ExplicitScrapeConfig(this)
}

case class PageInfo(
                   page_name : String,
                   max_number_comments_per_message : Option[Int],
                   messages_per_page: Option[Int],
                   max_pages_to_query : Option[Int]
                     )

case class ExplicitScrapeConfig(page_infos : List[ExplicitPageInfo])

case class ExplicitPageInfo(
                             page_name : String,
                             max_number_comments_per_message : Int,
                             messages_per_page: Int,
                             max_pages_to_query : Int
                             ) {
  override def toString =
    s"FBPage(name: $page_name, comments per msg: $max_number_comments_per_message, messages per page: $messages_per_page, pages to query: $max_pages_to_query)"

}

object ExplicitScrapeConfig {
  def apply(sc: ScrapeConfig) : ExplicitScrapeConfig = {
    ExplicitScrapeConfig(
      sc.page_infos.map(
        (pi : PageInfo) => ExplicitPageInfo(
          pi page_name,
          pi.max_number_comments_per_message getOrElse(sc default_max_number_comments_per_message) ,
          pi.messages_per_page getOrElse(sc default_messages_per_page),
          pi.max_pages_to_query getOrElse(sc default_max_pages_to_query_per_pageName)
        )
      )
    )
  }
}
