package io.gatling.dubbo.check

import io.gatling.core.check.extractor.jsonpath._
import io.gatling.core.check.{ DefaultMultipleFindCheckBuilder, Preparer }
import io.gatling.core.json.JsonParsers
import io.gatling.core.session.{ Expression, RichExpression }
import io.gatling.dubbo._

trait DubboJsonPathOfType {
  self: DubboJsonPathCheckBuilder[String] =>

  def ofType[X: JsonFilter](implicit extractorFactory: JsonPathExtractorFactory) = new DubboJsonPathCheckBuilder[X](path, jsonParsers)
}

object DubboJsonPathCheckBuilder {

  val CharsParsingThreshold = 200 * 1000

  def preparer(jsonParsers: JsonParsers): Preparer[String, Any] =
    response => {
      if (response.length() > CharsParsingThreshold || jsonParsers.preferJackson)
        jsonParsers.safeParseJackson(response)
      else
        jsonParsers.safeParseBoon(response)
    }

  def jsonPath(path: Expression[String])(implicit extractorFactory: JsonPathExtractorFactory, jsonParsers: JsonParsers) =
    new DubboJsonPathCheckBuilder[String](path, jsonParsers) with DubboJsonPathOfType
}

class DubboJsonPathCheckBuilder[X: JsonFilter](
    private[check] val path:        Expression[String],
    private[check] val jsonParsers: JsonParsers
)(implicit extractorFactory: JsonPathExtractorFactory)
  extends DefaultMultipleFindCheckBuilder[DubboCheck, String, Any, X](
    DubboStringExtender,
    DubboJsonPathCheckBuilder.preparer(jsonParsers)
  ) {

  import extractorFactory._

  def findExtractor(occurrence: Int) = path.map(newSingleExtractor[X](_, occurrence))
  def findAllExtractor = path.map(newMultipleExtractor[X])
  def countExtractor = path.map(newCountExtractor)
}
