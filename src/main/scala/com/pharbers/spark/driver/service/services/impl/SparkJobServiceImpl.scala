package com.pharbers.spark.driver.service.services.impl

import java.util
import java.util.concurrent.TimeUnit

import com.pharbers.kafka.producer.PharbersKafkaProducer
import com.pharbers.kafka.schema.SparkJob
import com.pharbers.spark.driver.service.config.Config
import com.pharbers.spark.driver.service.model.JobVO
import com.pharbers.spark.driver.service.services.SparkJobService
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.stereotype.Service

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/08/01 15:00
  * @note 一些值得注意的地方
  */
@Service
class SparkJobServiceImpl extends SparkJobService{
    override def runJob(jobVO: JobVO): JobVO = {
        //todo： 验证参数， 确认driver存在等
        val pkp = new PharbersKafkaProducer[String, SparkJob]
        val value = new SparkJob(jobVO.id, jobVO.config.bucketName, jobVO.config.ossKey, jobVO.config.mode, jobVO.config.config.asInstanceOf[util.Map[CharSequence, CharSequence]])
        val res = pkp.produce(jobVO.config.topic, jobVO.id, value).get(10, TimeUnit.SECONDS)
        pkp.producer.close()
        res match {
            case _: RecordMetadata => new JobVO(jobVO.id, jobVO.config, Config.DriverStatus.CREATING)
            case _ => throw new Exception("发送kafka失败")
        }
    }
}
