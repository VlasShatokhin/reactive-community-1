package code.shatokhin.reactive_community_1.example5_interops

import akka.NotUsed
import akka.stream.scaladsl.{Sink, Source}
import code.shatokhin.reactive_community_1.Materializer
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

import scala.collection.JavaConverters._
import scala.language.postfixOps

object InterOps extends App with Materializer {

  // RxScala Publisher
  val rxPub: Publisher[Int] = Flowable.fromIterable((1 to 100) asJava)

  // Akka Streams Source
  val akkaSource: Source[Int, NotUsed]#Repr[String] = Source.fromPublisher(rxPub).map(_.toString)

  // Akka Streams Publisher
  val akkaPub: Publisher[String] = akkaSource.runWith(Sink.asPublisher(true))

  // Reactor Publisher
  val reactorPub = Flux.from(akkaPub).map[String](_ + "\n")

  reactorPub.subscribe(print(_))
}
