package com.pharbers.spark.driver.service.controller

import com.pharbers.spark.driver.service.config.Config
import com.pharbers.spark.driver.service.model.JobVO
import com.pharbers.spark.driver.service.services.SparkJobService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 14:56
  * @note 一些值得注意的地方
  */
@RestController
@RequestMapping(Array("/spark/job"))
class SparkJobController {
    @Autowired
    var sparkJobService: SparkJobService = _

    @PostMapping(Array("/run"))
    def runJob(@RequestBody postJob: JobVO): JobVO ={
        try {
            val response = sparkJobService.runJob(postJob)
            response
        } catch {
            case e: Exception => {
                postJob.setStatus(Config.DriverStatus.ERROR)
                postJob
            }
        }
    }

    //todo： 获取job状态
    @GetMapping(Array("/"))
    def getSparkJob(@RequestParam id: String): JobVO = {
        ???
    }
}
