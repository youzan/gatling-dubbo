//package dubbo
//
//import com.youzan.xxx.XxxService  //引入应用 API
//
//import com.alibaba.dubbo.config.utils.ReferenceConfigCache
//import com.alibaba.dubbo.config.utils.ReferenceConfigCache.KeyGenerator
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
//  //缓存逻辑
//  val keyGenerator = new KeyGenerator {
//    def generateKey(referenceConfig: ReferenceConfig[_]): String = {
//      referenceConfig.getUrl
//    }
//  }
//  val cache = ReferenceConfigCache.getCache("_DEFAULT_", keyGenerator)
//  val xxxService = cache.get(reference) //已缓存就直接返回, 未缓存的话就初始化并放入缓存, 下次就可以直接返回
//
//
//
//  /**
//    * gatling 压测逻辑
//    * dubbo 压测插件的 api 只有一个，即 dubbo("com.youzan.xxx.XxxService", f, param())
//    * 第一个参数可以是任意字符串，不过为了方便统计接口级的性能基线，建议设置为全限定接口名
//    * 第二、第三个参数的作用下面会讲述，名字也可以自行设置
//    */
//  val jsonFileFeeder = jsonFile("data.json").circular
//  val dubboScenario = scenario("scenario of xxx")
//    .forever("tripsCount") {
//      feed(jsonFileFeeder)
//        .exec(
//          dubbo("com.youzan.xxx.XxxService", f, param())
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
//
//
//  //为 f 提供参数（非动态参数，即不需要从外部json文件读入的参数，如设置DTO的部分固定字段），参数都放到 Array[Object] 里返回
//  def param(): Array[Object] = {
//    Array.empty[Object]
//  }
//
//  //接口调用逻辑，这里从param取出各个参数，并使用session中的动态参数设置完全这些参数；或者直接从session取参数设置，注意参数类型需一致，不一致就做相应的转化
//  def f(param: Array[Object], session: Session): Object = {
//    xxxService.getXxxList(session.attributes("kdtId").asInstanceOf[Integer].toLong, session.attributes("page").asInstanceOf[Integer])
//  }
//
//}
//
