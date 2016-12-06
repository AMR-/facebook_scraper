package utils

import java.util.Properties
import scala.collection.JavaConverters._
import utils.GeneralUtils.using

object Props {

  val DEFAULT_PROPERTIES_FILE = "local.default.properties"
  val MAIN_PROPERTIES_FILE = "local.properties"

  // props load is like props.load(_)
  private val localProperties : Map[String, String] = {
    val props = new Properties()
    using(getClass.getClassLoader getResourceAsStream DEFAULT_PROPERTIES_FILE)(props load)
    using(getClass.getClassLoader getResourceAsStream MAIN_PROPERTIES_FILE)(props load)
    props.asScala.toMap
  }

  def get(name : String) : Option[String] = {
    localProperties get name
  }

  def apply(name : String) : Option[String] = get(name)

}
