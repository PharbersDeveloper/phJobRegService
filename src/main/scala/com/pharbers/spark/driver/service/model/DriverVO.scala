package com.pharbers.spark.driver.service.model

import java.util.UUID

import scala.beans.BeanProperty

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 13:36
  * @note 一些值得注意的地方
  */
case class DriverVO() {
    def this(id: String, config: DriverConfig, status: String){
        this()
        this.id = id
        this.config = config
        this.status = status
    }
    @BeanProperty
    var id: String = UUID.randomUUID().toString.replaceAll("-", "")
    @BeanProperty
    var config: DriverConfig = _
    @BeanProperty
    var status: String = ""
}
