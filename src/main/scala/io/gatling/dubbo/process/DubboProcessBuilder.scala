package io.gatling.dubbo.process

import io.gatling.commons.validation.Success
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import io.gatling.dubbo.DubboCheck
import io.gatling.dubbo.action.DubboActionBuilder
import io.gatling.dubbo.check.DubboCheckSupport

case class DubboProcessBuilder(interface: String, method: String, argTypes: Expression[Array[String]] = _ => Success(Array.empty[String]), argValues: Expression[Array[Object]] = _ => Success(Array.empty[Object]), checks: List[DubboCheck] = Nil) extends DubboCheckSupport {

  def argTypes(argTypes: Expression[Array[String]]): DubboProcessBuilder = copy(argTypes = argTypes)

  def argValues(argValues: Expression[Array[Object]]): DubboProcessBuilder = copy(argValues = argValues)

  def check(dubboChecks: DubboCheck*): DubboProcessBuilder = copy(checks = checks ::: dubboChecks.toList)

  def build(): ActionBuilder = DubboActionBuilder(interface, method, argTypes, argValues, checks)
}
