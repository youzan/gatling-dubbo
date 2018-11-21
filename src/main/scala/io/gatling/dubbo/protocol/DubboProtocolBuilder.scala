package io.gatling.dubbo.protocol

object DubboProtocolBuilderBase {
  def protocol(protocol: String) = DubboProtocolBuilderGenericStep(protocol)
}

case class DubboProtocolBuilderGenericStep(protocol: String) {
  def generic(generic: String) = DubboProtocolBuilderUrlStep(protocol, generic)
}

case class DubboProtocolBuilderUrlStep(protocol: String, generic: String) {
  def url(url: String) = DubboProtocolBuilderRegistryProtocolStep(protocol, generic, url)
}

case class DubboProtocolBuilderRegistryProtocolStep(protocol: String, generic: String, url: String) {
  def registryProtocol(registryProtocol: String) = DubboProtocolBuilderRegistryAddressStep(protocol, generic, url, registryProtocol)
}

case class DubboProtocolBuilderRegistryAddressStep(protocol: String, generic: String, url: String, registryProtocol: String) {
  def registryAddress(registryAddress: String) = DubboProtocolBuilder(protocol, generic, url, registryProtocol, registryAddress)
}

case class DubboProtocolBuilder(protocol: String, generic: String, url: String, registryProtocol: String, registryAddress: String) {

  def build = DubboProtocol(
    protocol = protocol,
    generic = generic,
    url = url,
    registryProtocol = registryProtocol,
    registryAddress = registryAddress
  )
}
