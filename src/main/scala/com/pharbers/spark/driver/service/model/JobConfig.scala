package com.pharbers.spark.driver.service.model

import scala.beans.BeanProperty

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 14:38
  * @note 一些值得注意的地方
  */
class JobConfig {
    @BeanProperty
    var name = ""
    @BeanProperty
    var topic: String = ""
    @BeanProperty
    var bucketName = ""
    @BeanProperty
    var ossKey = ""
    @BeanProperty
    var mode = ""
    @BeanProperty
    var config: java.util.Map[String, String] = _
}
