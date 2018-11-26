package io.gatling.dubbo

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.dubbo.check.DubboCheckSupport
import io.gatling.dubbo.process.DubboProcessBuilder
import io.gatling.dubbo.protocol.{ DubboProtocol, DubboProtocolBuilder, DubboProtocolBuilderBase }

import scala.language.implicitConversions


import java.util.{List => JList}

import io.gatling.core.session.Session

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

trait DubboDsl extends DubboCheckSupport {

  //the toplevel DSL builder object (Dubbo)
  val Dubbo = DubboProtocolBuilderBase

  //(the toplevel) DSL builder method dubbo for the action
  def dubbo(interface: String, method: String) = DubboProcessBuilder(interface, method)

  implicit def dubboProtocolBuilder2DubboProtocol(builder: DubboProtocolBuilder): DubboProtocol = builder.build
  implicit def dubboProcessBuilder2ActionBuilder(builder: DubboProcessBuilder): ActionBuilder = builder.build()

  def transformJsonDubboData(argTypeName: String, argValueName: String, session: Session): Session = {
    session.set(argTypeName, toArray(session(argTypeName).as[JList[String]]))
      .set(argValueName, toArray(session(argValueName).as[JList[Any]]))
  }

  private def toArray[T:ClassTag](value: JList[T]): Array[T] = {
    value.asScala.toArray
  }
}
