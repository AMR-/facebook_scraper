package model

case class FacebookPageError(error : FBError)

case class FBError(code : Int, message : String)
