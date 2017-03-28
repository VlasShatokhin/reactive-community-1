package code.shatokhin.reactive_community_1

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

private [reactive_community_1] trait Materializer {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
}
