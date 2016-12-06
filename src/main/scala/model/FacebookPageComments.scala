package model

class FacebookPageComments(val data:List[Data], val paging: PagePaging)

case class Data(comments: Comments, id:String, message:String)

case class Comments(data : List[CommentData], paging : Option[CommentPaging], summary : CommentSummary)

case class CommentData(created_time : String, from : CommentDataFrom, id : String, message : String)

case class CommentDataFrom(id : String, name : String)

case class CommentPaging(cursors : CommentPagingCursors)

case class CommentPagingCursors(after : String, before : String)

case class CommentSummary(can_comment : Boolean, order : String, total_count : Int)

case class PagePaging(next : String, previous : String)
