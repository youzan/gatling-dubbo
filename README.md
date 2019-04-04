# Gatling-Dubbo 2.0
`【招聘】Java开发、测试开发等岗位，有意者请将简历投递至<sunjun【@】youzan.com>`  

Gatling的非官方Dubbo压测插件，基于Gatling 2.3.1，插件已在Dubbo 2.6.5上测试，但理论上所有Dubbo版本都适用，`2.0插件采用普通API调用方式执行压测请求，如果你想使用泛化调用方式执行压测请求，请参考`[1.0插件](https://github.com/youzan/gatling-dubbo/tree/v1.0)`，推荐使用2.0插件，即采用普通API调用方式，因为 dubbo 官方推荐生产上使用该方式，所以以同样的方式压测得到的结果，更具有参考意义，且2.0插件无需 dubbo 框架做任何改造。`

## 使用方法

### 打包Jar

> 按需修改配置:  
- 默认为4核8G内存机器配置了200线程池，与dubbo线程池一致，你也可以根据自己的机器配置使用API调整线程池大小，下述
- 如果你使用其他Dubbo版本，请修改根目录下build.sbt中的libraryDependencies配置

> 如果你不需要修改配置，可以略过以下打包步骤，直接[下载Jar包（该  Jar 包无应用依赖）](https://github.com/youzan/gatling-dubbo/releases)

项目依赖sbt，请安装sbt 1.2.1，详见[官方文档](https://www.scala-sbt.org/1.x/docs/Setup.html)，执行
```bash
$ git clone https://github.com/youzan/gatling-dubbo.git
$ cd gatling-dubbo
$ sbt assembly
```

### 将上述jar包拷贝到/your-path-to/gatling-charts-highcharts-bundle-2.3.1/lib目录
```bash
$ cp /your-path-to/gatling-dubbo/target/scala-2.12/gatling-dubbo-assembly-1.0.jar /your-path-to/gatling-charts-highcharts-bundle-2.3.1/lib
```

### 依赖
由于插件采用普通API调用方式执行压测请求，所以需要引入服务特定的 API 包，为了演示方便，这里直接将依赖写在了项目根目录下的 build.sbt 中：
```sbtshell
name := "gatling-dubbo"

version := "1.0"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "io.gatling" % "gatling-core" % "2.3.1" % "provided",
  "com.alibaba" % "dubbo" % "2.6.5",
  "com.youzan.xxx" % "xxx-api" % "1.xxx-RELEASE" exclude("ch.qos.logback", "logback-classic") //应用 API 包
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
```

### 创建Simulation和相应的json数据文件
参考gatling-dubbo/src/test/scala目录下的DubboTest.scala和data.json

`DubboTest.scala`具体说明请参考示例
> 示例中展示的是单接口的例子，如果你想探测应用单机或集群真实水位，可以结合使用 randomSwitch 混合多接口且按生产环境真实的接口比例同时进行压测。

`data.json`
```json
[
  {
    "kdtId": 160,
    "page": 1
  },
  {
    "kdtId": 160,
    "page": 2
  },
  {
    "kdtId": 160,
    "page": 3
  }
]
```
数据采用 json 数组保存，其中每一个 json 对象都是一次压测请求需要的完整参数，且为了方便通过 session 设置动态参数，数据结构采用一维结构，风格同 Jmeter。

### 执行Dubbo压测
```bash
$ bin/gatling.sh
GATLING_HOME is set to /your-path-to/gatling-charts-highcharts-bundle-2.3.1
Choose a simulation number:
     [0] dubbo.DubboTest
0
Select simulation id (default is 'dubbotest'). Accepted characters are a-z, A-Z, 0-9, - and _

Select run description (optional)

Simulation dubbo.DubboTest started...

================================================================================
2019-04-04 10:12:44                                           5s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=56     KO=0     )
> com.youzan.xxx.XxxService                                (OK=56     KO=0     )

---- scenario of xxx -----------------------------------------------------------
[--------------------------------------------------------------------------]  0%
          waiting: 0      / active: 10     / done:0
================================================================================

...

================================================================================
2019-04-04 10:13:11                                          31s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=320    KO=0     )
> com.youzan.xxx.XxxService                                (OK=320    KO=0     )

---- scenario of xxx -----------------------------------------------------------
[--------------------------------------------------------------------------]  0%
          waiting: 0      / active: 10     / done:0
================================================================================

Simulation dubbo.DubboTest completed in 31 seconds
Parsing log file(s)...
Parsing log file(s) done
Generating reports...

================================================================================
---- Global Information --------------------------------------------------------
> request count                                        320 (OK=320    KO=0     )
> min response time                                     44 (OK=44     KO=-     )
> max response time                                    343 (OK=343    KO=-     )
> mean response time                                    62 (OK=62     KO=-     )
> std deviation                                         42 (OK=42     KO=-     )
> response time 50th percentile                         49 (OK=49     KO=-     )
> response time 75th percentile                         53 (OK=53     KO=-     )
> response time 95th percentile                        160 (OK=160    KO=-     )
> response time 99th percentile                        275 (OK=275    KO=-     )
> mean requests/sec                                 10.323 (OK=10.323 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                           320 (100%)
> 800 ms < t < 1200 ms                                   0 (  0%)
> t > 1200 ms                                            0 (  0%)
> failed                                                 0 (  0%)
================================================================================

Reports generated in 0s.
Please open the following file: /your-path-to/gatling-charts-highcharts-bundle-2.3.1/results/dubbotest-1554343959840/index.html
```

打开上述报告查看更多压测信息：
![Reports](Reports.png)


### License
Apache License, Version 2.0
