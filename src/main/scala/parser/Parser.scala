package parser

import model.{FacebookPageCommentsWithPageName, Data, CommentData}
import utils.Enrichment.EnrichedString

object Parser {

  val CSV_HEADER = List("page_id", "page_name", "page_feed_post",
    "comment_id", "comment_timestamp", "comment_author", "comment_message")

  /**
   * Grab the desired data from the fb objects
   */
  final def extractCommentData(pages : List[FacebookPageCommentsWithPageName]) : List[List[String]] = {
    pages flatMap toRows distinct
  }

  private def toRows(fpc : FacebookPageCommentsWithPageName): List[List[String]] = {
    fpc.data flatMap(toRows(_, fpc pageName))
  }

  private def toRows(post : Data, pageName : String) : List[List[String]] = {
    post.comments.data map(toRow(_,
      // page id
      post id,
      // pageName,
      pageName,
      // page feed post
      post message
    ))
  }

  private def toRow(comment : CommentData, pageId : String, pageName : String, pagePostInFeed : String) : List[String] = {
    List(
      // page id
      pageId,
      // page name
      pageName,
      // page feed post
      pagePostInFeed.withCondensedSpaces withStrippedUrls,
      // comment id
      comment id,
      // comment timestamp
      comment created_time,
      // comment author
      comment.from name,
      // comment message
      comment.message.withCondensedSpaces withStrippedUrls
    )
  }

}
