package com.iservport.swimit.akka

import akka.actor.{ActorRef, ActorSystem, Actor, ActorLogging}
import akka.event.LoggingReceive

import scala.io.Source

/**
 * Faz a leitura do arquivo csv e o divide em linhas. As linhas são
 * convertidas em instâncias da classe SwimData e uma fila de tamanho
 * máximo pré definido é mantida e enviada ao ator de interpretação.
 *
 * Created by mauriciofernandesdecastro on 18/05/15.
 */
class ReaderActor(parserActor: ActorRef) extends Actor with ActorLogging {

  implicit val system: ActorSystem = context.system

  def receive = LoggingReceive {
    case CsvReader(fileName, lastSessionNumber, queueMaxLength) => {
      val queue = new scala.collection.mutable.Queue[SwimData]()
      var n = 0
      var session = lastSessionNumber
      for (line <- Source.fromFile(fileName).getLines() if line.contains(";")) {
        n = n + 1
        val frags:Array[Int] = line.split(";").map(_.toInt)
        val l = frags.length match {
          case 5 =>
            val time = frags(3)
            if (time==0) {
              session = session + 1
              log.debug(s"New session: $session")
            }
            val s = SwimData(s = session, x = frags(0), y = frags(1), z = frags(2), t = time, h = frags(4))
            if (queue.length >= queueMaxLength) queue.dequeue()
            queue.enqueue(s)
            parserActor ! queue.reverse
          case _ => log.debug(s"Discarded: $line")
        }
      }
      println(s"$n linhas processadas")
    }
    case _ =>
  }

}
