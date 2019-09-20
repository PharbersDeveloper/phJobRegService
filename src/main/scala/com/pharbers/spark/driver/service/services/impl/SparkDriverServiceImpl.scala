//package com.pharbers.spark.driver.service.services.impl
//
//import java.util.concurrent.TimeUnit
//
//import com.pharbers.kafka.producer.PharbersKafkaProducer
//import com.pharbers.kafka.schema.SparkDriverConfig
//import com.pharbers.spark.driver.service.config.Config
//import com.pharbers.spark.driver.service.model.{DriverConfig, DriverVO}
//import com.pharbers.spark.driver.service.services.SparkDriverService
//import org.apache.kafka.clients.producer.RecordMetadata
//import org.springframework.stereotype.Service
//
///** 功能描述
//  *
//  * @param args 构造参数
//  * @tparam T 构造泛型参数
//  * @author dcs
//  * @version 0.0
//  * @since 2019/08/01 13:53
//  * @note 一些值得注意的地方
//  */
//@Service
//class SparkDriverServiceImpl extends SparkDriverService{
//
//    override def createSparkDriver(driverVO: DriverVO): DriverVO = {
//        //todo： 验证是否存在， 是否能创建， 验证参数等
//        val pkp = new PharbersKafkaProducer[String, SparkDriverConfig]
//        val value = new SparkDriverConfig(driverVO.config.name, driverVO.config.cup, driverVO.config.eme, driverVO.config.topic)
//        val res = pkp.produce(Config.CREATE_SPARK_DRIVER_TOPIC, driverVO.id, value).get(10, TimeUnit.SECONDS)
//        pkp.producer.close()
//        res match {
//            case _: RecordMetadata => new DriverVO(driverVO.id, driverVO.config, Config.DriverStatus.CREATING)
//            case _ => throw new Exception("发送kafka失败")
//        }
//    }
//}
