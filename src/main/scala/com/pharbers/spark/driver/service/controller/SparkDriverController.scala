package com.pharbers.spark.driver.service.controller

import com.pharbers.spark.driver.service.config.Config
import com.pharbers.spark.driver.service.model.DriverVO
import com.pharbers.spark.driver.service.services.SparkDriverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 11:22
  * @note 一些值得注意的地方
  */
@RestController
@RequestMapping(Array("/spark/driver"))
class SparkDriverController {
    @Autowired
    var sparkDriverService: SparkDriverService = _

    @PostMapping(Array("/create"))
    def createSparkDriver(@RequestBody driverPost: DriverVO): DriverVO = {
        try {
            val response = sparkDriverService.createSparkDriver(driverPost)
            response
        } catch {
            case e: Exception => {
                driverPost.setStatus(Config.DriverStatus.ERROR)
                driverPost
            }
        }

    }

    //todo： 获取driver状态
    @GetMapping(Array("/"))
    def getSparkDriver(@RequestParam id: String): DriverVO = {
        ???
    }
}
