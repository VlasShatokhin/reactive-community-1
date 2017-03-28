package code.shatokhin.reactive_community_1.example1_flows

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Basics extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val source = Source[Int](1 to 100)

  val flow = Flow[Int].map(_ + "!")

  val sink = Sink.foreach[String](println)

  val sinkFuture = source
    .filter(_ % 2 == 0) // custom op
    .via(flow)
    .runWith(sink)


  sinkFuture onComplete {
    case Success(_) => system.terminate()
    case Failure(x) => println(x)
      system.terminate()
  }

}
