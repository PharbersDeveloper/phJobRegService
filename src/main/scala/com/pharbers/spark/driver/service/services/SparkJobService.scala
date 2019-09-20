package com.pharbers.spark.driver.service.services

import com.pharbers.ipaas.data.driver.api.model.Job
import com.pharbers.spark.driver.service.model.{JobVO, OperatorVO, PluginVO}
import com.pharbers.util.log.PhLogable
import org.springframework.stereotype.Service

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 14:35
  * @note 一些值得注意的地方
  */
@Service
trait SparkJobService extends PhLogable{
//    def runJob(jobVO: JobVO): JobVO

    def buildJob(jobVO: JobVO): Job

    def findOperators(packages: Seq[String]): Array[OperatorVO]

    def findPlugins(packages: Seq[String]): Array[PluginVO]

//    def findJobModel()
}
