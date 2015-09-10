package com.iservport.swimit.akka

import org.scalatest.FlatSpec

/**
 * Created by mauriciofernandesdecastro on 26/07/15.
 */
class CustomProtocolSpec extends FlatSpec {

  "AvgDataA queue of SwimData" should "produce the right sum" in {
    val queue = new scala.collection.mutable.Queue[SwimData]()
    queue.enqueue(SwimData(4,10,100,0,1000), SwimData(6,-20,-200,9,2000))
    val avg2 = AvgData(queue)
    assert(avg2.ax == 5)
    assert(avg2.ay == -5)
    assert(avg2.az == -50)
    assert(avg2.ah == 1500)
    queue.enqueue(SwimData(4,40,400,0,0))
    val avg3 = AvgData(queue)
    assert(avg3.ax == 4)
    assert(avg3.ay == 10)
    assert(avg3.az == 100)
    assert(avg3.ah == 1000)
  }
}
