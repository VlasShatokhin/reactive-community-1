package code.shatokhin.reactive_community_1.example2_backpressure

import akka.stream.ThrottleMode
import akka.stream.scaladsl.{Flow, Sink, Source}
import code.shatokhin.reactive_community_1.Materializer

import scala.concurrent.duration._
import scala.language.postfixOps

object SlowSink extends App with Materializer {

  val source = Source[Int](1 to 1000)
    .throttle(1, 200 milliseconds, 1, ThrottleMode.shaping)
    .map(num => {
      println(s"source -> : $num")
      num
    })
    // runs when back-pressured from downstream
    .conflate((num1, num2) => {
      val avg = (num1 + num2) / 2
      println(s" ! conflated: $avg ")
      avg
    })

  val sink = Flow[Int]
    //.throttle(1, 1 seconds, 1, ThrottleMode.shaping)
    .to(Sink.foreach(num => println(s" -> sink: $num")))

  source.to(sink).run()
}