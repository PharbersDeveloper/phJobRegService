package com.pharbers.spark.driver.service.model

import com.pharbers.ipaas.data.driver.api.Annotation.Plugin
import scala.collection.JavaConverters._
import scala.beans.BeanProperty

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/17 11:21
  * @note 一些值得注意的地方
  */
class PluginVO {
    @BeanProperty
    var args: java.util.Map[String, String] = new java.util.HashMap()

    @BeanProperty
    var name: String = ""

    @BeanProperty
    var msg: String = ""

    @BeanProperty
    var classPath: String = ""

    def this(plugin: Plugin, classPath: String){
        this()
        this.args = plugin.args().map(x => (x, "")).toMap.asJava
        this.name = plugin.name() match {
            case "" => classPath.split("\\.").last
            case s => s
        }
        this.msg = plugin.msg()
        this.classPath = classPath
    }
}
