package io.gatling.dubbo.action

import java.util.concurrent.Executors

import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.Predef.Session
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioContext
import io.gatling.dubbo.DubboCheck

case class DubboActionBuilder[A](requestName: Expression[String], f: (Array[Object], Session) => A, param: Array[Object], checks: List[DubboCheck], threadPoolSize: Int) extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx._
    val executor = Executors.newFixedThreadPool(threadPoolSize)
    val objectMapper: ObjectMapper = new ObjectMapper()
    new DubboAction[A](requestName, f, param, executor, objectMapper, checks, coreComponents, throttled, next)
  }

}
