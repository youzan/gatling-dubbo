package io.gatling.dubbo.protocol

import io.gatling.core.protocol.ProtocolComponents
import io.gatling.core.session.Session

case class DubboComponents(dubboProtocol: DubboProtocol) extends ProtocolComponents {

  def onStart: Option[Session => Session] = None
  def onExit: Option[Session => Unit] = None
}
