package model

case class FacebookPageCommentsWithPageName(pageName : String, number : String, d : List[Data], p : PagePaging) extends FacebookPageComments(d, p)

object FacebookPageCommentsWithPageName {

  def apply(fpc: FacebookPageComments, pageName : String, number : String) : FacebookPageCommentsWithPageName =
    FacebookPageCommentsWithPageName(pageName, number, fpc.data, fpc.paging)

}

object NullFacebookPageCommentsWithPageName extends FacebookPageCommentsWithPageName(null, null, null, null)
