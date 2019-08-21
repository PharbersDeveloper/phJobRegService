package com.pharbers.spark.driver.service.services

import com.pharbers.spark.driver.service.model.{DriverConfig, DriverVO}
import org.springframework.stereotype.Service

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 13:05
  * @note 一些值得注意的地方
  */
@Service
trait SparkDriverService {
    def createSparkDriver(driverVO: DriverVO): DriverVO
}
