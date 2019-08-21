package com.pharbers.spark.driver.service.model

import scala.beans.BeanProperty

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/07/31 18:44
  * @note 一些值得注意的地方
  */
case class DriverConfig() {
    @BeanProperty
    var name: String = "test-driver"
    @BeanProperty
    var cup = 1
    @BeanProperty
    var eme = 1024
    //todo: 这儿似乎不应该由传入指定
    @BeanProperty
    var topic = ""
}
