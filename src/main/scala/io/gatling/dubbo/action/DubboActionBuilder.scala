package io.gatling.dubbo.action

import com.alibaba.dubbo.config.utils.ReferenceConfigCache
import com.alibaba.dubbo.config.{ ApplicationConfig, ReferenceConfig, RegistryConfig }
import com.alibaba.dubbo.rpc.service.GenericService
import com.fasterxml.jackson.databind.ObjectMapper
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.protocol.ProtocolComponentsRegistry
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioContext
import io.gatling.dubbo.DubboCheck
import io.gatling.dubbo.protocol.{ DubboComponents, DubboProtocol }

case class DubboActionBuilder(interface: String, method: String, argTypes: Expression[Array[String]], argValues: Expression[Array[Object]], checks: List[DubboCheck]) extends ActionBuilder {

  private def components(protocolComponentsRegistry: ProtocolComponentsRegistry): DubboComponents =
    protocolComponentsRegistry.components(DubboProtocol.DubboProtocolKey)

  override def build(ctx: ScenarioContext, next: Action): Action = {
    import ctx._
    val protocol = components(protocolComponentsRegistry).dubboProtocol

    //Dubbo客户端配置
    val reference = new ReferenceConfig[GenericService]
    val application = new ApplicationConfig
    application.setName("gatling-dubbo")
    reference.setApplication(application)
    reference.setProtocol(protocol.protocol)
    reference.setGeneric(protocol.generic)
    if (protocol.url == "") { //直连 or by etcd3
      val registry = new RegistryConfig
      registry.setProtocol(protocol.registryProtocol)
      registry.setAddress(protocol.registryAddress)
      reference.setRegistry(registry)
    } else {
      reference.setUrl(protocol.url)
    }
    reference.setInterface(interface)
    //    reference.setConnections(10)

    val cache = ReferenceConfigCache.getCache
    val genericService = cache.get(reference) //已缓存就直接返回, 未缓存的话就初始化并放入缓存, 下次就可以直接返回

    val objectMapper: ObjectMapper = new ObjectMapper()
    new DubboAction(interface, method, argTypes, argValues, genericService, checks, coreComponents, throttled, objectMapper, next)
  }

}
