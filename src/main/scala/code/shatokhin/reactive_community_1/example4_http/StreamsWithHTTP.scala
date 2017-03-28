package code.shatokhin.reactive_community_1.example4_http

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import code.shatokhin.reactive_community_1.Materializer

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

object StreamsWithHTTP extends App with Materializer {

  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val messageFlow: Flow[Message, Message, Any] = Flow[Message]
    // wrapping to Futures since could have a text stream
    .collect {
      case TextMessage.Strict(text) => Future.successful(text)
      case TextMessage.Streamed(textStream) => textStream.runFold("")(_ + _)
        .flatMap(Future.successful)
    }
    // accessing Futures
    .mapAsync(1)(identity)
    .groupedWithin(1000, 1 second)
    .mapAsync(10)(messages => Future {
      println(messages)
      messages
    })
    .map(_ => TextMessage("Ok"))

  val route = path("measurements") {
    get {
      handleWebSocketMessages(messageFlow)
    }
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
}
