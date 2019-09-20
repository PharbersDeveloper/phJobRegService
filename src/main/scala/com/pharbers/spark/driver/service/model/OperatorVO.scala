package com.pharbers.spark.driver.service.model

import com.pharbers.ipaas.data.driver.api.Annotation.Operator
import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/17 10:51
  * @note 一些值得注意的地方
  */
class OperatorVO {
    @BeanProperty
    var needPlugin: Boolean = false

    @BeanProperty
    var args: java.util.Map[String, String] = new java.util.HashMap()

    @BeanProperty
    var source: java.util.Map[String, String] = new java.util.HashMap()

    @BeanProperty
    var name: String = ""

    @BeanProperty
    var msg: String = ""

    @BeanProperty
    var classPath: String = ""

    @BeanProperty
    var plugin: PluginVO = _

    def this(operator: Operator, classPath: String){
        this()
        this.needPlugin = operator.needPlugin()
        this.args = operator.args().map(x => (x, "")).toMap.asJava
        this.source = operator.source().map(x => (x, "")).toMap.asJava
        this.name = operator.name() match {
            case "" => classPath.split("\\.").last
            case s => s
        }
        this.msg = operator.msg()
        this.classPath = classPath
    }
}
