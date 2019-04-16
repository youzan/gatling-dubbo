package io.gatling.dubbo

import io.gatling.core.Predef.Session
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import io.gatling.dubbo.check.DubboCheckSupport
import io.gatling.dubbo.process.DubboProcessBuilder

import scala.language.implicitConversions

trait DubboDsl extends DubboCheckSupport {

  def dubbo[A](requestName: Expression[String], f: (Session) => A) = DubboProcessBuilder[A](requestName, f)

  implicit def dubboProcessBuilder2ActionBuilder[A](builder: DubboProcessBuilder[A]): ActionBuilder = builder.build()

}
