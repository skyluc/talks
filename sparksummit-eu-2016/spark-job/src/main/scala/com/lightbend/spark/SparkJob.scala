package com.lightbend.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scopt.OptionParser

object SparkJob {

  case class Sequence(
    value: Int,
    size: Int,
    partitions: Int)

  val seqR = "(\\d*),\\s*(\\d*),\\s*(\\d*)".r

  case class Config(
    instructions: List[Sequence] = List(),
    initialExecutors: Int = 2,
    maxExecutors: Int = 4,
    minExecutors: Int = 1,
    schedulerBacklogTimeout: Int = 10,
    executorIdleTimeout: Int = 10,
    sustainedSchedulerBacklogTimeout: Int = 10)

  def parseConfig(args: Array[String]): Config = {
    val parser = new OptionParser[Config]("spark-job") {
      arg[String]("instructions").action { (value, config) =>
        val blocks = value.split(Array('(', ')')).filter { !_.isEmpty() }
        val seqs = blocks.collect {
          case seqR(value, size, partitions) =>
            Sequence(value.toInt, size.toInt, partitions.toInt)
        }
        config.copy(instructions = seqs.to[List])
      }
      arg[String]("initialExecutors").action { (value, config) =>
        config.copy(initialExecutors = value.toInt)
      }
      arg[String]("maxExecutors").action { (value, config) =>
        config.copy(maxExecutors = value.toInt)
      }
      arg[String]("minExecutors").action { (value, config) =>
        config.copy(minExecutors = value.toInt)
      }
      arg[String]("schedulerBacklogTimeout").action { (value, config) =>
        config.copy(schedulerBacklogTimeout = value.toInt)
      }
      arg[String]("executorIdleTimeout").action { (value, config) =>
        config.copy(executorIdleTimeout = value.toInt)
      }
      arg[String]("sustainedSchedulerBacklogTimeout").action { (value, config) =>
        config.copy(sustainedSchedulerBacklogTimeout = value.toInt)
      }
      checkConfig { c =>
        if (c.instructions.isEmpty)
          failure("unable to parse instructions")
        else
          success
      }
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        config
      case None =>
        System.exit(1)
        null
    }
  }

  def main(args: Array[String]): Unit = {

    run(parseConfig(args))

  }

  private def run(config: Config) {

    val conf = new SparkConf

    if (conf.getOption("spark.master").isEmpty) {
      conf.set("spark.master", "local[2]")
    }

    if (!conf.get("spark.master").startsWith("local")) {
      conf.set("spark.dynamicAllocation.enabled", "true")
      conf.set("spark.shuffle.service.enabled", "true")
    }

    conf.set("spark.dynamicAllocation.initialExecutors", config.initialExecutors.toString())
    conf.set("spark.dynamicAllocation.maxExecutors", config.maxExecutors.toString())
    conf.set("spark.dynamicAllocation.minExecutors", config.minExecutors.toString())
    conf.set("spark.dynamicAllocation.schedulerBacklogTimeout", config.schedulerBacklogTimeout.toString())
    conf.set("spark.dynamicAllocation.executorIdleTimeout", config.executorIdleTimeout.toString())
    conf.set("spark.dynamicAllocation.sustainedSchedulerBacklogTimeout", config.sustainedSchedulerBacklogTimeout.toString())

    conf.setAppName("Dynamic allocation test")

    conf.set("spark.executor.uri", "/var/spark/spark-2.0.1-bin-hadoop2.7.tgz")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    hanoi(sc, 12, 4000, 4)
    
    while (true) {
      println("Started computation")

      val res = config.instructions.map { s =>
        hanoi(sc, s.value, s.size, s.partitions)
      }.reduce(_ + _)

      println(s"Done computation: $res")
    }

  }

  private def hanoi(sc: SparkContext, value: Int, size: Int, partitions: Int): Int = {
    val rdd = sc.makeRDD(List.fill(size)(value), partitions)
    rdd.map { i =>
      Hanoi.solve(i)
      i
    }.reduce(_ + _)
  }

}