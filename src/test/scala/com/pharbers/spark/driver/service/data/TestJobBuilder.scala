package com.pharbers.spark.driver.service.data

import com.pharbers.spark.driver.service.model.{JobVO, OperatorVO, PluginVO}
import org.scalatest.FunSuite

import scala.collection.JavaConverters._

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/18 13:33
  * @note 一些值得注意的地方
  */
class TestJobBuilder extends FunSuite{

    test("job build"){
        val jobVO = new JobVO
        jobVO.name = "test"
        jobVO.`type` = "job"
        jobVO.args = Map("cpa" -> "cpaPath").asJava
        val operator = new OperatorVO
        operator.name = "operator"
        operator.needPlugin = true
        operator.classPath = "com"
        operator.args = Map("cpaPath" -> "*cpa").asJava
        val plugin = new PluginVO
        plugin.name = "plugin"
        plugin.args = Map("test" -> "test").asJava
        operator.plugin = plugin
        jobVO.operators = Map("test_operator" -> operator).asJava
        val job = JobBuilder().buildJob(jobVO)
        assert(job.name == "test")
        assert(job.actions.get(0).name == "test_operator")
        assert(job.actions.get(0).opers.get(0).args.get("cpaPath") == "cpaPath")
        assert(job.actions.get(0).opers.get(0).plugin.args.get("test") == "test")
    }
}
