package code.shatokhin.reactive_community_1.example3_graphs

import java.nio.file.Paths

import akka.stream.{ClosedShape, IOResult}
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, Keep, Merge, RunnableGraph, Source}
import akka.util.ByteString
import code.shatokhin.reactive_community_1.Materializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SimpleGraphs extends App with Materializer {

  val input = Source(1 to 10)

  val output = Flow[Int]
    .map(num => {
      println(num)
      ByteString(num + "\n")
    })
    .toMat(FileIO.toPath(Paths.get("numbers")))(Keep.right)


  val graph = GraphDSL.create(input, output)((mat1, mat2) => mat2) {
      implicit builder => (in, out) =>

      val bcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))

      val f1, f2, f3 = Flow[Int] map {
        _ + 10
      }
      val f4 = Flow[Int] map {
        _ + 100
      }

      in ~> f1 ~> bcast ~> f2 ~> merge
                  bcast ~> f4 ~> merge ~> f3 ~> out

      ClosedShape
    }

  val future: Future[IOResult] = RunnableGraph fromGraph graph run()

  future onComplete {
    _ => system.terminate()
  }
}

