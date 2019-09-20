package com.pharbers.spark.driver.service.data

import com.pharbers.ipaas.data.driver.api.Annotation.{Operator, Plugin}
import com.pharbers.ipaas.data.driver.api.model.Job
import com.pharbers.spark.driver.service.model.{JobVO, OperatorVO, PluginVO}

import scala.collection.JavaConverters._
import scala.collection.mutable

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/18 10:41
  * @note 一些值得注意的地方
  */
object JobModelInterpreter {
    def interpretModel(jobVO: JobVO): Unit = {

    }

    def interpretIpassJob(ipassJob: Job, operators: Array[OperatorVO]): JobVO = {
        val jobVO = new JobVO
        jobVO.name = ipassJob.name
        jobVO.`type` = "job"
        jobVO.args.putAll(ipassJob.args)
        jobVO.operators.putAll(ipassJob.actions.asScala.flatMap(x => {
            val opers = x.opers.asScala
            val localSource = mutable.Map[String, String]()
            opers.zipWithIndex.map { case (oper, index) =>
                val operatorAnnotation = Class.forName(oper.reference).getAnnotation(classOf[Operator])
                val operatorVO = new OperatorVO()
                operatorVO.name = operatorAnnotation.name()
                operatorVO.msg = operatorAnnotation.msg()
                operatorVO.classPath = oper.reference
                operatorAnnotation.args().foreach(x => operatorVO.args.put(x, oper.args.get(x)))
                operatorAnnotation.source().foreach(x => operatorVO.source.put(x, localSource.getOrElse(oper.args.get(x), oper.args.get(x))))
                if (oper.plugin != null) {
                    operatorVO.needPlugin = operatorAnnotation.needPlugin()
                    val pluginAnnotation = Class.forName(oper.plugin.reference).getAnnotation(classOf[Plugin])
                    val pluginVO = new PluginVO()
                    pluginVO.name = pluginAnnotation.name()
                    pluginVO.msg = pluginAnnotation.msg()
                    pluginVO.classPath = oper.plugin.reference
                    pluginAnnotation.args().foreach(x => pluginVO.args.put(x, oper.plugin.args.get(x)))
                    operatorVO.plugin = pluginVO
                }
                if (index == opers.length - 1) {
                    (x.name, operatorVO)
                } else {
                    val operatorAsSourceName = s"${x.name}_$index"
                    localSource.put(oper.name, operatorAsSourceName)
                    (operatorAsSourceName, operatorVO)
                }
            }
        }).toMap.asJava)
        jobVO
    }
}
