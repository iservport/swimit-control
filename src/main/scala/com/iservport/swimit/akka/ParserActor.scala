package com.iservport.swimit.akka

import akka.actor._
import akka.event.LoggingReceive

/**
 * Created by mauriciofernandesdecastro on 18/05/15.
 */
class ParserActor(writerActor: ActorRef) extends Actor with ActorLogging {

  implicit val system: ActorSystem = context.system

  /**
   * Escreve os dados do acelerômetro e todos os dados da fila
   * @return
   */
  def receive = LoggingReceive {
    case q:scala.collection.mutable.Queue[SwimData] =>
      val s = q.front
      writerActor ! s
      q.tail.foreach(writerActor ! CalcData(s, _))
      writerActor ! AvgData(q)
    case _ =>
  }

}
