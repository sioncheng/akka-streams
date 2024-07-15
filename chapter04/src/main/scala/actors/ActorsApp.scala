package actors

import akka.actor.typed.ActorSystem
import akka.actor.typed.SpawnProtocol
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Future
import akka.actor.typed.ActorRef
import actors.CallerActor.CallerMessage
import akka.actor.typed.Props
import actors.CallerActor.CallerProcessMessage
import actors.CallerActor.CallerEndMessage
import scala.util.Success
import scala.util.Failure

object ActorsApp {
  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem[SpawnProtocol.Command] =
        ActorSystem(SpawnProtocol(), "ActorSetup")
    implicit val ec = system.executionContext
    implicit val timeout = Timeout(3.seconds)

    import akka.actor.typed.scaladsl.AskPattern._

    val caller : Future[ActorRef[CallerMessage]] =
        system.ask(ref =>
            SpawnProtocol.Spawn(
            behavior = CallerActor(),
            name = "CallerActor",
            props = Props.empty,
            replyTo = ref
        ))

    // for (ref <- caller) {
    //     println(s"**** ref $ref")
    //     ref ! CallerProcessMessage("m1")
    //     ref ! CallerProcessMessage("m2")
    //     ref ! CallerEndMessage()
    //     ref ! CallerProcessMessage("m3")
    // }

    caller.onComplete {
        case Success(value) => 
            println(s"**** value $value")
            value ! CallerProcessMessage("m1")
            value ! CallerProcessMessage("m2")
            value ! CallerEndMessage()
            value ! CallerProcessMessage("m3")
            value ! CallerProcessMessage("m4")
        case Failure(exception) => 
            println(s"exception $exception")
    }

    scala.io.StdIn.readLine("#### press entry to exit\r\n")

    system.terminate()
  }
}
