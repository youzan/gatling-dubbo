package io.gatling.dubbo.process

import io.gatling.core.Predef.Session
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import io.gatling.dubbo.DubboCheck
import io.gatling.dubbo.action.DubboActionBuilder
import io.gatling.dubbo.check.DubboCheckSupport

case class DubboProcessBuilder[A](requestName: Expression[String], f: (Session) => A, checks: List[DubboCheck] = Nil, threadPoolSize: Int = 200) extends DubboCheckSupport {

  def check(dubboChecks: DubboCheck*): DubboProcessBuilder[A] = copy[A](checks = checks ::: dubboChecks.toList)

  def threadPoolSize(threadPoolSize: Int): DubboProcessBuilder[A] = copy[A](threadPoolSize = threadPoolSize)

  def build(): ActionBuilder = DubboActionBuilder[A](requestName, f, checks, threadPoolSize)
}
