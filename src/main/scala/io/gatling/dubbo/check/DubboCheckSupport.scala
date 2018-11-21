package io.gatling.dubbo.check

import io.gatling.core.check.extractor.jsonpath.JsonPathExtractorFactory
import io.gatling.core.json.JsonParsers
import io.gatling.core.session.Expression

trait DubboCheckSupport {

  def jsonPath(path: Expression[String])(implicit extractorFactory: JsonPathExtractorFactory, jsonParsers: JsonParsers) =
    DubboJsonPathCheckBuilder.jsonPath(path)

}
