import io.gatling.core.Predef._
import io.gatling.dubbo.Predef._

import scala.concurrent.duration._

class DubboTest extends Simulation {

  val dubboConfig = Dubbo
    .protocol("dubbo")
    .generic("true")
    .url("dubbo://IP地址:端口")
    .registryProtocol("")
    .registryAddress("")

  val jsonFileFeeder = jsonFile("data.json").circular
  val dubboScenario = scenario("load test dubbo")
    .forever("repeated") {
      feed(jsonFileFeeder)
        .exec(session => transformJsonDubboData("args_types1", "args_values1", session))
        .exec(dubbo("com.xxx.xxxService", "methodName")
          .argTypes("${args_types1}")
          .argValues("${args_values1}")
          .check(jsonPath("$.code").is("200"))
        )
    }

  setUp(
    dubboScenario.inject(atOnceUsers(10))
      .throttle(
        reachRps(10) in (1 seconds),
        holdFor(30 seconds))
  ).protocols(dubboConfig)

}