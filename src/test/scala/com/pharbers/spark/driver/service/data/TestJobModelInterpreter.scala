package com.pharbers.spark.driver.service.data

import java.io.{File, FileInputStream}

import com.pharbers.ipaas.data.driver.api.model.Job
import com.pharbers.ipaas.data.driver.libs.input.JsonInput
import com.pharbers.spark.driver.service.config.Config
import com.pharbers.spark.driver.service.services.impl.SparkJobServiceImpl
import org.scalatest.FunSuite

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/20 14:26
  * @note 一些值得注意的地方
  */
class TestJobModelInterpreter extends FunSuite{
    test("test checkIpassJob"){
        val ipassJob = JsonInput().readObject[Job](new FileInputStream(new File("src/test/resources/pfizer_max_all.json")))
        val res = JobModelInterpreter.checkIpassJob(ipassJob, new SparkJobServiceImpl().findOperators(Config.OPERATOR_PACKAGES))
        assert(res)
    }
}
