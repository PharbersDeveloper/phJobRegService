package com.pharbers.spark.driver.service.data

import com.pharbers.ipaas.data.driver.api.model
import com.pharbers.ipaas.data.driver.api.model.{Action, Job}
import com.pharbers.spark.driver.service.config.Config
import com.pharbers.spark.driver.service.model.{JobVO, OperatorVO, PluginVO}
import com.pharbers.util.log.PhLogable

import scala.collection.JavaConverters._
import scala.collection.mutable

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/17 14:03
  * @note 一些值得注意的地方
  */
case class JobBuilder() extends PhLogable{
    def buildJob(jobVO: JobVO): Job ={
        val job = new Job
        job.name = jobVO.name
        job.args = jobVO.args
        job.factory = Config.JOB_FACTORY
        job.reference = Config.JOB_CLASS_PATH
        job.actions = topological(jobVO.operators.asScala).map(x => {
            val action = new Action
            action.name = x._1
            action.opers = List(buildOperator(x._2, x._1 + "_operator", jobVO.args.asScala.toMap)).asJava
            action.factory = Config.ACTION_FACTORY
            action.reference = Config.ACTION_CLASS_PATH
            action
        }).asJava
        job
    }

    def topological(dag: mutable.Map[String, OperatorVO]): List[(String, OperatorVO)] ={
        val topoMap = dag.map(x => (x._1, (x._2, 0)))
        var res: List[(String, OperatorVO)] = Nil
        topoMap.foreach(x => x._2._1.source.values().asScala.foreach(x => {
            val value = topoMap.getOrElse(x, throw new Exception(s"$x source not found"))
            topoMap.put(x, (value._1, value._2 + 1))
        }))
        while (topoMap.nonEmpty){
            val value = topoMap.find(x => x._2._2 == 0).getOrElse(throw new Exception("have ring"))
            topoMap.remove(value._1)
            value._2._1.source.values().asScala.foreach(x => {
                val value = topoMap.getOrElse(x, throw new Exception(s"$x source not found"))
                topoMap.put(x, (value._1, value._2 + -1))
            })
            res = (value._1, value._2._1) +: res
        }
        res
    }

    private def buildPlugin(pluginVO: PluginVO, name: String, jobArgs: Map[String, String]): model.Plugin ={
        val plugin = new model.Plugin
        plugin.name = name
        plugin.args = try {
            pluginVO.args.asScala.map(x => setArgs(x, jobArgs)).asJava
        } catch {
            case e: Exception =>
                logger.error(name, e)
                throw e
        }
        //todo: 配置
        plugin.factory = "com.pharbers.ipaas.data.driver.api.factory.PhPluginFactory"
        plugin.reference = pluginVO.classPath
        plugin.args = pluginVO.args
        plugin
    }

    private def buildOperator(operatorVO: OperatorVO, name: String, jobArgs: Map[String, String]): model.Operator ={
        val operator = new model.Operator
        operator.name = name
        operator.args = try {
            operatorVO.args.asScala.map(x => setArgs(x, jobArgs)).asJava
        } catch {
            case e: Exception =>
                logger.error(name, e)
                throw e
        }
        operator.args.putAll(operatorVO.source)
        //todo: 配置
        operator.factory = "com.pharbers.ipaas.data.driver.api.factory.PhOperatorFactory"
        operator.reference = operatorVO.classPath
        operator.plugin = if (operatorVO.needPlugin) {
            buildPlugin(operatorVO.plugin, name + "_plugin", jobArgs)
        } else {
            null
        }
        operator
    }

    private def setArgs(arg: (String, String), jobArgs: Map[String, String]): (String, String) ={
        if (arg._2 != null && arg._2.startsWith("*")){
            (arg._1, jobArgs.getOrElse(arg._2.replace("*", ""), throw new Exception(s"no find ${arg._2} in job args")))
        } else {
            arg
        }
    }
}
