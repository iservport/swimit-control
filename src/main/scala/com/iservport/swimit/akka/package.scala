package com.iservport.swimit

import com.typesafe.config.ConfigFactory

/**
 * Created by mauriciofernandesdecastro on 09/05/15.
 */
package object akka {

  val conf = ConfigFactory.load()
  val cassandraConf = conf.getConfig("akka-cassandra.main.db.cassandra")
  import scala.collection.JavaConversions._
  val hosts = cassandraConf.getStringList("hosts").toList
  val port = cassandraConf.getInt("port")

  private[akka] object Keyspaces {
    val swimit = "swimit"

  }}
