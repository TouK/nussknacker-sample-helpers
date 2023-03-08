package pl.touk.nussknacker.sample

import pl.touk.nussknacker.engine.api.process._

class CustomProcessConfigCreator extends EmptyProcessConfigCreator {

  override def expressionConfig(processObjectDependencies: ProcessObjectDependencies): ExpressionConfig =
    ExpressionConfig(
      Map(
        "CUSTOM_HELPER" -> WithCategories.anyCategory(CustomHelper)
      ),
      List.empty
    )

}
