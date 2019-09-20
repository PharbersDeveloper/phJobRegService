package com.pharbers.spark.driver.service.model

import java.util

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/17 13:33
  * @note 一些值得注意的地方
  */
class JobVO {
    @BeanProperty
    var name: String = ""

    @BeanProperty
    var `type`: String = "job"

    @BeanProperty
    var args: java.util.Map[String, String] = new util.HashMap()

    @BeanProperty
    var operators: java.util.Map[String, OperatorVO] = new util.HashMap()
}
