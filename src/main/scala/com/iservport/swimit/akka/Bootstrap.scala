package com.iservport.swimit.akka

import akka.actor.{Props, ActorSystem}
import akka.kernel.Bootable

/**
 * Created by mauriciofernandesdecastro on 18/05/15.
 */
class Bootstrap extends Bootable {

  // incializa atores
  val system = ActorSystem("swimmit")
  lazy val writerActor = system.actorOf(Props(new WriterActor()), "writerActor")
  lazy val parserActor = system.actorOf(Props(new ParserActor(writerActor)), "parserActor")
  lazy val readerActor = system.actorOf(Props(new ReaderActor(parserActor)), "readerActor")

  val dataFile = "/Users/mauriciofernandesdecastro/workspace/swimit-control/src/main/resources/oscar2.txt"
  val lastSessionNumber = 3999
  val queueMaxLength = 60

  def startup = {
    readerActor ! CsvReader(dataFile, lastSessionNumber, queueMaxLength)
  }

  def shutdown = {
    system.shutdown()
    system.awaitTermination()
  }

}
