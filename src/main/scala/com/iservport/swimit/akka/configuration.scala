package com.iservport.swimit.akka

import akka.actor.ActorSystem
import com.datastax.driver.core.Cluster

/**
 * Configuração Cassandra.
 *
 * Originalmente publicado em https://github.com/eigengo/activator-akka-cassandra
 */
trait CassandraCluster {
  def cluster: Cluster
}

trait ConfigCassandraCluster extends CassandraCluster {
  def system: ActorSystem

  lazy val cluster: Cluster =
    Cluster.builder().addContactPoints(hosts: _*).withPort(port).build()
}