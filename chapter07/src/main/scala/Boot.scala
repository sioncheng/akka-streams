import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.ExecutionContextExecutor
import akka.stream.scaladsl.Source
import akka.NotUsed
import scala.concurrent.Future
import akka.Done

object Boot extends App {
  
  implicit val system: ActorSystem[_] =
    ActorSystem(Behaviors.empty, "ActorSystem")

  implicit val ec : ExecutionContextExecutor =
    system.executionContext

  val source: Source[Int, NotUsed] = 
    Source(1 to 100)
  val done: Future[Done] = //source.runForeach(i => println(s"e $i"))
    source.filter(i => i % 3 == 0)
        .map(i => s"n $i")
        .runForeach(println)

  done.onComplete(_ => system.terminate())
}
