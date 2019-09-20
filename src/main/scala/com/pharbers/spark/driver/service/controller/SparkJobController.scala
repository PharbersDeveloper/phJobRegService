package com.pharbers.spark.driver.service.controller

import com.pharbers.ipaas.data.driver.api.model.Job
import com.pharbers.spark.driver.service.config.Config
import com.pharbers.spark.driver.service.model.{JobVO, OperatorVO, PluginVO}
import com.pharbers.spark.driver.service.services.SparkJobService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils
import java.io.IOException

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

    @GetMapping(value = Array("/"))
    def getHome: Array[Byte] = {
        val resource = new ClassPathResource("test.html")
        val inputStream = resource.getInputStream
        try
            FileCopyUtils.copyToByteArray(inputStream)
        finally
            if (inputStream != null) inputStream.close()
    }

    @GetMapping(Array("/operators"))
    def findOperators(): Array[OperatorVO] = {
        sparkJobService.findOperators(Config.OPERATOR_PACKAGES)
    }

    @GetMapping(Array("/plugins"))
    def findPlugins(): Array[PluginVO] = {
        sparkJobService.findPlugins(Config.PLUGIN_PACKAGES)
    }

    @PostMapping(Array("/build"))
    def buildJob(@RequestBody jobVO: JobVO): Job = {
        sparkJobService.buildJob(jobVO)
    }
}
