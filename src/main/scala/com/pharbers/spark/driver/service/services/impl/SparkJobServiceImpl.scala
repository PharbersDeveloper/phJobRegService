package com.pharbers.spark.driver.service.services.impl

import com.pharbers.ipaas.data.driver.api.model.Job
import com.pharbers.ipaas.data.driver.operators.util.OperatorInterpreter
import com.pharbers.spark.driver.service.data.JobBuilder
import com.pharbers.spark.driver.service.model.{JobVO, OperatorVO, PluginVO}
import com.pharbers.spark.driver.service.services.SparkJobService
import org.springframework.stereotype.Service

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 15:00
  * @note 一些值得注意的地方
  */
@Service
class SparkJobServiceImpl extends SparkJobService{

    override def findOperators(packages: Seq[String]): Array[OperatorVO] = {
        packages.flatMap(x => {
            val classAnAnnotations = OperatorInterpreter.getOperatorAnnotations(x)
            classAnAnnotations.map(x => new OperatorVO(x._2, x._1))
        }).toArray
    }

    override def findPlugins(packages: Seq[String]): Array[PluginVO] = {
        packages.flatMap(x => {
            val classAnAnnotations = OperatorInterpreter.getPluginAnnotations(x)
            classAnAnnotations.map(x => new PluginVO(x._2, x._1))
        }).toArray
    }

    override def buildJob(jobVO: JobVO): Job = {
        jobVO.`type` match {
            case "job" => JobBuilder().buildJob(jobVO)
            case "model" => ???
            case _ => null
        }
    }
}
