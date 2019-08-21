package com.pharbers.spark.driver.service.model

import scala.beans.BeanProperty

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 14:37
  * @note 一些值得注意的地方
  */
class JobVO {
    def this(id: String, config: JobConfig, status: String){
        this()
        this.id = id
        this.config = config
        this.status = status
    }
    @BeanProperty
    var id = ""
    @BeanProperty
    var config: JobConfig = _
    @BeanProperty
    var status: String = ""
}
