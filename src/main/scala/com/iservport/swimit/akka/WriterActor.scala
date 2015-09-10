package com.iservport.swimit.akka

import akka.actor.{ActorSystem, Actor, ActorLogging}
import akka.event.LoggingReceive
import com.datastax.driver.core.Cluster

/**
 * Created by mauriciofernandesdecastro on 08/06/15.
 */
class WriterActor extends Actor with ActorLogging {

  implicit val system: ActorSystem = context.system

  lazy val cluster: Cluster =
    Cluster.builder().addContactPoints(hosts: _*).withPort(port).build()

  val session = cluster.connect(Keyspaces.swimit)

  val pStmt1 = session.prepare("INSERT INTO wodi(s ,t ,e ,x ,y ,z ,h ) VALUES (?, ?, ?, ?, ?, ?, ?);")
  val pStmt2 = session.prepare("INSERT INTO wodr(s ,d ,t ,e ,rx ,ry ,rz ,rh ,rxy ,rxz ,ryz ,rxyz ) VALUES (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?);")
  val pStmt3 = session.prepare("INSERT INTO wodd(s ,t ,e ,ax ,ay ,az ,ah ,axy ,axz ,ayz ,axyz ,dx ,dy ,dz ,dh ,dtx ,dty ,dtz ,dth ,qx ,qy ,qz ,qh ) " +
    "VALUES (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?);")

  def saveData1(s: Integer, t: Integer, e: Integer, x: Integer, y: Integer, z: Integer, h: Integer): Unit = {
    session.executeAsync(pStmt1.bind(s, t, e, x, y, z, h))
  }

  def saveData2(s: Integer, d: Integer, t: Integer, e: Integer, rx: java.lang.Double, ry: java.lang.Double, rz: java.lang.Double, rh: java.lang.Double, rxy: java.lang.Double, rxz: java.lang.Double, ryz: java.lang.Double, rxyz: java.lang.Double): Unit = {
    session.executeAsync(pStmt2.bind(s, d, t, e, rx, ry, rz, rh, rxy, rxz, ryz, rxyz))
  }

  def saveData3(s: Integer, t: Integer, e: Integer
                , ax: java.lang.Double, ay: java.lang.Double, az: java.lang.Double, ah: java.lang.Double
                , axy: java.lang.Double, axz: java.lang.Double, ayz: java.lang.Double, axyz: java.lang.Double
                , dx: Integer, dy: Integer, dz: Integer, dh: Integer
                , dtx: Integer, dty: Integer, dtz: Integer, dth: Integer
                , qx: java.lang.Double, qy: java.lang.Double, qz: java.lang.Double, qh: java.lang.Double): Unit = {
    session.executeAsync(pStmt3.bind(s, t, e, ax, ay, az, ah, axy, axz, ayz, axyz, dx, dy, dz, dh, dtx, dty, dtz, dth, qx, qy, qz, qh))
  }

  override def receive = LoggingReceive {
    case s:SwimData =>
      println(s"${s.s} ${s.t} ${s.x} ${s.y} ${s.z} ${s.h}")
      saveData1(s.s,s.t,0,s.x,s.y,s.z,s.h)
    case c:CalcData =>
      saveData2(c.s,c.dt,c.t,0,c.rx,c.ry,c.rz,c.rh,c.rxy,c.rxz,c.ryz,c.rxyz)
    case a:AvgData =>
      saveData3(a.s,a.t,0,a.ax,a.ay,a.az,a.ah,a.axy,a.axz,a.ayz,a.axyz,a.dx,a.dy,a.dz,a.dh,a.dtx,a.dty,a.dtz,a.dth,a.qx,a.qy,a.qz,a.qh)
    case x:Any => println("qq " + x)
  }

}
