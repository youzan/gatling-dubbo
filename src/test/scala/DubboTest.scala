//package dubbo
//
//import com.youzan.xxx.XxxService  //引入应用 API
//
//import com.alibaba.dubbo.config.{ApplicationConfig, ReferenceConfig}
//import io.gatling.core.Predef._
//import io.gatling.core.session.Session
//import io.gatling.dubbo.Predef._
//
//import scala.concurrent.duration._
//
//class DubboTest extends Simulation {
//
//  /**
//    * dubbo 服务初始化
//    */
//  val application = new ApplicationConfig()
//  application.setName("gatling-dubbo")
//
//  // 引用远程服务
//  val reference = new ReferenceConfig[XxxService] // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
//  reference.setApplication(application)
//  reference.setUrl("dubbo://ip:port/com.youzan.xxx.XxxService")  //设置服务地址、端口、全限定服务类名
//  reference.setInterface(classOf[XxxService])
//  //  reference.setVersion("1.0.0")    //设置版本号，可以不设置
//  //  reference.setTimeout(3000)        //设置超时时间，可以不设置
//
//  val xxxService = reference.get()
//
//  /**
//    * gatling 压测逻辑
//    * dubbo 压测插件的 api 只有一个，即 dubbo("com.youzan.xxx.XxxService", f)
//    * 第一个参数可以是任意字符串，不过为了方便统计接口级的性能基线，建议设置为全限定接口名
//    * 第二个参数的作用下面会讲述，名字也可以自行设置
//    */
//  val jsonFileFeeder = jsonFile("data.json").circular
//  val dubboScenario = scenario("scenario of xxx")
//    .forever("tripsCount") {
//      feed(jsonFileFeeder)
//        .exec(
//          dubbo("com.youzan.xxx.XxxService", f)
//            .check(jsonPath("$.success").is("true"))      //基于 jsonpath 校验返回结果
//            .check(jsonPath("$.data.totalCount").is("9")) //更多校验
//          //            .threadPoolSize(200)    //4C8G施压机默认使用200线程池，你也可以根据施压机资源情况自行设置，一般不需要设置
//        )
//    }
//
//    setUp(
//      dubboScenario.inject(atOnceUsers(10))
//        .throttle(
//          reachRps(10) in (1 seconds),
//          holdFor(30 seconds))
//    )
//
//  //接口调用逻辑，包括入参构造和设置. 注意参数类型需一致，不一致就做相应的转化
//  def f(session: Session): Object = {
//    //如果方法的入参是复杂对象, 必须在这里 new 对象和 set 字段
//    xxxService.getXxxList(session.attributes("kdtId").asInstanceOf[Integer].toLong, session.attributes("page").asInstanceOf[Integer])
//  }
//
//}
//
