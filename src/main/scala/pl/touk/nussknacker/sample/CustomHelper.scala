package pl.touk.nussknacker.sample

import cats.data.ValidatedNel
import cats.implicits.catsSyntaxValidatedId
import pl.touk.nussknacker.engine.api.generics.{GenericFunctionTypingError, GenericType, TypingFunction}
import pl.touk.nussknacker.engine.api.typed.typing
import pl.touk.nussknacker.engine.api.typed.typing.TypedClass
import pl.touk.nussknacker.engine.api.{Documentation, ParamName}

import scala.language.higherKinds
import scala.reflect.ClassTag

object CustomHelper {

  import scala.collection.JavaConverters._

  @Documentation(description = "Returns diff of two lists")
  @GenericType(typingFunction = classOf[ListElementTyping])
  def diff[T](@ParamName("list1") list1: java.util.List[T], @ParamName("list2") list2: java.util.List[T]): java.util.List[T] = {
    import scala.collection.JavaConverters._
    list1.asScala.filterNot(list2.asScala.toSet).asJava
  }

  @Documentation(description = "Flat list of lists into list")
  @GenericType(typingFunction = classOf[ListElementTyping])
  def flatten[T](@ParamName("list") list: java.util.List[java.util.List[T]]): java.util.List[T] = {
    list.asScala.flatMap(_.asScala).toList.asJava
  }

  class ListElementTyping extends CollectionElementTyping[java.util.List]

  class CollectionElementTyping[F[_]](implicit classTag: ClassTag[F[_]]) extends TypingFunction {
    private val fClass: Class[F[_]] = classTag.runtimeClass.asInstanceOf[Class[F[_]]]

    override def computeResultType(arguments: List[typing.TypingResult]): ValidatedNel[GenericFunctionTypingError, typing.TypingResult] = arguments match {
      case TypedClass(`fClass`, componentType :: Nil) :: _ => componentType.validNel
      case firstArgument :: _ => firstArgument.validNel
      case _ => GenericFunctionTypingError.ArgumentTypeError.invalidNel
    }
  }

}
