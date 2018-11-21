package io.gatling.dubbo.protocol

import akka.actor.ActorSystem
import io.gatling.core.CoreComponents
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.protocol.{ Protocol, ProtocolKey }

object DubboProtocol {

  val DubboProtocolKey = new ProtocolKey {

    type Protocol = DubboProtocol
    type Components = DubboComponents

    def protocolClass: Class[io.gatling.core.protocol.Protocol] = classOf[DubboProtocol].asInstanceOf[Class[io.gatling.core.protocol.Protocol]]

    def defaultProtocolValue(configuration: GatlingConfiguration): DubboProtocol = throw new IllegalStateException("Can't provide a default value for DubboProtocol")

    def newComponents(system: ActorSystem, coreComponents: CoreComponents): DubboProtocol => DubboComponents = {
      dubboProtocol => DubboComponents(dubboProtocol)
    }
  }
}

case class DubboProtocol(
    protocol: String, //dubbo
    generic:  String, //泛化调用?
    url:      String, //use url or

    //use registry
    registryProtocol: String,
    registryAddress:  String
) extends Protocol {
  type Components = DubboComponents
}
