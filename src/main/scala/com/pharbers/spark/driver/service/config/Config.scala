package com.pharbers.spark.driver.service.config

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 13:57
  * @note 一些值得注意的地方
  */
object Config {
    //todo: 配置化，环境变量
    val CREATE_SPARK_DRIVER_TOPIC: String = scala.util.Properties.envOrElse("CREATE_SPARK_DRIVER_TOPIC", "DriverRequest")
    object DriverStatus extends Enumeration{
        type DriverStatus = String
        val RUNNING = "running"
        val ERROR = "error"
        val CREATING = "creating"
        val NON_EXISTENT = "does not exist"
    }

}
