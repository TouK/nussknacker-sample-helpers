package pl.touk.nussknacker.sample

import pl.touk.nussknacker.engine.api.{Documentation, ParamName}

object CustomHelper {
  import scala.collection.JavaConverters._

  @Documentation(description = "Returns diff of two lists")
  def diff[T](@ParamName("list1") list1: java.util.List[T], @ParamName("list2") list2: java.util.List[T]): java.util.List[T] = {
    import scala.collection.JavaConverters._
    list1.asScala.filterNot(list2.asScala.toSet).asJava
  }

  @Documentation(description = "Flat list of lists into list")
  def flatten[T](@ParamName("list") list: java.util.List[java.util.List[T]]): java.util.List[T] = {
    list.asScala.map(_.asScala).flatten.toList.asJava
  }

}
