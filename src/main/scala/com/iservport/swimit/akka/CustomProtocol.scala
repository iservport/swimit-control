package com.iservport.swimit.akka

/**
 * Created by mauriciofernandesdecastro on 19/05/15.
 */
sealed trait CustomProtocol
case class CsvReader(fileName: String, lastSession: Int = 1, queueMaxLength: Int = 5) extends CustomProtocol
case class SwimData(s: Int = 0, x: Int = 0, y: Int = 0, z: Int = 0, t: Int = 0, h: Int = 0) extends CustomProtocol
case class CalcData(d: SwimData, p: SwimData) extends CustomProtocol {
  val s = d.s
  val freq = 25
  val interval = 1000/freq
  val t = d.t
  val ft: Float = t - p.t
  val fraction: Float = ft / interval + 0.5f //fraction to normalize intervals
  val dt = fraction.toInt * interval
  val rx: Double = (d.x - p.x) / dt
  val ry: Double = (d.y - p.y) / dt
  val rz: Double = (d.z - p.z) / dt
  val rh: Double = (d.h - p.h) / dt
  val rxy: Double = (qs(d.x,d.y) - qs(p.x,p.y)) / dt
  val rxz: Double = (qs(d.x,d.z) - qs(p.x,p.z)) / dt
  val ryz: Double = (qs(d.y,d.z) - qs(p.y,p.z)) / dt
  val rxyz: Double = (qs(d.x,d.y,d.z) - qs(p.x,p.y,p.z)) / dt
  def qs(a: Int, b: Int, c: Int = 0): Double = Math.sqrt(a * a + b * b + c * c)
  // testes
//  def rDiv(rdx: Double, rdy: Double, rdz: Double, dt: Int) = (rdx/dt, rdy/dt, rdz/dt)
//  def d(a: Double, b: Double) = a - b
//  val c = rDiv(d(s.x,p.x), d(s.y,p.y), d(s.z,p.z), dt)

}
case class AvgData(q: scala.collection.mutable.Queue[SwimData]) {

  var p = SwimData()
  var minx = 0
  var miny = 0
  var minz = 0
  var minh = 0
  var maxx = 0
  var maxy = 0
  var maxz = 0
  var maxh = 0
  var tminx = 0
  var tminy = 0
  var tminz = 0
  var tminh = 0
  var tmaxx = 0
  var tmaxy = 0
  var tmaxz = 0
  var tmaxh = 0
  for (c <- q.iterator) {
    p = SwimData(c.s, c.x+p.x, c.y+p.y, c.z+p.z, c.t, c.h+p.h)
    if (c.x < minx) { minx = c.x; tminx = c.t }
    if (c.y < miny) { miny = c.y; tminy = c.t }
    if (c.z < minz) { minz = c.z; tminz = c.t }
    if (c.h < minh) { minh = c.h; tminh = c.t }
    if (c.x > maxx) { maxx = c.x; tmaxx = c.t }
    if (c.y > maxy) { maxy = c.y; tmaxy = c.t }
    if (c.z > maxz) { maxz = c.z; tmaxz = c.t }
    if (c.h > maxh) { maxh = c.h; tmaxh = c.t }
  }
  val s = p.s
  val size = q.length
  val ax:Double = p.x / size
  val ay:Double = p.y / size
  val az:Double = p.z / size
  val ah:Double = p.h / size
  val axy: Double = qs(ax,ay)
  val axz: Double = qs(ax,az)
  val ayz: Double = qs(ay,az)
  val axyz: Double = qs(ax,ay,az)
  val dx = maxx - minx
  val dy = maxy - miny
  val dz = maxz - minz
  val dh = maxh - minh
  val dtx = tmaxx - tminx
  val dty = tmaxy - tminy
  val dtz = tmaxz - tminz
  val dth = tmaxh - tminh
  val qx:Double = dx / ax
  val qy:Double = dy / ay
  val qz:Double = dz / az
  val qh:Double = dh / ah
  val t = p.t
  def qs(a: Double, b: Double, c: Double = 0): Double = Math.sqrt(a * a + b * b + c * c)

}

