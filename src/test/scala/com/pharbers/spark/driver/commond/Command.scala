package com.pharbers.spark.driver.commond

import java.io.{File, FileInputStream, PrintWriter}
import java.util.Scanner

import com.pharbers.ipaas.data.driver.api.model.Job
import com.pharbers.ipaas.data.driver.libs.input.JsonInput
import com.pharbers.spark.driver.service.config.Config
import com.pharbers.spark.driver.service.data.{JobBuilder, JobModelInterpreter}
import com.pharbers.spark.driver.service.model.{JobVO, OperatorVO, PluginVO}
import com.pharbers.spark.driver.service.services.impl.SparkJobServiceImpl

import scala.collection.JavaConverters._
import scala.tools.nsc.interpreter.InputStream

/** 功能描述
  *
  * @param args 构造参数
  * @tparam T 构造泛型参数
  * @author dcs
  * @version 0.0
  * @since 2019/09/18 14:05
  * @note 一些值得注意的地方
  */
object Command extends App {
    var phase = 0
    var jobVO = new JobVO
    var end = false
    val scanner = new Scanner(System.in)
    val sparkJobService = new SparkJobServiceImpl
    println("begin")
    val operators = sparkJobService.findOperators(Config.OPERATOR_PACKAGES)
    val plugins = sparkJobService.findPlugins(Config.PLUGIN_PACKAGES)
    println("可以使用的算子:")
    println(operators.map(x => x.name).mkString("\n"))
    while (!end){
        interpretCmd()
    }
    println("end")

    def interpretCmd(): Unit ={
        println("输入指令")
        print(">")
        val cmd = scanner.nextLine()
        cmd match {
            case "new job" if phase == 0 => newJob("job")
            case "replay" => replay()
            case "set action" if phase > 0 => setAction()
            case "job json" if phase == 1 => buildJob()
            case "show dag" if jobVO.operators.size() > 0 => showDag()
            case "exit" => end = true
            case "save job" if phase == 1 => saveJob(jobVO.name)
            case "load job" => loadJob()
            case "load ipass job" => loadIpassJob()
            case "set job args" if phase == 1 => setJobArgs()
            case _ =>
        }
    }

    def newJob(jobType: String): Unit ={
        println("job name")
        print(">")
        jobVO.name = scanner.nextLine()
        jobVO.`type` = jobType
        phase = phase + 1
    }

    def setAction(): Unit ={
        println("action name")
        print(">")
        val actionName = scanner.nextLine()
        if (jobVO.operators.containsKey(actionName)){
            println("action is exists, reset?(y/n)")
            print(">")
            scanner.nextLine() match {
                case "y" => println(s"reset action $actionName")
                case _ => return
            }
        }
        val operatorVO = addOperator(actionName)
        jobVO.operators.put(actionName, operatorVO)
    }

    def addOperator(actionName: String): OperatorVO ={
        val operatorVO = new OperatorVO
        println("choose operator")
        println(operators.map(x => x.name).mkString("\n"))
        print(">")
        def chooseOperator(name: String):OperatorVO = {
            operators.find(x => x.name == name) match {
                case Some(operator) => operator
                case _ =>
                    println("choose operator again")
                    println(operators.map(x => x.name).mkString("\n"))
                    print(">")
                    chooseOperator(scanner.nextLine())
            }
        }
        def choosePlugin(name: String):PluginVO = {
            plugins.find(x => x.name == name) match {
                case Some(plugin) => plugin
                case _ =>
                    println("choose plugin again")
                    println(plugins.map(x => x.name).mkString("\n"))
                    print(">")
                    choosePlugin(scanner.nextLine())
            }
        }
        val operatorMode = chooseOperator(scanner.nextLine())
        operatorVO.name = operatorMode.name
        operatorVO.source.putAll(operatorMode.source)
        operatorVO.args.putAll(operatorMode.args)
        operatorVO.classPath = operatorMode.classPath
        operatorVO.needPlugin = operatorMode.needPlugin
        operatorVO.source.asScala.foreach(x => {
            println(s"choose source ${x._1}")
            println((jobVO.operators.keySet().asScala - actionName).mkString("\n"))
            print(">")
            operatorVO.source.put(x._1, scanner.nextLine())
        })
        operatorVO.args.asScala.foreach(x => {
            println(s"set args ${x._1}")
            print(">")
            val inputValue = scanner.nextLine()
            if(inputValue.startsWith("*")) jobVO.args.put(inputValue.replace("*", ""), "")
            val argsValue = inputValue match {
                case "null" => null
                case _ => inputValue
            }
            operatorVO.args.put(x._1, argsValue)
        })
        if (operatorVO.needPlugin){
            println("choose plugin")
            println(plugins.map(x => x.name).mkString("\n"))
            print(">")
            val pluginMode = choosePlugin(scanner.nextLine())
            val pluginVO = new PluginVO()
            pluginVO.name = pluginMode.name
            pluginVO.args.putAll(pluginMode.args)
            pluginVO.classPath = pluginMode.classPath
            pluginVO.args.asScala.foreach(x => {
                println(s"set args ${x._1}")
                print(">")
                val inputValue = scanner.nextLine()
                if(inputValue.startsWith("*")) jobVO.args.put(inputValue.replace("*", ""), "")
                val argsValue = inputValue match {
                    case "null" => null
                    case _ => inputValue
                }
                pluginVO.args.put(x._1, argsValue)
            })
            operatorVO.plugin = pluginVO
        }
        operatorVO
    }

    def buildJob(): Unit ={
        val job = JobBuilder().buildJob(jobVO)
        val json = JsonInput.mapper.writeValueAsString(job)
        println(json)
    }


    def showActions(): Unit ={
        println(jobVO.name)
        println(JobBuilder().topological(jobVO.operators.asScala).map(x =>
            (s"action:${x._1}", s"operator:${x._2.name}\n  args:${x._2.args.asScala.mkString}\n  msg:${x._2.msg}")).mkString("\n"))
    }

    def showDag(): Unit ={
        //todo: jobVO.operators不能为空
        val begin = JobBuilder().topological(jobVO.operators.asScala).last._1
        val placeholder = new OperatorVO()
        placeholder.name = ""
        placeholder.source = Map("placeholder" -> "").asJava
        val dag = bfs(List(begin), List(List(List(begin))), jobVO.operators.asScala.toMap ++ Map("" -> placeholder))
        val dagHead = dag.head.map(x => x.map(x => x + List.fill(30 - x.length)(" ").mkString("")))
        dag.tail.foldLeft(dagHead)((l, r) => {
            var index = 0
            val out = r.map(opera => opera.map(x => {
                val num = l(index).map(x => x.length).sum
                index += 1
                x + List.fill(num - x.length)(" ").mkString("")
            }))
            println(l.map(x => x.mkString("|")).mkString("|", "|", "|"))
            out
        })
        println(begin)
    }

    def saveJob(name: String): Unit ={
        val file = new File(name + ".json")
        if(file.exists()){
            println("文件已存在, 换一个名字")
            print(">")
            saveJob(scanner.nextLine())
        } else {
            val json = JsonInput.mapper.writeValueAsString(jobVO)
            file.createNewFile()
            val writer = new PrintWriter(file)
            writer.println(json)
            writer.close()
            println(file.getAbsolutePath)
        }
    }

    def loadJob(): Unit ={
        println("job name")
        print(">")
        val file = new File( scanner.nextLine() + ".json")
        if (file.exists()){
            jobVO = JsonInput().readObject[JobVO](new FileInputStream(file))
            phase = 1
        } else {
            println("job not found")
        }
    }

    def loadIpassJob(): Unit = {
        println("job name")
        print(">")
        val file = new File(scanner.nextLine() + ".json")
        if (file.exists()){
            jobVO = JobModelInterpreter.interpretIpassJob(JsonInput().readObject[Job](new FileInputStream(file)), operators)
            phase = 1
        } else {
            println("job not found")
        }
    }

    def setJobArgs(): Unit ={
        jobVO.args.asScala.foreach(x => {
            println(s"set args ${x._1}")
            println(s"value now: ${x._2}")
            print(">")
            val value = scanner.nextLine()
            if(value != "") jobVO.args.put(x._1, value)
        })
    }

    def replay(): Unit ={
        jobVO = new JobVO
        phase = 0
    }

    def bfs(begin: Seq[String], res: Seq[Seq[Seq[String]]], map: Map[String, OperatorVO]): Seq[Seq[Seq[String]]] ={
        val addPlaceholder: Iterable[String] => List[String] = {
            case non if non.isEmpty => List("")
            case seq => seq.toList
        }
        begin.map(x => addPlaceholder(map(x).source.values().asScala)) match {
            case nextBegin if nextBegin.flatten.exists(x => x != "") => bfs(nextBegin.flatten, nextBegin +: res, map)
            case _ => res
        }
    }
}
