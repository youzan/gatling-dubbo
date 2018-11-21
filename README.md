# Gatling-Dubbo
`【招聘】Java开发、测试开发等岗位，有意者请将简历投递至<canyuns【@】163.com>`  

Gatling的非官方Dubbo压测插件，基于Gatling 2.3.1，插件已在Dubbo 2.6.5上测试，但理论上所有支持泛化调用的Dubbo版本都适用

## 使用方法

### clone代码
```bash
$ git clone https://github.com/youzan/gatling-dubbo.git
$ cd gatling-dubbo
```

### 打包Jar
需先安装sbt 1.2.1，详见[官方文档](https://www.scala-sbt.org/1.x/docs/Setup.html)，执行
```bash
$ sbt assembly
```

### 将上述jar包拷贝到/your-path-to/gatling-charts-highcharts-bundle-2.3.1/lib目录
```bash
$ cp /your-path-to/gatling-dubbo/target/scala-2.12/gatling-dubbo-assembly-1.0.jar /your-path-to/gatling-charts-highcharts-bundle-2.3.1/lib
```


### 创建Simulation和相应的json数据文件
参考gatling-dubbo/src/test/scala目录下的DubboTest.scala和data.json，并分别修改Simulation中的IP、端口、Service、Method、Check逻辑、setUp逻辑和json数据文件中的相应参数类型、参数值


### 执行Dubbo压测
```bash
$ bin/gatling.sh
GATLING_HOME is set to /your-path-to/gatling-charts-highcharts-bundle-2.3.1
Choose a simulation number:
     [0] DubboTest
0
Select simulation id (default is 'dubbotest'). Accepted characters are a-z, A-Z, 0-9, - and _

Select run description (optional)

Simulation DubboTest started...

================================================================================
2018-11-21 17:01:54                                           5s elapsed
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=51     KO=0     )
> com.xxx.xxxService.methodName                            (OK=51     KO=0     )

---- load test dubbo -----------------------------------------------------------
[--------------------------------------------------------------------------]  0%
          waiting: 0      / active: 10     / done:0
================================================================================

...

Simulation DubboTest completed in 31 seconds
Parsing log file(s)...
Parsing log file(s) done
Generating reports...

================================================================================
---- Global Information --------------------------------------------------------
> request count                                        320 (OK=320    KO=0     )
> min response time                                     33 (OK=33     KO=-     )
> max response time                                    460 (OK=460    KO=-     )
> mean response time                                    49 (OK=49     KO=-     )
> std deviation                                         53 (OK=53     KO=-     )
> response time 50th percentile                         37 (OK=37     KO=-     )
> response time 75th percentile                         39 (OK=39     KO=-     )
> response time 95th percentile                         78 (OK=78     KO=-     )
> response time 99th percentile                        385 (OK=385    KO=-     )
> mean requests/sec                                 10.323 (OK=10.323 KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                           320 (100%)
> 800 ms < t < 1200 ms                                   0 (  0%)
> t > 1200 ms                                            0 (  0%)
> failed                                                 0 (  0%)
================================================================================

Reports generated in 0s.
Please open the following file: /your-path-to/gatling-charts-highcharts-bundle-2.3.1/results/dubbotest-1542790909872/index.html
```

### License
Apache License, Version 2.0
